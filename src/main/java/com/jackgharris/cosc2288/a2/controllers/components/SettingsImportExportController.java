package com.jackgharris.cosc2288.a2.controllers.components;

import com.jackgharris.cosc2288.a2.core.MyHealth;
import com.jackgharris.cosc2288.a2.models.Activity;
import com.jackgharris.cosc2288.a2.models.Record;
import com.jackgharris.cosc2288.a2.utility.FXMLUtility;
import com.jackgharris.cosc2288.a2.utility.Resource;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;


public class SettingsImportExportController {


    @FXML
    private DatePicker startDatePicker;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private ComboBox<String> recordTypeSelector;

    @FXML
    private Button allowAnyUserToImportThisDataButton;

    @FXML
    private Button exportButton;

    @FXML
    private Label message;

    @FXML
    private Label importMessage;

    private boolean allowAnyUserToImportThisData;

    public void initialize(){

        this.startDatePicker.getEditor().setDisable(true);
        this.endDatePicker.getEditor().setDisable(true);
        this.startDatePicker.setStyle("-fx-opacity: 1");

        this.recordTypeSelector.getItems().addAll("Temperature","Weight","Blood Pressure");

        this.toggleAllowAnyUserToImportThisData();
        this.message.setText("");
        this.importMessage.setText("");

        this.exportButton.setDisable(true);
    }

    public void setImportMessage(String message){
        this.importMessage.setText(message);
        this.importMessage.getStyleClass().remove("alert-danger");
        this.importMessage.getStyleClass().add("alert-success");
    }

    public void setMessage(String message){
        Activity.add(new Activity(this.recordTypeSelector.getValue()+" (records) were successfully exported to .csv"));
        this.message.setText(message);
        this.message.getStyleClass().remove("alert-danger");
        this.message.getStyleClass().add("alert-success");
    }

    public void setError(String message){
        this.message.setText(message);
        this.message.getStyleClass().remove("alert-success");
        this.message.getStyleClass().add("alert-danger");
    }

    public void validateExport(){

        int errors = 0;

        if(this.startDatePicker.getValue() == null){
            this.startDatePicker.getStyleClass().add("text-field-error");
            this.startDatePicker.setStyle("-fx-border-radius: 15px;");
            errors++;
        }else{
            this.startDatePicker.getStyleClass().removeAll("text-field-error");
            this.startDatePicker.setStyle(" -fx-border-width: 2px");

        }

        if(this.endDatePicker.getValue() == null){
            this.endDatePicker.getStyleClass().add("text-field-error");
            this.endDatePicker.setStyle("-fx-border-radius: 15px; -fx-border-width: 2px");
            errors++;
        }else{
            this.endDatePicker.getStyleClass().removeAll("text-field-error");
            this.endDatePicker.setStyle(" -fx-border-width: 2px");
        }

        if(this.recordTypeSelector.getValue() == null){
            this.recordTypeSelector.getStyleClass().add("text-field-error");
            errors++;

        }else{
            this.recordTypeSelector.getStyleClass().removeAll("text-field-error");
        }

        this.exportButton.setDisable(errors != 0);
    }

    public void toggleAllowAnyUserToImportThisData(){

        this.allowAnyUserToImportThisData = !this.allowAnyUserToImportThisData;

        if(!this.allowAnyUserToImportThisData){
            this.allowAnyUserToImportThisDataButton.setText("Restrict to your id");
            this.allowAnyUserToImportThisDataButton.setStyle("-fx-background-color: -fx-error");
        }else{
            this.allowAnyUserToImportThisDataButton.setText("Yes to any user");
            this.allowAnyUserToImportThisDataButton.setStyle("-fx-background-color: -fx-success");
        }
    }

    public void export() throws IOException {
        ObservableList<Record> records = Record.where("type",this.recordTypeSelector.getValue()).withCurrentUser().whereGreaterThan("date",this.startDatePicker.getValue().toString()).whereLessThan("date",this.endDatePicker.getValue().toString()).sort("date").get();

        if(this.allowAnyUserToImportThisData){
            records.forEach((n)->{
                n.setUserId(-1);
            });
        }

        if(!MyHealth.isStageShown("export")){
            Stage stage = new Stage();
            stage.getProperties().put("id","export");
            FXMLLoader loader = new FXMLLoader(FXMLUtility.settingsExportPreview);
            Scene scene = new Scene(loader.load());
            stage.setTitle("MyHealth Export Preview");
            stage.getIcons().add(Resource.importExportFavicon());
            ExportPreviewWindowController controller = loader.getController();
            controller.setRecords(records);
            controller.setImportExportController(this);
            stage.setScene(scene);
            stage.show();
        }else{
            MyHealth.getStageById("export").requestFocus();
        }

    }

    public void fileImport() throws IOException {

        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Select File To Import");

        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("csv (*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(extensionFilter);

        File file = fileChooser.showOpenDialog(MyHealth.getStageById("dashboard"));


        if(file == null){
            return;
        }


        if (!MyHealth.isStageShown("import")) {
            Stage stage = new Stage();
            stage.getProperties().put("id", "import");
            FXMLLoader loader = new FXMLLoader(FXMLUtility.settingsImportPreview);
            Scene scene = new Scene(loader.load());
            stage.setTitle("MyHealth Import Preview");
            stage.getIcons().add(Resource.importExportFavicon());
            ImportPreviewWindowController controller = loader.getController();
            controller.setImportExportController(this);
            controller.setSelectedFile(file);
            stage.setScene(scene);
            stage.show();
        } else {
            MyHealth.getStageById("import").requestFocus();
        }

    }

    public void discardExport(){
        this.endDatePicker.setValue(null);
        this.startDatePicker.setValue(null);
        this.recordTypeSelector.setValue(null);

        this.startDatePicker.getStyleClass().removeAll("text-field-error");
        this.endDatePicker.getStyleClass().removeAll("text-field-error");
        this.recordTypeSelector.getStyleClass().removeAll("text-field-error");

        this.exportButton.setDisable(true);
    }


}
