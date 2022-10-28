package com.jackgharris.cosc2288.a2.controllers;

import com.jackgharris.cosc2288.a2.core.MyHealth;
import com.jackgharris.cosc2288.a2.models.Record;
import com.jackgharris.cosc2288.a2.models.Weight;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.util.List;

public class deleteRecordPopupController {

    @FXML
    private Label recordType;
    @FXML
    private Label recordDate;
    @FXML
    private Label recordValue;
    
    Record record;

    @FXML
    private AnchorPane parent;

    @FXML
    public void initialize(){
        this.parent.setStyle("-fx-background-color: "+ MyHealth.getTheme().getBackgroundColor());
        this.record = (Record) MyHealth.data.get("record");
        this.recordType.setText("Type: "+this.record.getClass().getSimpleName());
        this.recordDate.setText("Date: "+this.record.getDate());
        this.recordValue.setText("Value: "+this.record.getValue());
    }

    public void cancelButtonPress(){
        MyHealth.getStageById("deleteRecordPopup").close();
    }

    public void deleteButtonPress(){
        Weight.remove((Weight) this.record);
        MyHealth.getStageById("deleteRecordPopup").close();
    }
}
