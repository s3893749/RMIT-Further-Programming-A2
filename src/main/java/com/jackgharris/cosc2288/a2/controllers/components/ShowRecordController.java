//**** PACKAGE ****\\
package com.jackgharris.cosc2288.a2.controllers.components;

//**** PACKAGE IMPORTS ****\\
import com.jackgharris.cosc2288.a2.core.MyHealth;
import com.jackgharris.cosc2288.a2.models.Comment;
import com.jackgharris.cosc2288.a2.models.Record;
import com.jackgharris.cosc2288.a2.models.User;
import com.jackgharris.cosc2288.a2.utility.Resource;
import com.jackgharris.cosc2288.a2.utility.Validation;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.time.LocalDate;
import java.util.Objects;

//**** START SHOW RECORD CONTROLLER CLASS ****\\
public class ShowRecordController {

    //**** FXML IMPORTS ****\\
    //The follow class variables match the id's of the match fxml objects for this scene
    //this allows the class and its method to interact with them in an easy capacity.

    //The record type label will show the user the type of record that is currently set.
    @FXML
    private Label recordTypeLabel;

    //The record notes text area will show the user the contents of a note that is set
    //for this record.
    @FXML
    private TextArea recordNotesTextArea;

    //The record date picker is the date selector for this window, it will allow you to
    //change the date of a record.
    @FXML
    private DatePicker recordDatePicker;

    //The record value text field will allow you to edit and change the value for this
    //record
    @FXML
    private TextField recordValueTextField;

    //To delete record button will enable you to delete the record from the database
    @FXML
    private Button deleteRecordButton;

    //To delete record password input forces the user to confirm to delete by entering
    //their password.
    @FXML
    private PasswordField deletePasswordInput;

    //The parent anchor pane is the main pane that will have the css and styles applied
    //to
    @FXML
    private AnchorPane parent;

    //The save button will save the record to the database on button press
    @FXML
    private Button saveButton;

    //**** CLASS VARIABLES ****\\

    //The static show record controller instance is a reference back to the controller
    //that created this popup window, this is used to force the caller controller to
    //update its models after a change.
    public static ShowRecordController instance;

    //The record is the current record being viewed.
    private Record record;

    //The starting note is the value of the note on load.
    private String startingNote;

    //**** INITIALIZE METHOD ****\\
    //This method is called by JavaFX when the loaded FXML is initialized and can act as a constructor
    public void initialize(){
        //call our callback function to set the datepicker to disable any used dates.
        Callback<DatePicker, DateCell> dayCellFactory  = this.disableUsedDates();
        this.recordDatePicker.setDayCellFactory(dayCellFactory);

        //disable the date picker text input
        this.recordDatePicker.getEditor().setDisable(true);

        //disable the delete record button
        this.deleteRecordButton.setDisable(true);

        //set the record controller instance to this
        ShowRecordController.instance = this;

        //set the styles to our user theme styles
        this.parent.setStyle(MyHealth.getInstance().getUser().getTheme().getStyle());
    }

    //**** CANCEL METHOD ****\\
    //This method will cancel the request and close the current stage.
    public void cancel(){
        MyHealth.getStageById("showRecord").close();
    }

    //**** SAVE METHOD ****\\
    //This method will save the changes to the database, and insert a new comment
    //if required.
    public void save(){

        //set the new date and value and update the records database details
        this.record.setDate(this.recordDatePicker.getValue().toString());
        this.record.setValue(this.recordValueTextField.getText());
        this.record.updateDetails();

        //check if we need to insert a note, if so insert it or update it
        if(!this.startingNote.equals(this.recordNotesTextArea.getText()) && this.recordNotesTextArea.getText().length() < 50){
            Comment.add(new Comment(0,"Comment",MyHealth.getInstance().getUser().getId(),this.record.getId()+"/"+this.recordNotesTextArea.getText(),this.recordDatePicker.getValue().toString()));
        }

        //close this window
        MyHealth.getStageById("showRecord").close();

        //get the caller instance to update its models
        RecordWithLineChartController.getInstance().updateModels();
    }

    //**** RESET METHOD ****\\
    //This method will reset all the values back to the starting postions
    public void reset(){
        this.recordDatePicker.setValue(this.record.getDate());
        this.recordValueTextField.setText(this.record.getValue());
        this.recordNotesTextArea.setText(this.startingNote);
        this.recordValueTextField.getStyleClass().removeAll("text-field-error");
        this.recordValueTextField.getStyleClass().add("text-field-success");
        this.saveButton.setDisable(false);
    }

