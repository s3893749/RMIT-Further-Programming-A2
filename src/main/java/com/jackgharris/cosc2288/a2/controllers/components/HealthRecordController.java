package com.jackgharris.cosc2288.a2.controllers.components;

import com.jackgharris.cosc2288.a2.core.MyHealth;
import com.jackgharris.cosc2288.a2.models.HealthRecord;
import com.jackgharris.cosc2288.a2.models.Record;
import com.jackgharris.cosc2288.a2.utility.Validation;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.time.LocalDate;

public class HealthRecordController {

    @FXML
    private TableView<Record> recordTable;

    @FXML
    private TableColumn<Record,String> dateColumn;

    @FXML
    private TableColumn<Record,String> temperatureColumn;

    @FXML
    private TableColumn<Record,String> weightColumn;

    @FXML
    private TableColumn<Record,String> bloodPressureColumn;

    @FXML
    private TextField temperatureValueInput;

    @FXML
    private TextField temperatureNote;

    @FXML
    private TextField weightValueInput;

    @FXML
    private TextField weightNote;

    @FXML
    private TextField bloodPressureValueInput;

    @FXML
    private TextField bloodPressureNote;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Button viewRecordButton;

    @FXML
    private Button saveButton;

    @FXML
    private Label subheading;



    public void initialize(){
        Callback<DatePicker, DateCell> dayCellFactory  = this.disableUsedDates();
        this.datePicker.setDayCellFactory(dayCellFactory);

        this.datePicker.getEditor().setDisable(true);

        this.recordTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        this.dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        this.temperatureColumn.setCellValueFactory(new PropertyValueFactory<>("temperature"));
        this.weightColumn.setCellValueFactory(new PropertyValueFactory<>("weight"));
        this.bloodPressureColumn.setCellValueFactory(new PropertyValueFactory<>("bloodPressure"));

        this.viewRecordButton.setDisable(true);

        this.saveButton.setDisable(true);

        this.temperatureNoteChanged();
        this.weightNoteChanged();
        this.bloodPressureNoteChanged();

        this.updateModels();
    }

    public void updateModels(){
        this.recordTable.getItems().clear();
        this.recordTable.getItems().addAll(HealthRecord.getAllForCurrentUser(true));
    }

    public void addHealthRecord(){
        int user_id = MyHealth.getInstance().getUser().getId();

        int added = 0;

        if(!this.temperatureValueInput.isDisable()){
            Record.add(new Record(0,"Temperature",user_id,this.temperatureValueInput.getText(),this.datePicker.getValue().toString()));
            added++;
        }

        if(!this.weightValueInput.isDisable()){
            Record.add(new Record(0,"Weight",user_id,this.weightValueInput.getText(),this.datePicker.getValue().toString()));
            added++;
        }

        if(!this.bloodPressureValueInput.isDisable()){
            Record.add(new Record(0,"BloodPressure",user_id,this.bloodPressureValueInput.getText(),this.datePicker.getValue().toString()));
            added++;
        }

        if(added > 0){
            this.subheading.setText("Add New Health record -> New Health Record Added for date '"+this.datePicker.getValue().toString()+"'");
            this.subheading.setStyle("-fx-text-fill: -fx-success");
        }

        this.updateModels();
        this.discard();
        this.saveButton.setDisable(true);
    }

    public void dateChanged(){

        if(this.datePicker.getValue() == null){
            this.updateSaveButton();
            return;
        }

        ObservableList<Record> temperature = Record.where("type","Temperature").withCurrentUser().where("date",this.datePicker.getValue().toString()).get();

        if(temperature.size() != 0){
            this.temperatureValueInput.setText(temperature.get(0).getValue());
            this.temperatureValueInput.setDisable(true);

        }else{
            this.temperatureValueInput.setDisable(false);
        }

        ObservableList<Record> weight = Record.where("type","Weight").withCurrentUser().where("date",this.datePicker.getValue().toString()).get();


        if(weight.size() != 0){
            this.weightValueInput.setText(weight.get(0).getValue());
            this.weightValueInput.setDisable(true);
        }else{
            this.weightValueInput.setDisable(false);
        }

        this.updateSaveButton();
    }

    public void discard(){
        this.temperatureValueInput.setText(null);
        this.temperatureNote.setText("");
        this.temperatureValueInput.setDisable(false);

        this.weightValueInput.setText(null);
        this.weightNote.setText("");
        this.weightValueInput.setDisable(false);

        this.bloodPressureValueInput.setText(null);
        this.bloodPressureNote.setText("");
        this.bloodPressureValueInput.setDisable(false);

        this.temperatureValueInput.getStyleClass().removeAll("text-field-error");
        this.temperatureValueInput.getStyleClass().removeAll("text-field-success");

        this.temperatureNote.getStyleClass().removeAll("text-field-error");
        this.temperatureNote.getStyleClass().removeAll("text-field-success");

        this.weightValueInput.getStyleClass().removeAll("text-field-error");
        this.weightValueInput.getStyleClass().removeAll("text-field-success");

        this.weightNote.getStyleClass().removeAll("text-field-error");
        this.weightNote.getStyleClass().removeAll("text-field-success");

        this.bloodPressureValueInput.getStyleClass().removeAll("text-field-error");
        this.bloodPressureValueInput.getStyleClass().removeAll("text-field-success");
        this.bloodPressureNote.getStyleClass().removeAll("text-field-error");
        this.bloodPressureNote.getStyleClass().removeAll("text-field-success");

        this.datePicker.setValue(null);
    }

