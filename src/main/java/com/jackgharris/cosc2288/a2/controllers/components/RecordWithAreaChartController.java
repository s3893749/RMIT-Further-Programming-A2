package com.jackgharris.cosc2288.a2.controllers.components;

import com.jackgharris.cosc2288.a2.core.MyHealth;
import com.jackgharris.cosc2288.a2.models.BloodPressure;
import com.jackgharris.cosc2288.a2.models.Record;
import com.jackgharris.cosc2288.a2.utility.FXMLUtility;
import com.jackgharris.cosc2288.a2.utility.Resource;
import com.jackgharris.cosc2288.a2.utility.Validation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;


public class RecordWithAreaChartController extends RecordPageController{


    @FXML
    private AreaChart<String, Integer> areaChart;

    @FXML
    private TextField addRecordInput;

    @FXML
    private TextField addRecordInputTwo;

    @FXML
    private Button addRecordButton;



    @Override
    public void construct() {
        this.addRecordButton.setDisable(true);
        this.viewRecordButton.setDisable(true);
        this.addRecordInput.setDisable(true);
        this.addRecordInputTwo.setDisable(true);

        //call our callback function to set the datepicker to disable any used dates.
        Callback<DatePicker, DateCell> dayCellFactory  = this.disableUsedDates();
        this.addRecordDatePicker.setDayCellFactory(dayCellFactory);

        this.valueColumn.setText("Systolic");

        this.recordTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        //disable the date picker text input
        this.addRecordDatePicker.getEditor().setDisable(true);

        this.dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        this.valueColumn.setCellValueFactory(new PropertyValueFactory<>("systolic"));

        TableColumn<Record, Integer> valueColumnTwo = new TableColumn<>();
        valueColumnTwo.setText("Diastolic");
        valueColumnTwo.setCellValueFactory(new PropertyValueFactory<>("diastolic"));

        this.recordTable.getColumns().add(valueColumnTwo);

        this.setInstance(this);

        this.updateModels();
    }

    @Override
    public void updateModels() {
        //Get our records from the database but limit them via the current set limit.
        ObservableList<Record> records = Record.where("type", this.recordType).withCurrentUser().limit(this.limit).sort("date").updateCache().get();

        //create our blood pressure list
        ObservableList<BloodPressure> bloodPressures = FXCollections.observableArrayList();

        //cast all our records to blood pressure objects
        records.forEach((n) -> {
            bloodPressures.add(new BloodPressure(n.getId(), n.getType(), n.getUserId(), n.getValue(), n.getDate().toString()));
        });

        //-------------------------------------------------------------\\
        //                    UPDATE CHART CONTENTS                    \\
        //-------------------------------------------------------------\\
        //
        //This section of the method takes our records and updates the
        // chart model contents.
        XYChart.Series<String, Integer> series1 = new XYChart.Series<>();
        XYChart.Series<String, Integer> series2 = new XYChart.Series<>();

        bloodPressures.forEach((n) -> {
            series1.getData().add(new XYChart.Data<>(n.getDate().toString(), n.getSystolic()));
            series2.getData().add(new XYChart.Data<>(n.getDate().toString(), n.getDiastolic()));
        });

        this.areaChart.getData().clear();

        this.areaChart.setLegendVisible(false);

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

    @Override
    public void addRecord(){

        Record.add(new Record(0,this.recordType,MyHealth.getInstance().getUser().getId(),this.addRecordInput.getText()+"/"+this.addRecordInputTwo.getText(), this.addRecordDatePicker.getValue().toString()));
        this.updateModels();


        this.addRecordInput.setText(null);
        this.addRecordInputTwo.setText(null);

        this.addRecordInput.getStyleClass().removeAll("text-field-success");
        this.addRecordInputTwo.getStyleClass().removeAll("text-field-success");

        this.addRecordDatePicker.setValue(null);

        this.addRecordButton.setDisable(true);
        this.addRecordInput.setDisable(true);
        this.addRecordInputTwo.setDisable(true);

    }

    public void datePickerUpdated(){

        this.addRecordInput.setDisable(false);
        this.addRecordInputTwo.setDisable(false);

    }

    public void valueInputUpdated(){

        boolean inputOneOutcome = false;
        boolean inputTwoOutcome = false;

        if(!Validation.isInteger(this.addRecordInput.getText())){
            this.addRecordInput.getStyleClass().add("text-field-error");
            this.addRecordInput.getStyleClass().removeAll("text-field-success");
            inputOneOutcome = false;
        }else{
            this.addRecordInput.getStyleClass().removeAll("text-field-error");
            this.addRecordInput.getStyleClass().add("text-field-success");
            inputOneOutcome = true;
        }

        if(!Validation.isInteger(this.addRecordInputTwo.getText())){
            this.addRecordInputTwo.getStyleClass().add("text-field-error");
            this.addRecordInputTwo.getStyleClass().removeAll("text-field-success");
            inputTwoOutcome = false;
        }else{
            this.addRecordInputTwo.getStyleClass().removeAll("text-field-error");
            this.addRecordInputTwo.getStyleClass().add("text-field-success");
            inputTwoOutcome = true;
        }

        if(inputOneOutcome && inputTwoOutcome){
            this.addRecordButton.setDisable(false);
        }else{
            this.addRecordButton.setDisable(true);
        }

    }

    @Override
    public void showRecord() throws IOException {
        if(!MyHealth.isStageShown("showRecord")){
            Stage stage = new Stage();
            stage.getProperties().put("id","showRecord");
            stage.setResizable(false);
            FXMLLoader loader = new FXMLLoader(FXMLUtility.showRecord);
            Scene scene = new Scene(loader.load());
            stage.setTitle("MyHealth Record | Show/Edit/Delete");
            stage.getIcons().add(Resource.favicon());
            ShowRecordController controller = loader.getController();
            controller.setRecord(this.recordTable.getSelectionModel().getSelectedItem());
            stage.setScene(scene);
            stage.show();
        }else{
            MyHealth.getStageById("showRecord").requestFocus();
        }
    }
}
