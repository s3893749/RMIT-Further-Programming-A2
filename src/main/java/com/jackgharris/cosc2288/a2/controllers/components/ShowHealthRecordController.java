package com.jackgharris.cosc2288.a2.controllers.components;

import com.jackgharris.cosc2288.a2.core.MyHealth;
import com.jackgharris.cosc2288.a2.models.Comment;
import com.jackgharris.cosc2288.a2.models.HealthRecord;
import com.jackgharris.cosc2288.a2.models.Record;
import com.jackgharris.cosc2288.a2.models.User;
import com.jackgharris.cosc2288.a2.utility.Resource;
import com.jackgharris.cosc2288.a2.utility.Validation;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.util.Objects;

public class ShowHealthRecordController {


    @FXML
    private TextField weightValue;

    @FXML
    private TextField weightNote;

    @FXML
    private TextField temperatureValue;

    @FXML
    private TextField temperatureNote;

    @FXML
    private TextField bloodPressureValue;

    @FXML
    private TextField bloodPressureNote;

    @FXML
    private PasswordField passwordInput;

    @FXML
    private Button deleteButton;

    @FXML
    private Button resetButton;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Label dateLabel;

    private Record temperature;
    private Record weight;
    private Record bloodPressure;

    private Comment temperatureComment;
    private Comment weightComment;
    private Comment bloodPressureComment;

    public void initialize(){
        this.saveButton.setDisable(true);
        this.deleteButton.setDisable(true);
    }

    public void updateSaveButton(){


        boolean validTemperature;

        if(!Validation.isFloat(this.temperatureValue.getText())){
            this.temperatureValue.getStyleClass().add("text-field-error");
            this.temperatureValue.getStyleClass().removeAll("text-field-success");
            validTemperature = false;
        }else{
            this.temperatureValue.getStyleClass().removeAll("text-field-error");
            this.temperatureValue.getStyleClass().add("text-field-success");
            validTemperature = true;
        }

        boolean validWeight;

        if(!Validation.isFloat(this.weightValue.getText())){
            this.weightValue.getStyleClass().add("text-field-error");
            this.weightValue.getStyleClass().removeAll("text-field-success");
            validWeight = false;
        }else{
            this.weightValue.getStyleClass().removeAll("text-field-error");
            this.weightValue.getStyleClass().add("text-field-success");
            validWeight = true;
        }

        boolean validBloodPressure;

        if(!Validation.isBloodPressure(this.bloodPressureValue.getText())){
            this.bloodPressureValue.getStyleClass().add("text-field-error");
            this.bloodPressureValue.getStyleClass().removeAll("text-field-success");
            validBloodPressure = false;
        }else{
            this.bloodPressureValue.getStyleClass().removeAll("text-field-error");
            this.bloodPressureValue.getStyleClass().add("text-field-success");
            validBloodPressure = true;
        }

        boolean validTemperatureNote;

        if(this.temperatureNote.getText().length() > 50){

            this.temperatureNote.getStyleClass().add("text-field-error");
            this.temperatureNote.getStyleClass().removeAll("text-field-success");
            validTemperatureNote = false;

        }else{

            if(!this.temperatureNote.getText().isBlank()){
                this.temperatureNote.getStyleClass().removeAll("text-field-error");
                this.temperatureNote.getStyleClass().add("text-field-success");
            }else{
                this.temperatureNote.getStyleClass().removeAll("text-field-success");
            }

            validTemperatureNote = true;
        }

        boolean validWeightNote;

        if(this.weightNote.getText().length() > 50){

            this.weightNote.getStyleClass().add("text-field-error");
            this.weightNote.getStyleClass().removeAll("text-field-success");
            validWeightNote = false;

        }else{

            if(!this.weightNote.getText().isBlank()){
                this.weightNote.getStyleClass().removeAll("text-field-error");
                this.weightNote.getStyleClass().add("text-field-success");
            }else{
                this.weightNote.getStyleClass().removeAll("text-field-success");
            }

            validWeightNote = true;
        }

        boolean validBloodPressureNote;

        if(this.bloodPressureNote.getText().length() > 50){

            this.bloodPressureNote.getStyleClass().add("text-field-error");
            this.bloodPressureNote.getStyleClass().removeAll("text-field-success");
            validBloodPressureNote = false;

        }else{

            if(!this.bloodPressureNote.getText().isBlank()){
                this.bloodPressureNote.getStyleClass().removeAll("text-field-error");
                this.bloodPressureNote.getStyleClass().add("text-field-success");
            }else{
                this.bloodPressureNote.getStyleClass().removeAll("text-field-success");
            }

            validBloodPressureNote = true;
        }

        if(validWeight & validTemperature & validBloodPressure & validTemperatureNote & validWeightNote & validBloodPressureNote){
            this.saveButton.setDisable(false);
        }else{
            this.saveButton.setDisable(true);
        }

    }

    public void updateDeleteButton(){
        if(!MyHealth.getInstance().getUser().getPassword().equals(User.hash(this.passwordInput.getText()))){
            this.passwordInput.getStyleClass().add("text-field-error");
            this.passwordInput.getStyleClass().removeAll("text-field-success");
            this.deleteButton.setDisable(true);
        }else{
            this.passwordInput.getStyleClass().removeAll("text-field-error");
            this.passwordInput.getStyleClass().add("text-field-success");
            this.deleteButton.setDisable(false);
        }
    }

