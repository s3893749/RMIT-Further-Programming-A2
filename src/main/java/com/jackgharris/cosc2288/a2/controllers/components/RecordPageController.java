//**** PACKAGE ****\\
package com.jackgharris.cosc2288.a2.controllers.components;

//**** PACKAGE IMPORTS ****\\
import com.jackgharris.cosc2288.a2.core.MyHealth;
import com.jackgharris.cosc2288.a2.models.Record;
import com.jackgharris.cosc2288.a2.utility.FXMLUtility;
import com.jackgharris.cosc2288.a2.utility.Resource;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.time.LocalDate;

//**** START ABSTRACT RECORD PAGE CONTROLLER CLASS ****\\
//This class is abstract, so it contains abstract methods that must be
//implemented by any children of this parent class.
public abstract class RecordPageController{

    //**** FXML IMPORTS ****\\
    //The follow class variables match the id's of the match fxml objects for this scene
    //this allows the class and its method to interact with them in an easy capacity.

    //Record name label, shows the current record type
    @FXML
    protected Label recordName;

    //Record table view, shows all the loaded records in table format
    @FXML
    protected TableView<Record> recordTable;

    //Record table date column shows the date of records in the table
    @FXML
    protected TableColumn<Record, String> dateColumn;

    //Record table value column shows the value of the records in the table column
    @FXML
    protected TableColumn<Record, String> valueColumn;

    //Add record date picker, is the date picker that is used to select the date for a new record
    @FXML
    protected DatePicker addRecordDatePicker;

    //View record button, shows the record for the table selection
    @FXML
    protected Button viewRecordButton;

    //Show limit, will limit how many records are shown
    protected int limit;

    //Record type, will store what type of record this page is showing
    protected String recordType;

    //**** STATIC VARIABLES ****\\
    //The static instance variable is used by the children to call back to this page
    //after a record has been updated, edited or deleted.
    private static RecordPageController instance;

    //**** STATIC GET INSTANCE METHOD ****\\
    //This method will return the current instance and is assessable anywhere via the
    //static keyword.
    public static RecordPageController getInstance(){
        return RecordPageController.instance;
    }

    //**** SET RECORD TYPE METHOD ***\\
    //This method will set the record type for the view and update the limit
    //and label text to reflect this.
    public void setRecordType(String recordType){

        this.recordType = recordType;
        this.recordName.setText(recordType);
        MyHealth.getInstance().setSelectedRecordType(this.recordType);
        this.limit =0;
    }

    //**** ABSTRACT ADD RECORD METHOD ***\\
    //This method  will be called by the addRecord button to add a new record
    //to the database
    public abstract void addRecord();

    //**** ABSTRACT CONSTRUCT METHOD ****\\
    //This method is called after the add record method and acts like a deferred
    //constructor for items that need to happen after a record has been set.
    public abstract void construct();

    //**** ABSTRACT UPDATE MODELS METHOD ****\\
    //This method will house the logic to update the models in the view
    public abstract void updateModels();

    //**** SET INSTANCE METHOD ****\\
    //This method will be used by the children to set the static instance to them self's.
    protected void setInstance(RecordPageController instance){
        RecordPageController.instance = instance;
    }

    //**** GET RECORD TYPE ****\\
    //This method returns the record type to the caller that's set for this controller.
    public String getRecordType(){
        return this.recordType;
    }

    //**** SHOW ALL RECORDS METHOD ****\\
    //This method sets the limit to 0 and updates the models
    public void showAllRecords(){
        this.limit = 0;
        this.updateModels();
    }

    //**** SHOW LAST WEEK METHOD ****\\
    //This record sets the limit to 7 and updates the models
    public void showLastWeek(){
        this.limit = 7;
        this.updateModels();
    }

    //**** SHOW LAST MONTH METHOD ****\\
    //This method sets the limit to 30 and then updates the models
    public void showLastMonth(){
        this.limit = 30;
        this.updateModels();
    }

    //**** UPDATE RECORD SELECTION METHOD ***\\
    //This methods is triggered when a table row is selected, it then checks to ensure that row
    //is not null and if not will then enable the record button.
    public void updateRecordSelection(){
        this.viewRecordButton.setDisable(this.recordTable.getSelectionModel().getSelectedItem() == null);
    }

    //**** DATE PICKER DISABLE USED DATES CALLBACK METHOD ****
    //This method will provide a callback for the data-picker cell builder method to check all records in
    //the database and disable any that are used.
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

    //**** SHOW RECORD METHOD ****\\
    //This method will create a new show record window popup that allows the user
    //to edit and delete the record.
    public void showRecord() throws IOException {

        //check to ensure the stage is not already shown
        if(!MyHealth.isStageShown("showRecord")){
            //create a new stage
            Stage stage = new Stage();
            //set the id
            stage.getProperties().put("id","showRecord");
            //disable resize
            stage.setResizable(false);
            //load the FXML
            FXMLLoader loader = new FXMLLoader(FXMLUtility.showRecord);
            //set the scene
            Scene scene = new Scene(loader.load());
            //set the title
            stage.setTitle("MyHealth Record | Show/Edit/Delete");
            //set the icon
            stage.getIcons().add(Resource.favicon());
            //get the controller
            ShowRecordController controller = loader.getController();
            //set the active record
            controller.setRecord(this.recordTable.getSelectionModel().getSelectedItem());
            //bind the scene to the stage
            stage.setScene(scene);
            //finally show the stage
            stage.show();
        }else{
            //else if its already shown simply give it focus.
            MyHealth.getStageById("showRecord").requestFocus();
        }
    }



}
