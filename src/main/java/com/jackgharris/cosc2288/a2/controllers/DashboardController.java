package com.jackgharris.cosc2288.a2.controllers;

import com.jackgharris.cosc2288.a2.core.MyHealth;
import com.jackgharris.cosc2288.a2.models.Weight;
import com.jackgharris.cosc2288.a2.utility.FXMLUtility;
import com.jackgharris.cosc2288.a2.utility.Resource;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class DashboardController {

    @FXML
    private AnchorPane parent;

    @FXML
    private VBox menu;

    @FXML
    private LineChart<String, Integer> testChart;

    @FXML
    private TableView<Weight> weightTable;

    @FXML
    private TableColumn<Weight, String> dateColumn;

    @FXML
    private TableColumn<Weight, Integer> weightColumn;

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

    @FXML
    public void initialize(){
        parent.setStyle("-fx-background-color: "+MyHealth.getTheme().getBackgroundColor());
        menu.setStyle("-fx-background-color: "+MyHealth.getTheme().getMenuColor());

        this.updateRecordTable();

        this.addRecordDatePicker.getEditor().setDisable(true);
        this.updateTestChart();
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
        XYChart.Series<String, Integer> series = new XYChart.Series<>();

        for (Weight weight : Weight.getAll(123)){
            series.getData().add(new XYChart.Data<>(weight.getDate(), weight.getValue()));
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
        Weight.add(new Weight(123,Integer.parseInt(addRecordWeightInput.getText()),this.addRecordDatePicker.getValue().toString()));
        this.updateTestChart();
        this.updateRecordTable();
    }

    public void deleteRecord(ActionEvent event){
       Weight weight = this.weightTable.getSelectionModel().getSelectedItem();

       Weight.remove(weight);
       this.updateRecordTable();
       this.updateTestChart();
    }

    public void editRecord(ActionEvent event){

    }
}
