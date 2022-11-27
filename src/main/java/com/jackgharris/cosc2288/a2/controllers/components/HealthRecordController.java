//**** PACKAGE ****\\
package com.jackgharris.cosc2288.a2.controllers.components;

//**** PACKAGE IMPORTS ****\\
import com.jackgharris.cosc2288.a2.core.MyHealth;
import com.jackgharris.cosc2288.a2.models.Comment;
import com.jackgharris.cosc2288.a2.models.HealthRecord;
import com.jackgharris.cosc2288.a2.models.Record;
import com.jackgharris.cosc2288.a2.utility.FXMLUtility;
import com.jackgharris.cosc2288.a2.utility.Resource;
import com.jackgharris.cosc2288.a2.utility.Time;
import com.jackgharris.cosc2288.a2.utility.Validation;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.time.LocalDate;


//**** START HEALTH RECORD CONTROLLER CLASS ****\\
public class HealthRecordController {


    //**** FXML IMPORTS ****\\
    //The follow class variables match the id's of the match fxml objects for this scene
    //this allows the class and its method to interact with them in an easy capacity.

    //The record table shows the list of health records
    @FXML
    private TableView<Record> recordTable;

    //Record table date column shows the date value
    @FXML
    private TableColumn<Record,String> dateColumn;

    //Record table temperature column shows the temperature
    @FXML
    private TableColumn<Record,String> temperatureColumn;

    //Record table weight column shows the weight.
    @FXML
    private TableColumn<Record,String> weightColumn;

    //Record table blood pressure column shows the blood pressure
    @FXML
    private TableColumn<Record,String> bloodPressureColumn;

    //The temperature value input is the input field that accepts a new input value for a temperature record
    @FXML
    private TextField temperatureValueInput;

    //The temperature not text input accepts a note to accompany the value, it is optional
    @FXML
    private TextField temperatureNote;

    //The weight value input accepts the value of a new weight record
    @FXML
    private TextField weightValueInput;

    //The weight note text field accepts a not to accompany the new weight record
    @FXML
    private TextField weightNote;

    //Blood pressure value input accepts a new blood pressure entry
    @FXML
    private TextField bloodPressureValueInput;

    //Blood pressure note is the text field that the user can enter a new note
    @FXML
    private TextField bloodPressureNote;

    //Date picker is the date picker the user uses to select the date for the new health record and its relevant sub records
    @FXML
    private DatePicker datePicker;

    //View record button opens a ShowHealthRecord window that allows the user to delete or edit the entire of a health record.
    @FXML
    private Button viewRecordButton;

    //Save button saves the new health record to the database
    @FXML
    private Button saveButton;

    //The subheading to used to show any changes or notifications to the user, example: health record deleted or health record saved!
    @FXML
    private Label subheading;


    //**** CLASS VARIABLES ****\\
    //The static variable instance is set on initialize and is used by the child windows to interface back with this controller.
    public static HealthRecordController instance;


    //**** INITIALIZE METHOD ****\\
    //This method is called when the fxml is loaded, in this context it is being used
    //as a constructor as we want to initialize these items on fxml load.
    public void initialize(){

        //Set our date picker day cell factory callback to disable all the used dated
        Callback<DatePicker, DateCell> dayCellFactory  = this.disableUsedDates();
        this.datePicker.setDayCellFactory(dayCellFactory);

        //Disable the date picker manual editor, this makes it selection only.
        this.datePicker.getEditor().setDisable(true);

        //Set the table to the constrained resize, this ensures all columns use the
        this.recordTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        //Set the cell factories to bind the data for each column
        this.dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        this.temperatureColumn.setCellValueFactory(new PropertyValueFactory<>("temperature"));
        this.weightColumn.setCellValueFactory(new PropertyValueFactory<>("weight"));
        this.bloodPressureColumn.setCellValueFactory(new PropertyValueFactory<>("bloodPressure"));

        //Disable the view record button by default, is enabled once one is selected.
        this.viewRecordButton.setDisable(true);

        //Disable the save button by default, is enabled once the entered values are validated
        this.saveButton.setDisable(true);

        //Update the models
        this.updateModels();

        //Set the static instance to this instance.
        HealthRecordController.instance = this;
    }


    //**** SET MESSAGE METHOD ****\\
    //This method accepts a message from its pop-up window and will set the subheading to that message and
    //set the css style, red for error or green for good!
    public void setMessage(String message, String css){
        //Set message
        this.subheading.setText(message);

        //Set Class -> "alert-success" or "alert-danger"
        this.subheading.getStyleClass().add(css);
    }