    public void delete(){

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Record?");
        alert.setContentText("Are you sure you want to delete this record?");
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(Objects.requireNonNull(MyHealth.class.getResource("css/app.css")).toExternalForm());
        dialogPane.getStyleClass().add("parent");
        Stage stage = (Stage) dialogPane.getScene().getWindow();
        stage.getIcons().add(Resource.faviconWarning());
        ImageView imageView = new ImageView(Resource.faviconWarning());
        imageView.setFitWidth(64);
        imageView.setFitHeight(64);
        alert.setGraphic(imageView);
        dialogPane.setStyle(MyHealth.getInstance().getUser().getTheme().getStyle());
        ButtonType okButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
        alert.getButtonTypes().setAll(okButton, noButton);
        alert.showAndWait().ifPresent(type -> {

            if (type == okButton) {

                Record.delete(this.bloodPressure);
                Record.delete(this.weight);
                Record.delete(this.temperature);

                if(this.bloodPressureComment != null){
                    Comment.delete(this.bloodPressureComment);
                }
                if(this.weightComment != null){
                    Comment.delete(this.weightComment);
                }
                if(this.temperatureComment != null){
                    Comment.delete(this.temperatureComment);
                }
                HealthRecordController.instance.updateModels();
                HealthRecordController.instance.setMessage("Health Record Deleted","alert-danger");
                MyHealth.getStageById("showHealthRecord").close();
            }
        });



    }

    public void reset(){

        this.weightValue.setText(this.weight.getValue());
        this.temperatureValue.setText(this.temperature.getValue());
        this.bloodPressureValue.setText(this.bloodPressure.getValue());

        this.passwordInput.setText("");
        this.passwordInput.getStyleClass().removeAll("text-field-error");
        this.passwordInput.getStyleClass().removeAll("text-field-success");
        this.deleteButton.setDisable(true);


        this.updateSaveButton();
    }

    public void cancel(){
        MyHealth.getStageById("showHealthRecord").close();
    }

    public void save(){

        this.weight.setValue(this.weightValue.getText());
        this.weight.updateDetails();

        this.temperature.setValue(this.temperatureValue.getText());
        this.temperature.updateDetails();

        this.bloodPressure.setValue(this.bloodPressureValue.getText());
        this.bloodPressure.updateDetails();

        if(!this.temperatureComment.getNote().equals(this.temperatureNote.getText()) && this.temperatureNote.getText().length() < 50){
            Comment.add(new Comment(0,"Comment",MyHealth.getInstance().getUser().getId(),this.temperature.getId()+"/"+this.temperatureNote.getText(),this.temperature.getDate().toString()));
        }

        if(!this.weightComment.getNote().equals(this.weightNote.getText()) && this.weightNote.getText().length() < 50){
            Comment.add(new Comment(0,"Comment",MyHealth.getInstance().getUser().getId(),this.weight.getId()+"/"+this.weightNote.getText(),weight.getDate().toString()));
        }

        if(!this.bloodPressureComment.getNote().equals(this.bloodPressureNote.getText()) && this.bloodPressureNote.getText().length() < 50){
            Comment.add(new Comment(0,"Comment",MyHealth.getInstance().getUser().getId(),this.bloodPressure.getId()+"/"+this.bloodPressureNote.getText(),this.bloodPressure.getDate().toString()));
        }

        HealthRecordController.instance.updateModels();
        HealthRecordController.instance.setMessage("Updated Health Record!","alert-success");
        MyHealth.getStageById("showHealthRecord").close();

    }



    public void setRecords(HealthRecord healthRecord){
        this.temperature = healthRecord.getTemperatureRecord();
        this.weight = healthRecord.getWeightRecord();
        this.bloodPressure = healthRecord.getBloodPressureRecord();

        this.temperatureValue.setText(temperature.getValue());
        this.weightValue.setText(weight.getValue());
        this.bloodPressureValue.setText(bloodPressure.getValue());

        ObservableList<Comment> weightComment = Comment.get(weight.getUserId(),weight.getId());
        if(weightComment.size() != 0){
            this.weightNote.setText(weightComment.get(0).getNote());
            this.weightComment = weightComment.get(0);
        }else{
            this.weightComment = new Comment(-1,"Weight",weight.getUserId(),this.weight.getId()+"/",weight.getDate().toString());
        }

        ObservableList<Comment> temperatureComment = Comment.get(temperature.getUserId(), temperature.getId());
        if(temperatureComment.size() != 0){
            this.temperatureNote.setText(temperatureComment.get(0).getNote());
            this.temperatureComment = temperatureComment.get(0);
        }else{
            this.temperatureComment = new Comment(-1,"Temperature",temperature.getUserId(),this.temperature.getId()+"/",temperature.getDate().toString());
        }

        ObservableList<Comment> bloodPressureComment = Comment.get(bloodPressure.getUserId(), bloodPressure.getId());
        if(bloodPressureComment.size() !=0){
            this.bloodPressureNote.setText(bloodPressureComment.get(0).getNote());
            this.bloodPressureComment = bloodPressureComment.get(0);
        }else{
            this.bloodPressureComment = new Comment(-1,"BloodPressure",bloodPressure.getUserId(),this.bloodPressure.getId()+"/",bloodPressure.getDate().toString());
        }

        this.dateLabel.setText(this.dateLabel.getText()+" "+healthRecord.getDate());
        this.updateSaveButton();
    }
}


