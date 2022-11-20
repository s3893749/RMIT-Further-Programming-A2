package com.jackgharris.cosc2288.a2.controllers.components;

import com.jackgharris.cosc2288.a2.core.MyHealth;
import com.jackgharris.cosc2288.a2.models.Record;
import com.jackgharris.cosc2288.a2.utility.FXMLUtility;
import com.jackgharris.cosc2288.a2.utility.Resource;
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
import java.time.LocalDate;

public class RecordWithLineChartController {

    @FXML
    private Label recordName;

    @FXML
    private TableView<Record> recordTable;

    @FXML
    private TableColumn<Record, String> dateColumn;

    @FXML
    private TableColumn<Record, String> valueColumn;

    @FXML
    private DatePicker addRecordDatePicker;

    @FXML
    private TextField addRecordInput;

    @FXML
    private Button addRecordButton;

    @FXML
    private Button viewRecordButton;

    @FXML
    private LineChart<String, Float> recordChart;

    private String recordType;

    private int limit;

    public static RecordWithLineChartController instance;

    public void initialize(){

        //set our selection buttons and input to be disabled by default
        this.addRecordButton.setDisable(true);
        this.addRecordInput.setDisable(true);
        this.viewRecordButton.setDisable(true);

        //disable the date picker text input
        this.addRecordDatePicker.getEditor().setDisable(true);

        //call our callback function to set the datepicker to disable any used dates.
        Callback<DatePicker, DateCell> dayCellFactory  = this.disableUsedDates();
        this.addRecordDatePicker.setDayCellFactory(dayCellFactory);

        RecordWithLineChartController.instance = this;

    }

    public void setRecordType(String recordType){
        this.recordType = recordType;
        this.recordName.setText(recordType);
        MyHealth.getInstance().setSelectedRecordType(this.recordType);


        this.recordTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        this.dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        this.valueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
        this.updateModels();

    }

    public void datePickerUpdated(){
        this.addRecordInput.setDisable(false);
    }

    public void valueInputUpdated(){
        this.addRecordButton.setDisable(false);
    }

    public void addRecord(){

        Record.add(new Record(0,this.recordType,MyHealth.getInstance().getUser().getId(),this.addRecordInput.getText(), this.addRecordDatePicker.getValue().toString()));
        this.updateModels();

        this.addRecordButton.setDisable(true);
        this.addRecordInput.setText(null);
        this.addRecordInput.setDisable(true);
        this.addRecordDatePicker.setValue(null);

        this.addRecordInput.setDisable(true);
    }

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

    public void showAllRecords(){
        this.limit = 0;
        this.updateModels();
    }

    public void showLastWeek(){
        this.limit = 7;
        this.updateModels();
    }

    public void showLastMonth(){
        this.limit = 30;
        this.updateModels();
    }


    private Callback<DatePicker, DateCell> disableUsedDates() {

        final Callback<DatePicker, DateCell> dayCellFactory = (final DatePicker datePicker) -> new DateCell() {

            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                for (Record record: Record.where("type",MyHealth.getInstance().getSelectedRecordType()).fromCache().get()){
                    if(item.equals(record.getDate())){
                        setDisable(true);
                        setStyle("-fx-background-color: -fx-error;");

                    }
                }
            }
        };

        return dayCellFactory;
    }

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

    public void updateRecordSelection(){
        this.viewRecordButton.setDisable(this.recordTable.getSelectionModel().getSelectedItem() == null);
    }


}