    public void showRecord(){
    }

    public void updateRecordSelection(){
        this.viewRecordButton.setDisable(this.recordTable.getSelectionModel().getSelectedItem() == null);
    }

    public boolean temperatureValueChanged(){
        boolean outcome;

        if(!Validation.isFloat(this.temperatureValueInput.getText())){
            this.temperatureValueInput.getStyleClass().add("text-field-error");
            this.temperatureValueInput.getStyleClass().removeAll("text-field-success");
            outcome = false;
        }else{
            this.temperatureValueInput.getStyleClass().removeAll("text-field-error");
            this.temperatureValueInput.getStyleClass().add("text-field-success");
            outcome = true;
        }

        return outcome;
    }

    public boolean temperatureNoteChanged(){

        boolean outcome;

        if(this.temperatureNote.getText().length() > 50){

            this.temperatureNote.getStyleClass().add("text-field-error");
            this.temperatureNote.getStyleClass().removeAll("text-field-success");
            outcome = false;

        }else{

            if(!this.temperatureNote.getText().isBlank()){
                this.temperatureNote.getStyleClass().removeAll("text-field-error");
                this.temperatureNote.getStyleClass().add("text-field-success");
            }else{
                this.temperatureNote.getStyleClass().removeAll("text-field-success");
            }

            outcome = true;

        }

        return outcome;

    }

    public boolean weightValueChanged(){
        boolean outcome;

        if(!Validation.isFloat(this.weightValueInput.getText())){
            this.weightValueInput.getStyleClass().add("text-field-error");
            this.weightValueInput.getStyleClass().removeAll("text-field-success");
            outcome = false;
        }else{
            this.weightValueInput.getStyleClass().removeAll("text-field-error");
            this.weightValueInput.getStyleClass().add("text-field-success");
            outcome = true;
        }

        return outcome;

    }

    public boolean weightNoteChanged(){

        boolean outcome;

        if(this.weightNote.getText().length() > 50){

            this.weightNote.getStyleClass().add("text-field-error");
            this.weightNote.getStyleClass().removeAll("text-field-success");
            outcome = false;

        }else{

            if(!this.weightNote.getText().isBlank()){
                this.weightNote.getStyleClass().removeAll("text-field-error");
                this.weightNote.getStyleClass().add("text-field-success");
            }else{
                this.weightNote.getStyleClass().removeAll("text-field-success");
            }

            outcome = true;

        }

        return outcome;

    }


    public boolean bloodPressureValueChanged(){
        boolean outcome;

        if(!Validation.isBloodPressure(this.bloodPressureValueInput.getText())){
            this.bloodPressureValueInput.getStyleClass().add("text-field-error");
            this.bloodPressureValueInput.getStyleClass().removeAll("text-field-success");
            outcome = false;
        }else{
            this.bloodPressureValueInput.getStyleClass().removeAll("text-field-error");
            this.bloodPressureValueInput.getStyleClass().add("text-field-success");
            outcome = true;
        }

        return outcome;

    }

    public boolean bloodPressureNoteChanged(){

        boolean outcome;

        if(this.bloodPressureNote.getText().length() > 50){

            this.bloodPressureNote.getStyleClass().add("text-field-error");
            this.bloodPressureNote.getStyleClass().removeAll("text-field-success");
            outcome = false;

        }else{

            if(!this.bloodPressureNote.getText().isBlank()){
                this.bloodPressureNote.getStyleClass().removeAll("text-field-error");
                this.bloodPressureNote.getStyleClass().add("text-field-success");
            }else{
                this.bloodPressureNote.getStyleClass().removeAll("text-field-success");
            }

            outcome = true;

        }

        return outcome;


    }

    public void updateSaveButton(){


        this.temperatureNoteChanged();
        this.temperatureValueChanged();
        this.weightNoteChanged();
        this.weightValueChanged();
        this.bloodPressureValueChanged();
        this.bloodPressureNoteChanged();

        if(this.temperatureNoteChanged() && this.temperatureValueChanged() && this.weightNoteChanged() && this.weightValueChanged() && this.bloodPressureValueChanged() && this.bloodPressureNoteChanged() && datePicker.getValue() != null){
            this.saveButton.setDisable(false);
        }else{
            this.saveButton.setDisable(true);
        }
    }


    protected Callback<DatePicker, DateCell> disableUsedDates() {

        final Callback<DatePicker, DateCell> dayCellFactory = (final DatePicker datePicker) -> new DateCell() {

            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                for (HealthRecord record: HealthRecord.getAllForCurrentUser(false)){
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
