//**** PACKAGE ****\\
package com.jackgharris.cosc2288.a2.controllers.components;

//**** PACKAGE IMPORTS ****\\
import com.jackgharris.cosc2288.a2.controllers.settings.SettingsImportExportController;
import com.jackgharris.cosc2288.a2.core.MyHealth;
import com.jackgharris.cosc2288.a2.models.Record;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;


//**** START EXPORT PREVIEW WINDOW CONTROLLER CLASS ****\\
public class ExportPreviewWindowController {

    //**** FXML IMPORTS ****\\
    //The follow class variables match the id's of the match fxml objects for this scene
    //this allows the class and its method to interact with them in an easy capacity.

    //Record table displaying information on the records being exported
    @FXML
    private TableView<Record> previewTable;

    //Record table type column, shows the type of the record
    @FXML
    private TableColumn<Record, String> typeColumn;

    //Record table id column, shows the id of the record
    @FXML
    private TableColumn<Record, Integer> idColumn;

    //Record table user_id column, shows the user id that the record belongs too, this will be -1 if any user can import
    @FXML
    private TableColumn<Record, Integer> userIdColumn;

    //Record table value column, shows the value of the record
    @FXML
    private TableColumn<Record, String> valueColumn;

    //Record table date column, shows the date for the record.
    @FXML
    private TableColumn<Record, String> dateColumn;

    //Parent anchor pane, this object is what has the custom theme styles applied and is the parent
    //of all other objects in the scene, as such any children of it also get the styles applied.
    @FXML
    private AnchorPane parent;


    //**** CLASS VARIABLES ****\\

    //SettingsImportExportController, this is the main windows that opens the pop-up it's important for this
    //class to have a reference to it so that it can pass a save message back to the caller on completion.
    private SettingsImportExportController controller;



    //**** INITIALIZE METHOD ****\\
    //This method is called when the fxml is loaded, in this context it is being used
    //as a constructor as we want to initialize these items on fxml load.
    public void initialize(){

        //Set the parent styles to the theme styles.
        this.parent.setStyle(MyHealth.getInstance().getUser().getTheme().getStyle());

        //Set the table view to resize into its available space, this ensures all column are
        //equal in size.
        this.previewTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        //Set the columns to use the property value factory to load the matching value for
        //that column.
        this.idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        this.userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        this.typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        this.valueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
        this.dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        //Set the selected model to null, this prevents a row or 'model' being selected
        this.previewTable.setSelectionModel(null);

        //Disable the sortable columns, this is required if you disable the model selection
        //as is done able.
        this.dateColumn.setSortable(false);
        this.idColumn.setSortable(false);
        this.typeColumn.setSortable(false);
        this.valueColumn.setSortable(false);
        this.userIdColumn.setSortable(false);
    }

    //**** SET RECORDS METHOD ****\\
    //This method will set the records to the observable array list provided, the observable
    //list is used as it interfaces better with the JavaFX components as its from that library.
    public void setRecords(ObservableList<Record> records){

        //set the table data to the records using addAll
        this.previewTable.getItems().addAll(records);
    }

    //**** SET IMPORT EXPORT CONTROLLER METHOD ****\\
    //This method will set the controller instance that the window will call back to when
    //a export has been completed or failed to complete.
    public void setImportExportController(SettingsImportExportController controller){
        this.controller = controller;
    }

    //**** CANCEL METHOD ****\\
    //This method will close this stage and act as a cancellation of the export request.
    public void cancel(){
        MyHealth.getStageById("export").close();
    }

    //**** SAVE METHOD ****\\
    //The save method will take the records provided and insert them into a CSV file that
    //has been specified by the user.
    public void save(){

        //Create our file chooser and set the title
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save");

        //Limit the file chooser to '.csv' only
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("csv (*.csv)","*.csv");
        fileChooser.getExtensionFilters().add(extensionFilter);

        //Add the file chooser to this view as a showSaveDialog
        File file = fileChooser.showSaveDialog(MyHealth.getStageById("import/export"));

        //Check if the file is not null, if null we assume the user changed his/her mind and do nothing.
        if(file != null){
            try {

                //create our file and print writers
                FileWriter fileWriter = new FileWriter(file);
                PrintWriter printWriter = new PrintWriter(fileWriter);

                //print each record to the new csv file
                this.previewTable.getItems().forEach((n)->{
                    printWriter.println(n.getType()+","+n.getUserId()+","+n.getValue()+","+n.getDate());
                });

                //close the file and print writer
                printWriter.close();
                fileWriter.close();

            } catch (Exception e) {

                //if we get an error set the error back in the main controller
                this.controller.setError(e.getMessage());
            }

            //finally we close the stage and set the success message
            MyHealth.getStageById("export").close();
            this.controller.setMessage("File exported to '"+file.getAbsolutePath()+"'");
        }



    }

}