    //**** UPDATE MODELS METHOD ****\\
    //This method clears and updates the models in the record table, it should be called after a recrod
    //is changed, deleted or added.
    public void updateModels(){
        //clear the current records
        this.recordTable.getItems().clear();

        //Add all the records for this user and update its cache
        this.recordTable.getItems().addAll(HealthRecord.getAllForCurrentUser(true));
    }

    //**** ADD HEATH RECORD METHOD ****\\
    //This method will check to see if any records were found, and if not add them to the health record
    //We can also see that it will check to see if a comment is set and if so add it
    public void addHealthRecord(){

        //Get the current user id
        int user_id = MyHealth.getInstance().getUser().getId();

        //Integer to count how many were added
        int added = 0;

        //Check to see if the temperature has a record "isDisabled if record is present" if not then add it.
        if(!this.temperatureValueInput.isDisable()){
            Record.add(new Record(0,"Temperature",user_id,this.temperatureValueInput.getText(),this.datePicker.getValue().toString(), Time.now()));
            added++;
        }

        //Check to see if the temperateNote is not disabled and not blank.
        if(!this.temperatureNote.isDisable() && !this.temperatureNote.getText().isBlank()){
            Comment.add(new Comment(0,"Comment",MyHealth.getInstance().getUser().getId(),Record.lastAddedId+"/"+this.temperatureNote.getText(),this.datePicker.getValue().toString()));
        }

        //Check to see if we need to add a weight record, if so add it
        if(!this.weightValueInput.isDisable()){
            Record.add(new Record(0,"Weight",user_id,this.weightValueInput.getText(),this.datePicker.getValue().toString(), Time.now()));
            added++;
        }

        //Check to see if we need to add a weight note record then add it if so
        if(!this.weightNote.isDisable() && !this.weightNote.getText().isBlank()){
            Comment.add(new Comment(0,"Comment",MyHealth.getInstance().getUser().getId(),Record.lastAddedId+"/"+this.weightNote.getText(),this.datePicker.getValue().toString()));
        }

        //Check to see if we need to add a blood pressure value and add it if so
        if(!this.bloodPressureValueInput.isDisable()){
            Record.add(new Record(0,"BloodPressure",user_id,this.bloodPressureValueInput.getText(),this.datePicker.getValue().toString(), Time.now()));
            added++;
        }

        //Check to see if we need to add a blood pressure note, add it if so
        if(!this.bloodPressureNote.isDisable() && !this.bloodPressureNote.getText().isBlank()){
            Comment.add(new Comment(0,"Comment",MyHealth.getInstance().getUser().getId(),Record.lastAddedId+"/"+this.bloodPressureNote.getText(),this.datePicker.getValue().toString()));
        }


        //Finally we check to see how many were added, if more than 1 was added we set the subheading to the success color and message
        if(added > 0){
            this.subheading.setText("Add New Health record -> New Health Record Added for date '"+this.datePicker.getValue().toString()+"'");
            this.subheading.setStyle("-fx-text-fill: -fx-success");
        }

        //Update the models, call discard to reset the inputs and disable the saveButton ready for next use
        this.updateModels();
        this.discard();
        this.saveButton.setDisable(true);
    }

