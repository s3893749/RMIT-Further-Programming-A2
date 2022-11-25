//**** PACKAGE ****\\
package com.jackgharris.cosc2288.a2.controllers.components;

//**** PACKAGE IMPORTS ****\\
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

//**** START SHOW HEALTH RECORD CONTROLLER CLASS ****\\
public class ShowHealthRecordController {

    //**** FXML IMPORTS ****\\
    //The follow class variables match the id's of the match fxml objects for this scene
    //this allows the class and its method to interact with them in an easy capacity.

    //Weight value field, is the input for the weight value
    @FXML
    private TextField weightValue;

    //Weight note field, is the input for the note/comment for the weight record
    @FXML
    private TextField weightNote;

    //Temperature value field, is the input for the temperature value
    @FXML
    private TextField temperatureValue;

    //Temperature note field, is the input for the note/comment for the temperature record
    @FXML
    private TextField temperatureNote;

    //Blood pressure record input for our blood pressure value
    @FXML
    private TextField bloodPressureValue;

    //Blood pressure note field, is the input for the note/comment for the blood pressure record
    @FXML
    private TextField bloodPressureNote;

    //Password input, used to confirm the deletion of a record
    @FXML
    private PasswordField passwordInput;

    //Delete button, is enabled only after a valid password has been entered for this user
    @FXML
    private Button deleteButton;

    //Save button, saves any changes to the database, enabled once validation passes
    @FXML
    private Button saveButton;

    //Date label, shows the current date for this health records, note: dates can only
    //be changed for each record that makes up the health record not for all at once
    @FXML
    private Label dateLabel;

    //**** CLASS VARIABLES ****\\

    //Records, these are the records that make up the health record
    private Record temperature, weight, bloodPressure;

    //Comments, these are the notes/comments that accompany each record
    private Comment temperatureComment, weightComment, bloodPressureComment;

    //**** INITIALIZE METHOD ****\\
    //This method is called by JavaFX when the loaded FXML is initialized and can act as a constructor
    public void initialize(){
        //disable our save and delete buttons by default.
        this.saveButton.setDisable(true);
        this.deleteButton.setDisable(true);
    }

    //**** UPDATE SAVE BUTTON METHOD ****\\
    //This method will update the status of our saved button based on the validation of all the input fields.
    public void updateSaveButton(){

        //Validate our temperature, weight and blood pressure input using the helper method "validateValue"
        boolean validTemperature = this.validateValue(this.temperatureValue,"isFloat");

        boolean validWeight = this.validateValue(this.weightValue,"isFloat");

        boolean validBloodPressure = this.validateValue(this.bloodPressureValue,"isBloodPressure");

        //Validate our notes for temperature, weight and blood pressure using the helper method "validateNote"
        boolean validTemperatureNote = this.validateNote(this.temperatureNote);

        boolean validWeightNote = this.validateNote(this.weightNote);

        boolean validBloodPressureNote = this.validateNote(this.bloodPressureNote);

        //Finally check all the validation outcomes to ensure they are true and if so enable the button
        if(validWeight & validTemperature & validBloodPressure & validTemperatureNote & validWeightNote & validBloodPressureNote){
            this.saveButton.setDisable(false);
        }else{
            //else leave it disabled
            this.saveButton.setDisable(true);
        }

    }

    //**** VALIDATE VALUE METHOD ****\\
    //This method takes in a put and a type and will use the validation methods to ensure
    //that the input meets that requirement, else it will return false and append the text
    //input error styles.
    public boolean validateValue(TextField input, String type){

        //declare out outcome;
        boolean outcome = false;

        //switch between our types of validation
        switch (type){
            case "isFloat" -> {

                //check if it is not a float and add the error
                if(!Validation.isFloat(input.getText())){
                    input.getStyleClass().add("text-field-error");
                    input.getStyleClass().removeAll("text-field-success");
                    //set the outcome to false
                    outcome = false;
                }else{
                    //else it is a float and we append the success class
                    input.getStyleClass().removeAll("text-field-error");
                    input.getStyleClass().add("text-field-success");
                    //and set the outcome to true
                    outcome = true;
                }
            }

            case "isBloodPressure" ->{

                //check it is not a valid blood pressure input
                if(!Validation.isBloodPressure(input.getText())){
                    //append the error class
                    input.getStyleClass().add("text-field-error");
                    input.getStyleClass().removeAll("text-field-success");
                    //set the outcome to false
                    outcome = false;
                }else{
                    //else its valid and we append the success class
                    input.getStyleClass().removeAll("text-field-error");
                    input.getStyleClass().add("text-field-success");
                    //and set the outcome to true
                    outcome = true;
                }

            }
        }

        //finally return the outcome result
        return outcome;
    }

