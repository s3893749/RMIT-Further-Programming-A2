package com.jackgharris.cosc2288.a2.controllers;

import com.jackgharris.cosc2288.a2.core.MyHealth;
import com.jackgharris.cosc2288.a2.interfaces.HasActiveRecord;
import com.jackgharris.cosc2288.a2.models.Record;
import com.jackgharris.cosc2288.a2.models.Weight;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.time.LocalDate;

public class EditRecordPopupController implements HasActiveRecord{

    private Record record;

    @FXML
    private Label recordType;

    @FXML
    private TextField recordEditValue;

    @FXML
    private DatePicker recordEditDate;

    @FXML
    private Button resetDateButton;

    @FXML
    private Button resetValueButton;


    private Callback<DatePicker, DateCell> disableUsedDates() {
        final Callback<DatePicker, DateCell> dayCellFactory = (final DatePicker datePicker) -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);

                for (Record record: Weight.getAll()){
                    if(item.equals(record.getDate())){
                        setDisable(true);
                        setStyle("-fx-background-color: #c72929;");
                        setCursor(Cursor.CROSSHAIR);
                    }
                }
            }
        };
        return dayCellFactory;
    }

    @FXML
    public void initialize(){
        Callback<DatePicker, DateCell> dayCellFactory  = this.disableUsedDates();
        this.recordEditDate.setDayCellFactory(dayCellFactory);
        this.resetDateButton.setDisable(true);
        this.recordEditDate.getEditor().setDisable(true);
        this.resetValueButton.setDisable(true);

    }

    @Override
    public void setRecord(Record record) {
        this.record = record;
        this.recordType.setText("Type:  "+record.getClass().getSimpleName());
        this.recordEditValue.setText(record.getValue().toString());
        this.recordEditDate.setValue(record.getDate());
    }

    @Override
    public Record getRecord() {
        return this.record;
    }

    public void resetEditDateButtonPress(){
        this.recordEditDate.setValue(this.record.getDate());
        this.resetDateButton.setDisable(true);
    }

    public void resetEditValueButtonPress(){
        this.recordEditValue.setText(this.record.getValue().toString());
        this.resetValueButton.setDisable(true);
    }

    public void dateChanged(){
        this.resetDateButton.setDisable(false);
    }

    public void valueChanged(){
        this.resetValueButton.setDisable(false);
    }

    public void cancelButtonPress(){
        MyHealth.getStageById("editRecordPopup").close();
    }

    public void saveButtonPress(){
        this.record.setDate(this.recordEditDate.getValue().toString());
        this.record.setValue(Float.parseFloat(this.recordEditValue.getText()));
        MyHealth.getStageById("editRecordPopup").close();
    }

}
