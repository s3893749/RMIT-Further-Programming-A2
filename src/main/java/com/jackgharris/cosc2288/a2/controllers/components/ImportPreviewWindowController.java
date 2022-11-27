//**** PACKAGE ****\\
package com.jackgharris.cosc2288.a2.controllers.components;

//**** PACKAGE IMPORTS ****\\
import com.jackgharris.cosc2288.a2.controllers.settings.SettingsImportExportController;
import com.jackgharris.cosc2288.a2.core.MyHealth;
import com.jackgharris.cosc2288.a2.models.Activity;
import com.jackgharris.cosc2288.a2.models.Record;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

//**** START IMPORT PREVIEW WINDOW CONTROLLER CLASS ****\\
public class ImportPreviewWindowController {

    //**** FXML IMPORTS ****\\
    //The follow class variables match the id's of the match fxml objects for this scene
    //this allows the class and its method to interact with them in an easy capacity.

    //Preview table, this table will show a preview of the importing data
    @FXML
    private TableView<Record> previewTable;

    //Preview table -> type column, this column shows the record type
    @FXML
    private TableColumn<Record, String> typeColumn;

    //Preview table -> id column, this shows the id of the record
    @FXML
    private TableColumn<Record, Integer> idColumn;

    //Preview table -> user_id column, this shows the user id of the imported record
    @FXML
    private TableColumn<Record, Integer> userIdColumn;

    //Preview table -> value column, shows the value of the record
    @FXML
    private TableColumn<Record, String> valueColumn;

    //Preview table -> date column, shows the date for that record
    @FXML
    private TableColumn<Record, String> dateColumn;

    //Parent anchor pane, this is our core parent object and has our css applied to it
    @FXML
    private AnchorPane parent;

    //The import type should be static as it is needed to be accessed via a table row
    //callback
    private static String importType;

    //The block records on import array is used to store the values that already exist in
    //the database, these will be blocked on import attempt.
    private static ArrayList<Record> blockRecordsOnImport;

    //**** CLASS VARIABLES ****\\

    //The controller reference will point back to the settings controller that opened this window
    private SettingsImportExportController controller;

    //This File variable is the file selected by the user to import the csv from.
    private File file;

    //**** INITIALIZE METHOD ****\\
    //This method is called when the fxml is loaded, in this context it is being used
    //as a constructor as we want to initialize these items on fxml load.
    public void initialize(){

        //Set our styles to ensure the windows matches the theme
        this.parent.setStyle(MyHealth.getInstance().getUser().getTheme().getStyle());

        //set our preview table to use the full size and evenly space the columns
        this.previewTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        //bind our all columns to the specific data they need
        this.idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        this.userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        this.typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        this.valueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
        this.dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        //disable the ability to select a model from the table
        this.previewTable.setSelectionModel(null);

        //disable sorting on any column, required as part of the selecting model disable
        this.dateColumn.setSortable(false);
        this.idColumn.setSortable(false);
        this.typeColumn.setSortable(false);
        this.valueColumn.setSortable(false);
        this.userIdColumn.setSortable(false);

        //Search our data and highlight any conflicts in red that cannot be imported
        this.previewTable.setRowFactory(row -> new TableRow<Record>() {
            @Override
            public void updateItem(Record item, boolean empty) {
                super.updateItem(item, empty);

                //check to ensure this is not null if so we want to skip the column using the return key
                if(item == null){
                    return;
                }

                //check if a record exists for it, if so then set the style, else remove any set style
                if(Record.where("type", ImportPreviewWindowController.importType).where("date", item.getDate().toString()).get().size() != 0){
                    this.setStyle("-fx-background-color: -fx-error");
                }else{
                    this.setStyle("");
                }
            }
        });
    }

    //**** IMPORT DATA METHOD ****\\
    //This method handles the data import and will import the data it can and report any blocked records
    public void importData(){

        //create an atomic integer to track our block and imported records
        AtomicInteger blocked = new AtomicInteger();
        AtomicInteger canImport = new AtomicInteger();

        //for each of the items in our preview table we check to ensure it's not block, if it is we then increment
        //the block or if not we increment the imported and then add the record to the system.
        this.previewTable.getItems().forEach((record -> {
            if(ImportPreviewWindowController.blockRecordsOnImport.contains(record)){
                blocked.getAndIncrement();
            }else{
                canImport.getAndIncrement();
                Record.add(record);
            }
        }));

        //next we create our return message showing the blocked and imported numbers
        String message = "Import complete, "+blocked.get()+" records skipped, "+canImport+" records imported";

        //set the message in the calling controller
        this.controller.setImportMessage(message);

        //add an activity for that message as well
        Activity.add(new Activity(message));

        //finally we close this import stage.
        MyHealth.getStageById("import").close();
    }


    //**** CANCEL METHOD ****\\
    //This method simply cancels the import and closes the stage
    public void cancel(){
        MyHealth.getStageById("import").close();
    }

    //**** SET IMPORT CONTROLLER METHOD ****\\
    //This method is called by the controller that created this window and will set a references to its self via
    //this method.
    public void setImportExportController(SettingsImportExportController controller){
        this.controller = controller;
    }


    //**** SET SELECTED FILE METHOD ****\\
    //This method will set the selected file and build the records for that file.
    public void setSelectedFile(File file){
        this.file = file;
        this.buildRecords();
    }

    //**** BUILD RECORDS METHOD ****\\
    //This method will build all the record objects for the data contained in the CSV file.
    private void buildRecords(){
        ImportPreviewWindowController.blockRecordsOnImport = new ArrayList<>();

        //Declare our records array list
        ArrayList<Record> records = new ArrayList<>();

        //Declare our record type style
        String recordType ="";

        try{
            //Create our bufferedReader and FileReader to read the file
            BufferedReader bufferedReader = new BufferedReader(new FileReader(this.file));

            //declare our line string
            String line;

            //while the line is not null we process the line input
            while((line = bufferedReader.readLine()) != null){

                //Split all our values at the "," and set the recordType
                String[] recordFields = line.split(",");
                recordType = recordFields[0];

                //Declare our new record object
                Record record;

                //Check if we are importing the records as the current user or as the user in the csv, if "-1" we use the current user else we
                //import for the export user id only.
                if(recordFields[1].equals("-1")){
                    record = new Record(-1,recordFields[0],MyHealth.getInstance().getUser().getId(),recordFields[2],recordFields[3],null);
                }else{
                    record = new Record(-1,recordFields[0],Integer.parseInt(recordFields[1]),recordFields[2],recordFields[3],null);
                }

                //Add our new record to the array
                records.add(record);

                //Check to ensure we do not have an existing record matching those values and date.
                ObservableList<Record> existing = Record.where("type", recordType).where("date", recordFields[3]).get();

                //if we do then add the record to a blocked list
                if(existing.size() != 0){
                    ImportPreviewWindowController.blockRecordsOnImport.add(record);
                }
            }

        } catch (IOException e) {
            //If we hit an error simply out the message, this should never run due to validation of the file.
            System.out.println("Import Error, please contact support '"+e.getMessage()+"'");
        }

        //Finally set the import type to the record type detected and update the preview table of items.
        ImportPreviewWindowController.importType = recordType;
        this.previewTable.getItems().addAll(records);
    }

}
