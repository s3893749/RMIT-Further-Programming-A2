package com.jackgharris.cosc2288.a2.controllers.components;

import com.jackgharris.cosc2288.a2.core.MyHealth;
import com.jackgharris.cosc2288.a2.models.Record;
import com.jackgharris.cosc2288.a2.utility.FXMLUtility;
import com.jackgharris.cosc2288.a2.utility.Resource;
import com.jackgharris.cosc2288.a2.utility.Validation;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;

public class RecordWithLineChartController extends RecordPageController{


    @FXML
    private TextField addRecordInput;

    @FXML
    private Button addRecordButton;

    @FXML
    private LineChart<String, Float> recordChart;

    @FXML
    private Label valueInputLabel;


    @Override
    public void construct() {
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

        this.recordTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        this.dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        this.valueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));

        this.valueInputLabel.setText(this.recordType);
        this.updateModels();
    }


    public void datePickerUpdated(){
        this.addRecordInput.setDisable(false);
    }

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

    @Override
    public void addRecord(){

        Record.add(new Record(0,this.recordType,MyHealth.getInstance().getUser().getId(),this.addRecordInput.getText(), this.addRecordDatePicker.getValue().toString()));
        this.updateModels();

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


    @Override
    public void showRecord() throws IOException {

        if(!MyHealth.isStageShown("showRecord")){
            Stage stage = new Stage();
            stage.getProperties().put("id","showRecord");
            stage.setResizable(false);
            FXMLLoader loader = new FXMLLoader(FXMLUtility.showRecord);
            Scene scene = new Scene(loader.load());
            stage.setTitle("MyHealth Record | Show/Edit/Delete");
            stage.getIcons().add(Resource.importExportFavicon());
            ShowRecordController controller = loader.getController();
            controller.setRecord(this.recordTable.getSelectionModel().getSelectedItem());
            stage.setScene(scene);
            stage.show();
        }else{
            MyHealth.getStageById("showRecord").requestFocus();
        }

    }




}
