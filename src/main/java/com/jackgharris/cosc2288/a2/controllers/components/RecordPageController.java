package com.jackgharris.cosc2288.a2.controllers.components;

import com.jackgharris.cosc2288.a2.core.MyHealth;
import com.jackgharris.cosc2288.a2.models.Record;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.io.IOException;
import java.time.LocalDate;

public abstract class RecordPageController{

    @FXML
    protected Label recordName;

    @FXML
    protected TableView<Record> recordTable;

    @FXML
    protected TableColumn<Record, String> dateColumn;

    @FXML
    protected TableColumn<Record, String> valueColumn;

    @FXML
    protected DatePicker addRecordDatePicker;

    @FXML
    protected Button viewRecordButton;

    protected int limit;

    protected String recordType;

    private static RecordPageController instance;


    public abstract void construct();

    public abstract void updateModels();

    public abstract void addRecord();

    public abstract void showRecord() throws IOException;

    protected void setInstance(RecordPageController instance){
        RecordPageController.instance = instance;
    }

    public void setRecordType(String recordType){

        this.recordType = recordType;
        this.recordName.setText(recordType);
        MyHealth.getInstance().setSelectedRecordType(this.recordType);
        this.limit =0;

    }

    public String getRecordType(){
        return this.recordType;
    }

    protected Callback<DatePicker, DateCell> disableUsedDates() {

        final Callback<DatePicker, DateCell> dayCellFactory = (final DatePicker datePicker) -> new DateCell() {

            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                for (Record record: Record.where("type",RecordPageController.getInstance().getRecordType()).fromCache().get()){
                    if(item.equals(record.getDate())){
                        setDisable(true);
                        setStyle("-fx-background-color: -fx-error;");

                    }
                }
            }
        };

        return dayCellFactory;
    }


    public static RecordPageController getInstance(){
        return RecordPageController.instance;
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

    public void updateRecordSelection(){
        this.viewRecordButton.setDisable(this.recordTable.getSelectionModel().getSelectedItem() == null);
    }



}
