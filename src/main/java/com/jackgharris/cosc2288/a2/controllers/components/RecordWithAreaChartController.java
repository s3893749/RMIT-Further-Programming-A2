//**** PACKAGE ****\\
package com.jackgharris.cosc2288.a2.controllers.components;

//**** PACKAGE IMPORTS ****\\
import com.jackgharris.cosc2288.a2.core.MyHealth;
import com.jackgharris.cosc2288.a2.models.BloodPressure;
import com.jackgharris.cosc2288.a2.models.Record;
import com.jackgharris.cosc2288.a2.utility.Time;
import com.jackgharris.cosc2288.a2.utility.Validation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;


//**** START RECORD WITH AREA CHART CONTROLLER CLASS ****\\
public class RecordWithAreaChartController extends RecordPageController{


    //**** FXML IMPORTS ****\\
    //The follow class variables match the id's of the match fxml objects for this scene
    //this allows the class and its method to interact with them in an easy capacity.

    //Area chart is the chart that shows the blood pressure
    @FXML
    private AreaChart<String, Integer> areaChart;

    //Text field add record input 1, is the input for the first part of the blood pressure value
    @FXML
    private TextField addRecordInput;

    //Text field add record input 2, is the input for the second part of the blood pressure value
    @FXML
    private TextField addRecordInputTwo;

    //Add record button adds the record to the database
    @FXML
    private Button addRecordButton;


    //**** CONSTRUCT METHOD ***\\
    //This method is processed after the record is set as is the constructor for the controller.
    @Override
    public void construct() {

        //disable all our buttons and inputs to start
        this.addRecordButton.setDisable(true);
        this.viewRecordButton.setDisable(true);
        this.addRecordInput.setDisable(true);
        this.addRecordInputTwo.setDisable(true);

        //call our callback function to set the datepicker to disable any used dates.
        Callback<DatePicker, DateCell> dayCellFactory  = this.disableUsedDates();
        this.addRecordDatePicker.setDayCellFactory(dayCellFactory);

        //set the text of our value 1 column
        this.valueColumn.setText("Systolic");

        //enable resize to ensure the table columns are evenly spaced
        this.recordTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        //disable the date picker text input
        this.addRecordDatePicker.getEditor().setDisable(true);

        //set the date and value 1 column property value factory
        this.dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        this.valueColumn.setCellValueFactory(new PropertyValueFactory<>("systolic"));

        //create our second column called Diastolic and set its text and cell value factory
        TableColumn<Record, Integer> valueColumnTwo = new TableColumn<>();
        valueColumnTwo.setText("Diastolic");
        valueColumnTwo.setCellValueFactory(new PropertyValueFactory<>("diastolic"));

        //add our second column to the table
        this.recordTable.getColumns().add(valueColumnTwo);

        //set the static instance to this
        this.setInstance(this);

        //update our models
        this.updateModels();
    }

    //**** UPDATE MODELS METHOD ****\\
    //This method will clear and rerender all the models in the chart and table
    @Override
    public void updateModels() {
        //Get our records from the database but limit them via the current set limit.
        ObservableList<Record> records = Record.where("type", this.recordType).withCurrentUser().limit(this.limit).sort("date").updateCache().get();

        //create our blood pressure list
        ObservableList<BloodPressure> bloodPressures = FXCollections.observableArrayList();

        //cast all our records to blood pressure objects
        records.forEach((n) -> {
            bloodPressures.add(new BloodPressure(n.getId(), n.getType(), n.getUserId(), n.getValue(), n.getDate().toString(), Time.now()));
        });

        //-------------------------------------------------------------\\
        //                    UPDATE CHART CONTENTS                    \\
        //-------------------------------------------------------------\\
        //
        //This section of the method takes our records and updates the
        // chart model contents.
        XYChart.Series<String, Integer> series1 = new XYChart.Series<>();
        XYChart.Series<String, Integer> series2 = new XYChart.Series<>();

        //add both our data points to series 1 and 2
        bloodPressures.forEach((n) -> {
            series1.getData().add(new XYChart.Data<>(n.getDate().toString(), n.getSystolic()));
            series2.getData().add(new XYChart.Data<>(n.getDate().toString(), n.getDiastolic()));
        });

        //clear the old data
        this.areaChart.getData().clear();

        //disable the legend
        this.areaChart.setLegendVisible(false);

        //add series 1 and 2 to the chart
        this.areaChart.getData().add(series1);
        this.areaChart.getData().add(series2);

        //-------------------------------------------------------------\\
        //                    UPDATE TABLE CONTENTS                    \\
        //-------------------------------------------------------------\\
        //
        //This code updates the contents shown in our table
        this.recordTable.getItems().clear();
        this.recordTable.getItems().addAll(bloodPressures);

        //Check if we have a limit set, if not then simply use the above cache, else update the cache.
        if (this.limit != 0) {
            Record.where("type", this.recordType).withCurrentUser().sort("date").updateCache().get();
        }
    }

    //**** ADD RECORD METHOD ****\\
    //This record adds a new record to the database
    @Override
    public void addRecord(){

        //create and add the new record
        Record.add(new Record(0,this.recordType,MyHealth.getInstance().getUser().getId(),this.addRecordInput.getText()+"/"+this.addRecordInputTwo.getText(), this.addRecordDatePicker.getValue().toString(), Time.now()));

        //update the models to show the new record
        this.updateModels();

        //reset all out inputs back to default
        this.addRecordInput.setText(null);
        this.addRecordInputTwo.setText(null);

        this.addRecordInput.getStyleClass().removeAll("text-field-success");
        this.addRecordInputTwo.getStyleClass().removeAll("text-field-success");

        this.addRecordDatePicker.setValue(null);

        this.addRecordButton.setDisable(true);
        this.addRecordInput.setDisable(true);
        this.addRecordInputTwo.setDisable(true);
    }

    //**** DATE PICKER UPDATED METHOD ****\\
    //This method will disable the input's when updated (date selected)
    public void datePickerUpdated(){

        this.addRecordInput.setDisable(false);
        this.addRecordInputTwo.setDisable(false);

    }

    //**** VALUE INPUT UPDATED ***\\
    //This method will validate both our input fields to ensure that they
    //are compliant with the storage requirements for blood pressure, if so
    //then this method will enable the add record button.
    public void valueInputUpdated(){

        //declare out outcomes from the check
        boolean inputOneOutcome = false;
        boolean inputTwoOutcome = false;

        //validate our first input field and set either error or success
        if(!Validation.isInteger(this.addRecordInput.getText())){
            this.addRecordInput.getStyleClass().add("text-field-error");
            this.addRecordInput.getStyleClass().removeAll("text-field-success");
            inputOneOutcome = false;
        }else{
            this.addRecordInput.getStyleClass().removeAll("text-field-error");
            this.addRecordInput.getStyleClass().add("text-field-success");
            inputOneOutcome = true;
        }

        //validate our second input field and set either success or error
        if(!Validation.isInteger(this.addRecordInputTwo.getText())){
            this.addRecordInputTwo.getStyleClass().add("text-field-error");
            this.addRecordInputTwo.getStyleClass().removeAll("text-field-success");
            inputTwoOutcome = false;
        }else{
            this.addRecordInputTwo.getStyleClass().removeAll("text-field-error");
            this.addRecordInputTwo.getStyleClass().add("text-field-success");
            inputTwoOutcome = true;
        }

        //finally check that both are passing validation, if so enable the button
        if(inputOneOutcome && inputTwoOutcome){
            this.addRecordButton.setDisable(false);
        }else{
            //else ensure its disabled
            this.addRecordButton.setDisable(true);
        }

    }


}
