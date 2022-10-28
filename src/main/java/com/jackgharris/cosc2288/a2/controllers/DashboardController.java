package com.jackgharris.cosc2288.a2.controllers;

import com.jackgharris.cosc2288.a2.core.MyHealth;
import com.jackgharris.cosc2288.a2.models.Record;
import com.jackgharris.cosc2288.a2.models.Weight;
import com.jackgharris.cosc2288.a2.utility.FXMLUtility;
import com.jackgharris.cosc2288.a2.utility.Resource;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.time.LocalDate;

public class DashboardController {

    @FXML
    private AnchorPane parent;

    @FXML
    private VBox menu;

    @FXML
    private LineChart<String, Float> testChart;

    @FXML
    private TableView<Weight> weightTable;

    @FXML
    private TableColumn<Weight, String> dateColumn;

    @FXML
    private TableColumn<Weight, Float> weightColumn;

    @FXML
    private DatePicker addRecordDatePicker;

    @FXML
    private TextField addRecordWeightInput;

    @FXML
    private Button addRecordButton;

    @FXML
    private Button deleteRecordButton;

    @FXML
    private Button editRecordButton;

    private Callback<DatePicker, DateCell> disableUsedDates() {
        final Callback<DatePicker, DateCell> dayCellFactory = (final DatePicker datePicker) -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                // Disable Monday

                for (Record record: Weight.getAll()){
                    if(item.equals(record.getDate())){
                        setDisable(true);
                        setStyle("-fx-background-color: #c72929;");
                        setCursor(Cursor.CROSSHAIR);
                    }
                }
            }
        };
        return dayCellFactory;
    }

    @FXML
    public void initialize(){
        parent.setStyle("-fx-background-color: "+MyHealth.getTheme().getBackgroundColor());
        menu.setStyle("-fx-background-color: "+MyHealth.getTheme().getMenuColor());

        this.updateRecordTable();
        this.addRecordDatePicker.getEditor().setDisable(true);
        this.updateTestChart();


        Callback<DatePicker, DateCell> dayCellFactory  = this.disableUsedDates();

        this.addRecordDatePicker.setDayCellFactory(dayCellFactory);
        this.addRecordButton.setDisable(true);
        this.addRecordWeightInput.setDisable(true);
    }

    public void dateSelectedAction(){
        this.addRecordWeightInput.setDisable(false);
    }

    public void valueEnteredAction(){
        this.addRecordButton.setDisable(false);
    }

    public void showSettingsMenu(ActionEvent event) throws IOException {

        if(!MyHealth.isStageShown("settings")){
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.getIcons().add(Resource.settingsFavicon());
            stage.setTitle("Preferences");
            stage.setScene(FXMLUtility.loadScene(FXMLUtility.settingsFXML,stage,MyHealth.appCSS));
            stage.show();
            stage.getProperties().put("id", "settings");
        }else{
            Stage stage = MyHealth.getStageById("settings");
            stage.requestFocus();
        }
    }

    public void updateTestChart(){
        System.out.println("Updating Chart");
        //System.out.println(this.startDate.getValue());
        this.testChart.getData().clear();
        XYChart.Series<String, Float> series = new XYChart.Series<>();

        for (Weight weight : Weight.getAll(123)){
            series.getData().add(new XYChart.Data<>(weight.getDate().toString(), weight.getValue()));
        }
        this.testChart.setLegendVisible(false);
        this.testChart.getData().add(series);
    }

    public void updateRecordTable(){
        this.weightTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        this.dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        this.weightColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
        this.weightTable.setItems(Weight.getAll(123));
    }

    public void addNewRecord(ActionEvent event){
        Weight.add(new Weight(123,Float.parseFloat(addRecordWeightInput.getText()),this.addRecordDatePicker.getValue().toString()));
        this.addRecordDatePicker.setValue(null);
        this.addRecordWeightInput.setText(null);
        this.addRecordButton.setDisable(true);
        this.addRecordWeightInput.setDisable(true);
        this.updateTestChart();
        this.updateRecordTable();
    }

    public void deleteRecord(ActionEvent event) throws IOException {
       Weight weight = this.weightTable.getSelectionModel().getSelectedItem();

        MyHealth.data.put("record", weight);

        if(!MyHealth.isStageShown("deleteRecordPopup")){
            Stage stage = new Stage();
            stage.getProperties().put("id", "deleteRecordPopup");
            stage.setResizable(false);
            stage.getIcons().add(Resource.warningFavicon());
            stage.setTitle("Delete Record Confirmation");
            stage.setScene(FXMLUtility.loadScene(FXMLUtility.deleteRecordPopup,stage,MyHealth.appCSS));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(MyHealth.getStageById("dashboard"));
            stage.showAndWait();

        }else{
            Stage stage = MyHealth.getStageById("deleteRecordPopup");
            stage.requestFocus();
        }

       this.updateRecordTable();
       this.updateTestChart();
    }

    public void editRecord(ActionEvent event){

    }
}