    //**** DELETE METHOD ****\\
    //This method will open an alert and confirm the deletion from the database
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
        alert.showAndWait().ifPresent(type -> {

            if (type == okButton) {
                //if the button is ok delete the record
                Record.delete(this.record);
                //close the stage
                MyHealth.getStageById("showRecord").close();
                //and update the calling controller
                RecordWithLineChartController.getInstance().updateModels();
            }
        });
    }

    //**** NOTE CHANGED METHOD ***\\
    //This method will validate the note text area is valid on every key press and either enable or disable the
    //save button based on that validation check.
    public void noteChanged(){
        if(!this.recordNotesTextArea.getText().isBlank() || this.recordNotesTextArea.getText().equals(this.startingNote)){

            if(this.recordNotesTextArea.getText().length() > 50){
                this.recordNotesTextArea.getStyleClass().add("text-field-error");
                this.saveButton.setDisable(true);
            }else{
                this.recordNotesTextArea.getStyleClass().add("text-field-success");
                this.recordNotesTextArea.getStyleClass().removeAll("text-field-error");
                this.saveButton.setDisable(false);
            }

        }
    }

    //**** VALUE CHANGED METHOD ****\\
    //This method will validate the new value on change to ensure it meets the validation requirements
    public void valueChanged(){

        //Firstly we check if this is a blood pressure record, if so run its validation else we
        //validate as a float.
        if(this.record.getType().equals("BloodPressure")){

            //validate using the validation helper method
            if (!Validation.isBloodPressure(this.recordValueTextField.getText())) {
                //set the error and remove the success class
                this.recordValueTextField.getStyleClass().add("text-field-error");
                this.recordValueTextField.getStyleClass().removeAll("text-field-success");
                //disable the save button
                this.saveButton.setDisable(true);
            } else {
                //remove the error and enable the success class
                this.recordValueTextField.getStyleClass().removeAll("text-field-error");
                this.recordValueTextField.getStyleClass().add("text-field-success");
                //re-enable the save button
                this.saveButton.setDisable(false);
            }

            //else we validate as float
        }else {

            //validate using the validation helper method
            if (!Validation.isFloat(this.recordValueTextField.getText())) {
                //set the error and remove the success class
                this.recordValueTextField.getStyleClass().add("text-field-error");
                this.recordValueTextField.getStyleClass().removeAll("text-field-success");
                //disable the save button
                this.saveButton.setDisable(true);
            } else {
                //remove the error and enable the success class
                this.recordValueTextField.getStyleClass().removeAll("text-field-error");
                this.recordValueTextField.getStyleClass().add("text-field-success");
                //re-enable the save button
                this.saveButton.setDisable(false);
            }
        }
    }

    //**** PASSWORD KEY PRESS METHOD ****\\
    //This method will validate that the password is valid and if so enable the delete button
    public void passwordKeyPress(){

        //check the password and either enable or disable the delete button
        if(!MyHealth.getInstance().getUser().getPassword().equals(User.hash(this.deletePasswordInput.getText()))){
            this.deletePasswordInput.getStyleClass().add("text-field-error");
            this.deletePasswordInput.getStyleClass().removeAll("text-field-success");
            this.deleteRecordButton.setDisable(true);
        }else{
            this.deletePasswordInput.getStyleClass().removeAll("text-field-error");
            this.deletePasswordInput.getStyleClass().add("text-field-success");
            this.deleteRecordButton.setDisable(false);
        }

    }

    //**** SET RECORD METHOD ****\\
    //This method is used by the caller and will set the record that is specific to this show/edit/delete window
    public void setRecord(Record record){

        //set the variables in the class
        this.record = record;
        this.recordTypeLabel.setText(record.getType()+" Record");
        this.recordDatePicker.setValue(record.getDate());
        this.recordValueTextField.setText(record.getValue());

        //get the comment if it exists
        ObservableList<Comment> comment = Comment.get(MyHealth.getInstance().getUser().getId(),this.record.getId());

        //if we have a comment load it
        if(comment.size() != 0){
            this.recordNotesTextArea.setText(comment.get(0).getNote());
            this.startingNote = comment.get(0).getNote();
        }else{
            //else set the starting note to blank.
            this.startingNote = "";
        }
    }

    //**** DISABLE USED DATES ****\\
    //This callback is used to disable all used dates from being able to be selected by the system
    private Callback<DatePicker, DateCell> disableUsedDates() {

        final Callback<DatePicker, DateCell> dayCellFactory = (final DatePicker datePicker) -> new DateCell() {

            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                for (Record record: Record.where("type",MyHealth.getInstance().getSelectedRecordType()).fromCache().get()){

                    //check if the current date item matches one from the database, if so then disable it and make the color red.
                    if(item.equals(record.getDate())){
                        setDisable(true);
                        setStyle("-fx-background-color: -fx-error;");

                    }
                }
            }
        };

        return dayCellFactory;
    }

    //**** GET RECORD METHOD ****\\
    //Returns the current record.
    public Record getRecord(){
        return this.record;
    }
}
