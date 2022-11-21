package com.jackgharris.cosc2288.a2.controllers.components;

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

public class ImportPreviewWindowController {

    private SettingsImportExportController controller;
    private File file;

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

        this.previewTable.setRowFactory(row -> new TableRow<Record>() {
            @Override
            public void updateItem(Record item, boolean empty) {
                super.updateItem(item, empty);

                if(item == null){
                    return;
                }

                if(Record.where("type", MyHealth.getInstance().getImportType()).where("date", item.getDate().toString()).get().size() != 0){
                    this.setStyle("-fx-background-color: -fx-error");
                }else{
                    this.setStyle("");
                }
            }
        });
    }

    public void importData(){
        AtomicInteger blocked = new AtomicInteger();
        AtomicInteger canImport = new AtomicInteger();

        this.previewTable.getItems().forEach((record -> {
            if(MyHealth.getInstance().getBlockedRecordsForImport().contains(record)){
                blocked.getAndIncrement();
            }else{
                canImport.getAndIncrement();
                Record.add(record);
            }
        }));

        String message = "Import complete, "+blocked.get()+" records skipped, "+canImport+" records imported";
        this.controller.setImportMessage(message);
        Activity.add(new Activity(message));
        MyHealth.getStageById("import").close();
    }


    public void cancel(){
        MyHealth.getStageById("import").close();
    }

    public void setImportExportController(SettingsImportExportController controller){
        this.controller = controller;
    }

    public void setSelectedFile(File file){
        this.file = file;
        this.buildRecords();
    }


    private void buildRecords(){
        ArrayList<Record> records = new ArrayList<>();
        String recordType ="";

        try{
            BufferedReader bufferedReader = new BufferedReader(new FileReader(this.file));
            String line;

            while((line = bufferedReader.readLine()) != null){

                String[] recordFields = line.split(",");
                recordType = recordFields[0];

                Record record;

                if(recordFields[1].equals("-1")){
                    record = new Record(-1,recordFields[0],MyHealth.getInstance().getUser().getId(),recordFields[2],recordFields[3]);
                }else{
                    record = new Record(-1,recordFields[0],Integer.parseInt(recordFields[1]),recordFields[2],recordFields[3]);
                }

                records.add(record);

                ObservableList<Record> existing = Record.where("type", recordType).where("date", recordFields[3]).get();

                if(existing.size() != 0){
                    MyHealth.getInstance().blockRecordOnImport(record);
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        MyHealth.getInstance().setImportType(recordType);
        this.previewTable.getItems().addAll(records);

    }


}
