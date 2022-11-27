//**** PACKAGE ****\\
package com.jackgharris.cosc2288.a2.controllers.components;

//**** PACKAGE IMPORTS ****\\
import com.jackgharris.cosc2288.a2.core.MyHealth;
import com.jackgharris.cosc2288.a2.models.Record;
import com.jackgharris.cosc2288.a2.utility.Time;
import com.jackgharris.cosc2288.a2.utility.Validation;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

//**** START RECORD WITH LINE CHART CONTROLLER CLASS ****\\
public class RecordWithLineChartController extends RecordPageController{


    //**** FXML IMPORTS ****\\
    //The follow class variables match the id's of the match fxml objects for this scene
    //this allows the class and its method to interact with them in an easy capacity.

    //Add record input field, accepts the new record value
    @FXML
    private TextField addRecordInput;

    //Add record button, is the button that triggers a new record to be added
    @FXML
    private Button addRecordButton;

    //Line chart, shows all the record values in a chart
    @FXML
    private LineChart<String, Float> recordChart;

    //Value input value, shows what type of record is currently shown
    @FXML
    private Label valueInputLabel;

    //**** CONSTRUCT METHOD ***\\
    //This method is processed after the record is set as is the constructor for the controller.
    @Override
    public void construct() {

        //set the static instance to this instance
        this.setInstance(this);

        //set our selection buttons and input to be disabled by default
        this.addRecordButton.setDisable(true);
        this.addRecordInput.setDisable(true);
        this.viewRecordButton.setDisable(true);

        //disable the date picker text input
        this.addRecordDatePicker.getEditor().setDisable(true);

        //call our callback function to set the datepicker to disable any used dates.
        Callback<DatePicker, DateCell> dayCellFactory  = this.disableUsedDates();
        this.addRecordDatePicker.setDayCellFactory(dayCellFactory);

        //set our table resize, data and value property factories
        this.recordTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        this.dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        this.valueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));

        //set the record to for the input label
        this.valueInputLabel.setText(this.recordType);

        //update our models
        this.updateModels();
    }

    //**** DATE PICKER UPDATED METHOD ****\\
    //This method will update the record input to enable it once a date has been selected
    public void datePickerUpdated(){
        this.addRecordInput.setDisable(false);
    }

    //**** VALUE INPUT UPDATED METHOD ****\\
    //This method will validate the value input once it's updated via a keypress
    public void valueInputUpdated(){

        if(!Validation.isFloat(this.addRecordInput.getText())){
            this.addRecordInput.getStyleClass().add("text-field-error");
            this.addRecordInput.getStyleClass().removeAll("text-field-success");
            this.addRecordButton.setDisable(true);
        }else{
            this.addRecordInput.getStyleClass().removeAll("text-field-error");
            this.addRecordInput.getStyleClass().add("text-field-success");
            this.addRecordButton.setDisable(false);
        }
    }

    //**** ADD RECORD METHOD ****\\
    @Override
    public void addRecord(){

        //add the new record
        Record.add(new Record(0,this.recordType,MyHealth.getInstance().getUser().getId(),this.addRecordInput.getText(), this.addRecordDatePicker.getValue().toString(), Time.now()));
        //update the models
        this.updateModels();

        //reset all our inputs and buttons to default, null & disabled if required
        this.addRecordButton.setDisable(true);
        this.addRecordInput.setText(null);
        this.addRecordInput.setDisable(true);
        this.addRecordDatePicker.setValue(null);
        this.addRecordInput.setDisable(true);
    }

    @Override
    public void updateModels(){
        //Get our records from the database but limit them via the current set limit.
        ObservableList<Record> records = Record.where("type",this.recordType).withCurrentUser().limit(this.limit).sort("date").updateCache().get();

        //-------------------------------------------------------------\\
        //                    UPDATE CHART CONTENTS                    \\
        //-------------------------------------------------------------\\
        //
        //This section of the method takes our records and updates the
        // chart model contents.
        this.recordChart.getData().clear();
        XYChart.Series<String, Float> series = new XYChart.Series<>();

        for (Record record : records){
            series.getData().add(new XYChart.Data<>(record.getDate().toString(),Float.valueOf(record.getValue())));
        }

        this.recordChart.setLegendVisible(false);
        this.recordChart.getData().add(series);

        //-------------------------------------------------------------\\
        //                    UPDATE TABLE CONTENTS                    \\
        //-------------------------------------------------------------\\
        //
        //This code updates the contents shown in our table
        this.recordTable.getItems().clear();
        this.recordTable.getItems().addAll(records);

        //Check if we have a limit set, if not then simply use the above cache, else update the cache.
        if(this.limit != 0){
            Record.where("type",this.recordType).withCurrentUser().sort("date").updateCache().get();
        }
    }

}