    //**** DATE CHANGED METHOD ****\\
    //This method is called when the date picker has its date changed, it will go and check if any records exist
    //and if so it will set them to that value and disable the inputs
    public void dateChanged(){

        //Check to ensure the value is not null, if not then return and update the save button
        if(this.datePicker.getValue() == null){
            this.updateSaveButton();

            //return to ensure the rest of the method does not run
            return;
        }

        // ** GET TEMPERATURE RECORD **\\
        //This code block will go ahead and get a temperature record for this date, if one is not valid then it will
        //make sure the inputs and not input is not disabled, else it will load them and disable the inputs
        ObservableList<Record> temperature = Record.where("type","Temperature").withCurrentUser().where("date",this.datePicker.getValue().toString()).get();

        //Check if our size is > 0, if it is then we have a result
        if(temperature.size() != 0){

            //Disable the input and set the value
            this.temperatureValueInput.setText(temperature.get(0).getValue());
            this.temperatureValueInput.setDisable(true);

            //Attempt to load our comment from the database
            ObservableList<Comment> temperatureComment = Comment.get(MyHealth.getInstance().getUser().getId(),temperature.get(0).getId());

            //Check if the comment is set, if so the size will be greater than 1
            if(temperatureComment.size() != 0){

                //If so then set the comment and disable the input
                this.temperatureNote.setText(temperatureComment.get(0).getNote());
                this.temperatureNote.setDisable(true);
            }else{
                //Else make sure the input is enabled and blank
                this.temperatureNote.setText("");
                this.temperatureNote.setDisable(false);
            }

        }else{
            //Finally as a precaution we make sure our values are enabled and blank if we do not have a record
            this.temperatureValueInput.setDisable(false);
            this.temperatureValueInput.setText("");
            this.temperatureNote.setText("");
            this.temperatureNote.setDisable(false);
        }


        // ** GET WEIGHT RECORD **\\
        //This code block will go ahead and get a weight record for this date, if one is not valid then it will
        //make sure the inputs and not input is not disabled, else it will load them and disable the inputs
        ObservableList<Record> weight = Record.where("type","Weight").withCurrentUser().where("date",this.datePicker.getValue().toString()).get();

        //Check if our size is > 0, if it is then we have a result
        if(weight.size() != 0){

            //Disable the input and set the value
            this.weightValueInput.setText(weight.get(0).getValue());
            this.weightValueInput.setDisable(true);

            //Attempt to load our comment from the database
            ObservableList<Comment> weightComment = Comment.get(MyHealth.getInstance().getUser().getId(),weight.get(0).getId());

            if(weightComment.size() != 0){
                //If so then set the comment and disable the input
                this.weightNote.setText(weightComment.get(0).getNote());
                this.weightNote.setDisable(true);
            }else{
                //Else make sure the input is enabled and blank
                this.weightNote.setText("");
                this.weightNote.setDisable(false);
            }

        }else{
            //Finally as a precaution we make sure our values are enabled and blank if we do not have a record
            this.weightValueInput.setDisable(false);
            this.weightValueInput.setText("");
            this.weightNote.setText("");
            this.weightNote.setDisable(false);
        }



        // ** GET BLOOD PRESSURE RECORD **\\
        //This code block will go ahead and get a blood pressure record for this date, if one is not valid then it will
        //make sure the inputs and not input is not disabled, else it will load them and disable the inputs
        ObservableList<Record> bloodPressure = Record.where("type","BloodPressure").withCurrentUser().where("date",this.datePicker.getValue().toString()).get();

        //Check if our size is > 0, if it is then we have a result
        if(bloodPressure.size() != 0){

            //Disable the input and set the value
            this.bloodPressureValueInput.setText(bloodPressure.get(0).getValue());
            this.bloodPressureValueInput.setDisable(true);

            //Attempt to load our comment from the database
            ObservableList<Comment> bloodPressureComment = Comment.get(MyHealth.getInstance().getUser().getId(),bloodPressure.get(0).getId());

            if(bloodPressureComment.size() != 0){
                //If so then set the comment and disable the input
                this.bloodPressureNote.setText(bloodPressureComment.get(0).getNote());
                this.bloodPressureNote.setDisable(true);
            }else{
                //Else make sure the input is enabled and blank
                this.bloodPressureNote.setText("");
                this.bloodPressureNote.setDisable(false);
            }

        }else{
            //Finally as a precaution we make sure our values are enabled and blank if we do not have a record
            this.bloodPressureValueInput.setDisable(false);
            this.bloodPressureValueInput.setText("");
            this.bloodPressureNote.setText("");
            this.bloodPressureNote.setDisable(false);

        }

        //Lastly at the end of the method we update our save buttons status, this will either enable or disable it
        //depending on what data was loaded
        this.updateSaveButton();
    }

    //**** DISCARD METHOD ****\\
    //This method will go ahead and discard any and all data that is currently in the input fields and reset
    //them back to a first load state.
    public void discard(){

        //Reset all our temperature & temperature note inputs
        this.temperatureValueInput.setText(null);
        this.temperatureNote.setText("");
        this.temperatureValueInput.setDisable(false);
        this.temperatureNote.setDisable(false);

        //Reset all our weight & weight note inputs
        this.weightValueInput.setText(null);
        this.weightNote.setText("");
        this.weightValueInput.setDisable(false);
        this.weightNote.setDisable(false);

        //Reset all our blood pressure & blood pressure note inputs
        this.bloodPressureValueInput.setText(null);
        this.bloodPressureNote.setText("");
        this.bloodPressureValueInput.setDisable(false);
        this.bloodPressureNote.setDisable(false);

        //Reset the temperature value and note text-field styles
        this.temperatureValueInput.getStyleClass().removeAll("text-field-error");
        this.temperatureValueInput.getStyleClass().removeAll("text-field-success");
        this.temperatureNote.getStyleClass().removeAll("text-field-error");
        this.temperatureNote.getStyleClass().removeAll("text-field-success");

        //Reset the weight and weight note text-feidl styles
        this.weightValueInput.getStyleClass().removeAll("text-field-error");
        this.weightValueInput.getStyleClass().removeAll("text-field-success");
        this.weightNote.getStyleClass().removeAll("text-field-error");
        this.weightNote.getStyleClass().removeAll("text-field-success");

        //Reset the blood pressure styles
        this.bloodPressureValueInput.getStyleClass().removeAll("text-field-error");
        this.bloodPressureValueInput.getStyleClass().removeAll("text-field-success");
        this.bloodPressureNote.getStyleClass().removeAll("text-field-error");
        this.bloodPressureNote.getStyleClass().removeAll("text-field-success");

        //Reset the date picker value to null
        this.datePicker.setValue(null);
    }


