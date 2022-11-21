package com.jackgharris.cosc2288.a2.controllers.components;

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

public class ExportPreviewWindowController {

    @FXML
    private TableView<Record> previewTable;

    @FXML
    private TableColumn<Record, String> typeColumn;

    @FXML
    private TableColumn<Record, Integer> idColumn;

    @FXML
    private TableColumn<Record, Integer> userIdColumn;

    @FXML
    private TableColumn<Record, String> valueColumn;

    @FXML
    private TableColumn<Record, String> dateColumn;

    private SettingsImportExportController controller;

    @FXML
    private AnchorPane parent;

    public void initialize(){
        this.parent.setStyle(MyHealth.getInstance().getUser().getTheme().getStyle());


        this.previewTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        this.idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        this.userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        this.typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        this.valueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
        this.dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        this.previewTable.setSelectionModel(null);
        this.dateColumn.setSortable(false);
        this.idColumn.setSortable(false);
        this.typeColumn.setSortable(false);
        this.valueColumn.setSortable(false);
        this.userIdColumn.setSortable(false);
    }

    public void setRecords(ObservableList<Record> records){
        this.previewTable.getItems().addAll(records);
    }

    public void setImportExportController(SettingsImportExportController controller){
        this.controller = controller;
    }

    public void cancel(){
        MyHealth.getStageById("import/export").close();
    }

    public void save(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save");

        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("csv (*.csv)","*.csv");
        fileChooser.getExtensionFilters().add(extensionFilter);

        File file = fileChooser.showSaveDialog(MyHealth.getStageById("import/export"));

        if(file != null){
            try {
                FileWriter fileWriter = new FileWriter(file);
                PrintWriter printWriter = new PrintWriter(fileWriter);

                this.previewTable.getItems().forEach((n)->{
                    printWriter.println(n.getType()+","+n.getUserId()+","+n.getValue()+","+n.getDate());
                });

                printWriter.close();
                fileWriter.close();

            } catch (Exception e) {
                this.controller.setError(e.getMessage());
            }

            MyHealth.getStageById("export").close();

            this.controller.setMessage("File exported to '"+file.getAbsolutePath()+"'");
        }



    }

}
