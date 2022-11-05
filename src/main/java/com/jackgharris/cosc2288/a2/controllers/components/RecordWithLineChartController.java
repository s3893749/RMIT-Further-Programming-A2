package com.jackgharris.cosc2288.a2.controllers.components;

import com.jackgharris.cosc2288.a2.core.MyHealth;
import com.jackgharris.cosc2288.a2.models.Record;
import com.jackgharris.cosc2288.a2.models.Weight;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

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

    }

    public void setRecordType(String recordType){
        this.recordType = recordType;
        this.recordName.setText(recordType);
        MyHealth.getInstance().setSelectedRecordType(this.recordType);


        this.recordTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        this.dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        this.valueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
        this.updateTable();
        this.updateChart();
    }

    public void datePickerUpdated(){
        this.addRecordInput.setDisable(false);
    }

    public void valueInputUpdated(){
        this.addRecordButton.setDisable(false);
    }

    public void addRecord(){

        Record.add(new Record(0,this.recordType,MyHealth.getInstance().getUser().getId(),this.addRecordInput.getText(), this.addRecordDatePicker.getValue().toString()));
        this.updateTable();
        this.updateChart();

        this.addRecordButton.setDisable(true);
        this.addRecordInput.setText(null);
        this.addRecordInput.setDisable(true);
        this.addRecordDatePicker.setValue(null);

        this.addRecordInput.setDisable(true);

    }

    private void updateTable(){
        this.recordTable.getItems().clear();
        this.recordTable.getItems().addAll(Record.where("user_id",String.valueOf(MyHealth.getInstance().getUser().getId())).where("type",this.recordType).updateCache().sort("date").get());
    }

    private void updateChart(){
        this.recordChart.getData().clear();
        XYChart.Series<String, Float> series = new XYChart.Series<>();

        for (Record record : Record.where("type", this.recordType).sort("date").get()){
            series.getData().add(new XYChart.Data<>(record.getDate().toString(),Float.valueOf(record.getValue())));
        }

        this.recordChart.setLegendVisible(false);
        this.recordChart.getData().add(series);
    }

    private Callback<DatePicker, DateCell> disableUsedDates() {

        final Callback<DatePicker, DateCell> dayCellFactory = (final DatePicker datePicker) -> new DateCell() {

            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);

                for (Record record: Record.where("type",MyHealth.getInstance().getSelectedRecordType()).getAndCache()){
                    if(item.equals(record.getDate())){
                        setDisable(true);
                        setStyle("-fx-background-color: #c72929;");

                    }
                }
            }
        };
        return dayCellFactory;
    }


}