    //**** VALIDATE NOTE METHOD ****\\
    //This helper method will accept a note text-field and validate it to ensure
    //it meets the assignment specifications
    private boolean validateNote(TextField note){

        //declare our outcome
        boolean outcome = false;

        //check the length
        if(note.getText().length() > 50){

            //if too long set the outcome to false and append our error style class
            note.getStyleClass().add("text-field-error");
            note.getStyleClass().removeAll("text-field-success");
            outcome = false;

        }else{

            //else if it's not too long that's good! but we just check for blank as blank is
            //allowed
            if(!note.getText().isBlank()){

                //if it's not blank we append the success class
                note.getStyleClass().removeAll("text-field-error");
                note.getStyleClass().add("text-field-success");
            }else{
                //else if blank we return true with no class appended.
                note.getStyleClass().removeAll("text-field-success");
            }

            outcome = true;
        }

        //finally return the outcome
        return outcome;
    }

    //**** UPDATE DELETE BUTTON METHOD ****\\
    //This method will be called on keypress in the password field and will update our delete buttons tatus
    public void updateDeleteButton(){

        //first we check for a password fail
        if(!MyHealth.getInstance().getUser().getPassword().equals(User.hash(this.passwordInput.getText()))){
            //append the error class and remove the success class
            this.passwordInput.getStyleClass().add("text-field-error");
            this.passwordInput.getStyleClass().removeAll("text-field-success");
            //set the disabled to true
            this.deleteButton.setDisable(true);
        }else{
            //else if the password matches then remove the error
            this.passwordInput.getStyleClass().removeAll("text-field-error");
            //set the class to success
            this.passwordInput.getStyleClass().add("text-field-success");
            //enable the button
            this.deleteButton.setDisable(false);
        }
    }

    //**** DELETE BUTTON METHOD ****\\
    //This method will check the password is correct and then if so delete the records from
    //the database.
    public void delete(){

        //Before we delete we ask one more time via a conformation alert
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        //set the title
        alert.setTitle("Delete Record?");
        //set the context text description
        alert.setContentText("Are you sure you want to delete this record?");
        //get our dialog pane
        DialogPane dialogPane = alert.getDialogPane();
        //insert out css file to that pane
        dialogPane.getStylesheets().add(Objects.requireNonNull(MyHealth.class.getResource("css/app.css")).toExternalForm());
        //add the css class of parent
        dialogPane.getStyleClass().add("parent");
        //get the stage
        Stage stage = (Stage) dialogPane.getScene().getWindow();
        //add the warning icon
        stage.getIcons().add(Resource.faviconWarning());
        //load the warning image into an image view
        ImageView imageView = new ImageView(Resource.faviconWarning());
        //set the fit width and high
        imageView.setFitWidth(64);
        imageView.setFitHeight(64);
        //set the graphite to that image
        alert.setGraphic(imageView);
        //set our custom theme styles to that parent dialog pane
        dialogPane.setStyle(MyHealth.getInstance().getUser().getTheme().getStyle());
        //declare our two button types, yes and no
        ButtonType okButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
        //set the buttons
        alert.getButtonTypes().setAll(okButton, noButton);

        //add our oppress event for each button
        alert.showAndWait().ifPresent(type -> {

            //if the ok button is pressed then delete the records
            if (type == okButton) {

                Record.delete(this.bloodPressure);
                Record.delete(this.weight);
                Record.delete(this.temperature);

                //if there are comments for the records delete them too!
                if(this.bloodPressureComment != null){
                    Comment.delete(this.bloodPressureComment);
                }
                if(this.weightComment != null){
                    Comment.delete(this.weightComment);
                }
                if(this.temperatureComment != null){
                    Comment.delete(this.temperatureComment);
                }

                //finally update the models on the main page
                HealthRecordController.instance.updateModels();
                //set the message back confirming the deletion
                HealthRecordController.instance.setMessage("Health Record Deleted","alert-danger");
                //close the show health record stage and by extension the alert
                MyHealth.getStageById("showHealthRecord").close();
            }
        });

    }

