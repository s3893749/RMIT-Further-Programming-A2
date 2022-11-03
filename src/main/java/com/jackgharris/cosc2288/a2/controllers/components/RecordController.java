package com.jackgharris.cosc2288.a2.controllers.components;

import com.jackgharris.cosc2288.a2.core.MyHealth;
import com.jackgharris.cosc2288.a2.models.Record;
import com.jackgharris.cosc2288.a2.models.Weight;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.time.LocalDate;

public class RecordController {

    @FXML
    private Label recordName;

    @FXML
    private TableView<Record> recordTable;

    @FXML
    private TableColumn<Weight, String> dateColumn;

    @FXML
    private TableColumn<Weight, String> valueColumn;

    @FXML
    private DatePicker addRecordDatePicker;

    @FXML
    private TextField addRecordInput;

    @FXML
    private Button addRecordButton;

    private String recordType;

    public void initialize(){

        //set our selection buttons and input to be disabled by default
        //this.addRecordButton.setDisable(true);
        //this.addRecordInput.setDisable(true);

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
        this.recordTable.setItems(Record.where("user_id", String.valueOf(MyHealth.getInstance().getUser().getId())).where("type",recordType).get());
    }

    public void datePickerUpdated(){

    }

    public void valueInputUpdated(){
        System.out.println("pressed!");
    }

    public void addRecord(){

        Record.add(new Record(0,this.recordType,MyHealth.getInstance().getUser().getId(),this.addRecordInput.getText(), this.addRecordDatePicker.getValue().toString()));

        this.updateTable();

    }

    private void updateTable(){
        this.recordTable.getItems().clear();
        this.recordTable.getItems().addAll(Record.where("user_id",String.valueOf(MyHealth.getInstance().getUser().getId())).where("type",this.recordType).get());
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