    //**** SHOW RECORD METHOD ****\\
    //This method will check if the show health record stage is already shown and
    //if not then create and show the stage
    public void showRecord() throws IOException {
        //Check to ensure it's not already shown
        if(!MyHealth.isStageShown("showHealthRecord")){

            //Create the new stage
            Stage stage = new Stage();
            //Set the stage id
            stage.getProperties().put("id","showHealthRecord");
            //disable resize
            stage.setResizable(false);
            //create the FXML loader
            FXMLLoader loader = new FXMLLoader(FXMLUtility.showHealthRecord);
            //load the scene
            Scene scene = new Scene(loader.load());
            //set the title
            stage.setTitle("MyHealth Record | Show/Edit/Delete");
            //set the icon
            stage.getIcons().add(Resource.favicon());
            //get our controller instance
            ShowHealthRecordController controller = loader.getController();
            //set the record
            controller.setRecords((HealthRecord) this.recordTable.getSelectionModel().getSelectedItem());
            //set the scene
            stage.setScene(scene);
            //finally show the stage
            stage.show();
        }else{
            //Else give the stage focus
            MyHealth.getStageById("showRecord").requestFocus();
        }
    }

    //**** UPDATE RECORD SELECTION METHOD ****\\
    //This method will be triggered when a row is selected in the table and if that row is not null it
    //will set the view record button to be enabled not disabled
    public void updateRecordSelection(){
        this.viewRecordButton.setDisable(this.recordTable.getSelectionModel().getSelectedItem() == null);
    }

    //**** TEMPERATURE VALUE CHANGED METHOD ****\\
    //This method will validate that the temperature input is a float and set an error or success class
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


    //**** WEIGHT VALUE CHANGED METHOD****\\
    //Validate the wight value and return true or false for success or fail
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

    //**** BLOOD PRESSURE VALUE CHANGED METHOD ****\\
    //Validate the blood pressure is in an acceptable format and apply styles before
    //returning true or false.
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

    //**** UPDATE SAVE BUTTON METHOD ****\\
    //This method will update the status of the save button, If all the validation
    //passes and is correct then the save button will be enabled, else it will stay
    //disabled.
    public void updateSaveButton(){

        //Recall our value changed methods
        this.temperatureValueChanged();
        this.weightValueChanged();
        this.bloodPressureValueChanged();

        //Check if the validation passes
        if(noteValidator(this.temperatureNote) && this.temperatureValueChanged() && noteValidator(this.weightNote) && this.weightValueChanged() && this.bloodPressureValueChanged() && noteValidator(this.bloodPressureNote) && this.datePicker.getValue() != null){

            //If so enable the save button
            this.saveButton.setDisable(false);
        }else{
            //Else disable the save button
            this.saveButton.setDisable(true);
        }
    }



    //**** DISABLE USED DATES METHOD ****\\
    //This method will be a callback for the date picker that will disable any used dates so that
    //the user cannot select them.
    protected Callback<DatePicker, DateCell> disableUsedDates() {

        final Callback<DatePicker, DateCell> dayCellFactory = (final DatePicker datePicker) -> new DateCell() {

            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);

                //For each of the health records for this user we check the date, and if used we setDisabled = true
                //and set the background color to our error;
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

    //**** NOTE VALIDATOR ****\\
    //This method will go ahead and validate the note field provided to it to ensure it meets specification,
    //It will return true or false as its outcome.
    public static boolean noteValidator(TextField note) {
        boolean outcome;


        //Check the length
        if(note.getText().length() > 50){

            //if too large then add a error and set the outcome to false
            note.getStyleClass().add("text-field-error");
            note.getStyleClass().removeAll("text-field-success");
            outcome = false;

        }else{

            //Else if It's good and not blank we add the success class
            if(!note.getText().isBlank()){
                note.getStyleClass().removeAll("text-field-error");
                note.getStyleClass().add("text-field-success");
            }else{
                note.getStyleClass().removeAll("text-field-success");
            }

            //if we reach here the outcome is true, and we have valid input
            outcome = true;
        }

        //finally we return the outcome
        return outcome;
    }
}
