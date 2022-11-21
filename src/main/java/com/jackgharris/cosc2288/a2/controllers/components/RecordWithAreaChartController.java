package com.jackgharris.cosc2288.a2.controllers.components;

import com.jackgharris.cosc2288.a2.core.MyHealth;
import com.jackgharris.cosc2288.a2.models.BloodPressure;
import com.jackgharris.cosc2288.a2.models.Record;
import com.jackgharris.cosc2288.a2.utility.Validation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.time.LocalDate;

public class RecordWithAreaChartController {

    @FXML
    private Label recordName;

    @FXML
    private TableView<Record> recordTable;

    @FXML
    private TableColumn<BloodPressure,String> dateColumn;

    @FXML
    private TableColumn<BloodPressure,Integer> valueColumn;

    @FXML
    private TableColumn<BloodPressure, Integer> valueColumnTwo;

    @FXML
    private AreaChart<String, Integer> areaChart;

    @FXML
    private DatePicker addRecordDatePicker;

    @FXML
    private TextField addRecordInput;

    @FXML
    private TextField addRecordInputTwo;

    @FXML
    private Button addRecordButton;

    @FXML
    private Button viewRecordButton;

    private String recordType;

    private int limit;

    public static RecordWithAreaChartController instance;

    public void initialize(){
        this.addRecordButton.setDisable(true);
        this.viewRecordButton.setDisable(true);
        this.addRecordInput.setDisable(true);
        this.addRecordInputTwo.setDisable(true);
        this.limit = 0;

        //call our callback function to set the datepicker to disable any used dates.
        Callback<DatePicker, DateCell> dayCellFactory  = this.disableUsedDates();
        this.addRecordDatePicker.setDayCellFactory(dayCellFactory);

        this.valueColumn.setText("Systolic");
        this.valueColumnTwo.setText("Diastolic");

        this.recordTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        RecordWithAreaChartController.instance = this;


        //disable the date picker text input
        this.addRecordDatePicker.getEditor().setDisable(true);
    }

    public void updateModels(){
        //Get our records from the database but limit them via the current set limit.
        ObservableList<Record> records = Record.where("type",this.recordType).withCurrentUser().limit(this.limit).sort("date").updateCache().get();

        //create our blood pressure list
        ObservableList<BloodPressure> bloodPressures = FXCollections.observableArrayList();

        //cast all our records to blood pressure objects
        records.forEach((n)->{
            bloodPressures.add(new BloodPressure(n.getId(),n.getType(),n.getUserId(),n.getValue(),n.getDate().toString()));
        });

        //-------------------------------------------------------------\\
        //                    UPDATE CHART CONTENTS                    \\
        //-------------------------------------------------------------\\
        //
        //This section of the method takes our records and updates the
        // chart model contents.
        XYChart.Series<String, Integer> series1 = new XYChart.Series<>();
        XYChart.Series<String, Integer> series2 = new XYChart.Series<>();

        bloodPressures.forEach((n)->{
            series1.getData().add(new XYChart.Data<>(n.getDate().toString(),n.getSystolic()));
            series2.getData().add(new XYChart.Data<>(n.getDate().toString(),n.getDiastolic()));
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

    public void showRecord(){

    }

    public void updateRecordSelection(){
        this.viewRecordButton.setDisable(this.recordTable.getSelectionModel().getSelectedItem() == null);
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


    public void setRecordType(String recordType){
        this.recordType = recordType;
        this.recordName.setText(recordType);
        MyHealth.getInstance().setSelectedRecordType(recordType);

        this.dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        this.valueColumn.setCellValueFactory(new PropertyValueFactory<>("systolic"));
        this.valueColumnTwo.setCellValueFactory(new PropertyValueFactory<>("diastolic"));
        this.updateModels();

    }


    private Callback<DatePicker, DateCell> disableUsedDates() {

        final Callback<DatePicker, DateCell> dayCellFactory = (final DatePicker datePicker) -> new DateCell() {

            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                for (Record record: Record.where("type", MyHealth.getInstance().getSelectedRecordType()).fromCache().get()){
                    if(item.equals(record.getDate())){
                        setDisable(true);
                        setStyle("-fx-background-color: -fx-error;");

                    }
                }
            }
        };

        return dayCellFactory;
    }
}
