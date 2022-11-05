package com.jackgharris.cosc2288.a2.controllers.components.popups;

import com.jackgharris.cosc2288.a2.core.MyHealth;
import com.jackgharris.cosc2288.a2.interfaces.HasActiveRecord;
import com.jackgharris.cosc2288.a2.models.Record;
import com.jackgharris.cosc2288.a2.models.Weight;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class DeleteRecordPopupController implements HasActiveRecord {

    @FXML
    private Label recordType;
    @FXML
    private Label recordDate;
    @FXML
    private Label recordValue;
    
    private Record record;

    @FXML
    private AnchorPane parent;

    @FXML
    public void initialize(){
        this.parent.setStyle("-fx-background-color: "+ MyHealth.getInstance().getTheme().getBackgroundColor());
    }

    public void cancelButtonPress(){
        MyHealth.getStageById("deleteRecordPopup").close();
    }

    public void deleteButtonPress(){
        Weight.remove((Weight) this.record);
        MyHealth.getStageById("deleteRecordPopup").close();
    }

    @Override
    public void setRecord(Record record){
        this.record = record;
        this.recordType.setText("Type: "+this.record.getClass().getSimpleName());
        this.recordDate.setText("Date: "+this.record.getDate());
        this.recordValue.setText("Value: "+this.record.getValue());
    }

    @Override
    public Record getRecord() {
        return this.record;
    }
}