    //**** RESET METHOD ****\\
    //This method will simply reset all the values, inputs and buttons back to default
    public void reset(){

        //set the values back to the starting values
        this.weightValue.setText(this.weight.getValue());
        this.temperatureValue.setText(this.temperature.getValue());
        this.bloodPressureValue.setText(this.bloodPressure.getValue());

        //remove the password and set it back to blank
        this.passwordInput.setText("");
        //remove the password input styles
        this.passwordInput.getStyleClass().removeAll("text-field-error");
        this.passwordInput.getStyleClass().removeAll("text-field-success");
        //disable the delete button
        this.deleteButton.setDisable(true);

        //update the models
        this.updateSaveButton();
    }

    //**** CANCEL METHOD ****\\
    //This method simply allows the user to cancel the edit request and closes the current stage
    public void cancel(){
        MyHealth.getStageById("showHealthRecord").close();
    }

    //**** SAVE METHOD ****\\
    //The save method will go ahead and save all the changed values back to the database
    public void save(){

        //set the weight value and save
        this.weight.setValue(this.weightValue.getText());
        this.weight.updateDetails();

        //set the temperature value and save
        this.temperature.setValue(this.temperatureValue.getText());
        this.temperature.updateDetails();

        //set the blood pressure value and save
        this.bloodPressure.setValue(this.bloodPressureValue.getText());
        this.bloodPressure.updateDetails();

        //next check if we have any comments to save, if so save them
        if(!this.temperatureComment.getNote().equals(this.temperatureNote.getText()) && this.temperatureNote.getText().length() < 50){
            Comment.add(new Comment(0,"Comment",MyHealth.getInstance().getUser().getId(),this.temperature.getId()+"/"+this.temperatureNote.getText(),this.temperature.getDate().toString()));
        }

        if(!this.weightComment.getNote().equals(this.weightNote.getText()) && this.weightNote.getText().length() < 50){
            Comment.add(new Comment(0,"Comment",MyHealth.getInstance().getUser().getId(),this.weight.getId()+"/"+this.weightNote.getText(),weight.getDate().toString()));
        }

        if(!this.bloodPressureComment.getNote().equals(this.bloodPressureNote.getText()) && this.bloodPressureNote.getText().length() < 50){
            Comment.add(new Comment(0,"Comment",MyHealth.getInstance().getUser().getId(),this.bloodPressure.getId()+"/"+this.bloodPressureNote.getText(),this.bloodPressure.getDate().toString()));
        }

        //finally we update the models on our main view
        HealthRecordController.instance.updateModels();
        //set our success message
        HealthRecordController.instance.setMessage("Updated Health Record!","alert-success");
        //and close this stage
        MyHealth.getStageById("showHealthRecord").close();
    }

    //**** SET RECORDS ****\\
    //This method will accept a health record object and set the records based on that
    public void setRecords(HealthRecord healthRecord){

        //set the temperature, weight and blood pressure records based on the
        this.temperature = healthRecord.getTemperatureRecord();
        this.weight = healthRecord.getWeightRecord();
        this.bloodPressure = healthRecord.getBloodPressureRecord();

        //set our values for the input fields based on these records
        this.temperatureValue.setText(temperature.getValue());
        this.weightValue.setText(weight.getValue());
        this.bloodPressureValue.setText(bloodPressure.getValue());

        //Load all our comments
        this.weightComment = this.loadComment(this.weightComment, this.weightNote,"Weight", this.weight.getId(),MyHealth.getInstance().getUser().getId(),this.weight.getDate().toString());
        this.temperatureComment = this.loadComment(this.temperatureComment, this.temperatureNote,"Temperature" ,this.temperature.getId(),MyHealth.getInstance().getUser().getId(), this.temperature.getDate().toString());
        this.bloodPressureComment = this.loadComment(this.bloodPressureComment, this.bloodPressureNote,"BloodPressure",this.bloodPressure.getId(),MyHealth.getInstance().getUser().getId(), this.bloodPressure.getDate().toString());

        //set the date label for this health record date
        this.dateLabel.setText(this.dateLabel.getText()+" "+healthRecord.getDate());

        //update the status of the save button
        this.updateSaveButton();
    }

    //**** LOAD COMMENT METHOD ****\\
    //This method will take in a number of parameters and return a comment object, either valid or blank to the caller.
    private Comment loadComment(Comment comment,TextField commentField ,String type, int record_id, int user_id,String date){

        //attempt to load a preexisting comment from the database
        ObservableList<Comment> comments = Comment.get(user_id,record_id);
        if(comments.size() != 0){
            //if one is found set the comment field text to its value
            commentField.setText(comments.get(0).getNote());
            //set the comment to that comment object
            comment = comments.get(0);
        }else{
            //else make a blank comment new comment and return that
            comment = new Comment(-1,type, user_id,record_id+"/",date);
        }
        return comment;
    }
}


