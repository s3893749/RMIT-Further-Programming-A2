package com.jackgharris.cosc2288.a2.controllers.components;

import com.jackgharris.cosc2288.a2.models.Record;
import com.jackgharris.cosc2288.a2.models.RecordType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class RecentOverviewController {

    @FXML
    private PieChart recordCountPieChart;

    @FXML
    private TableView<RecordType> totalRecordEntriesTable;

    public void initialize(){
        int temperatureCount = Record.where("type", "Temperature").withCurrentUser().get().size();
        int weightCount = Record.where("type", "Weight").withCurrentUser().get().size();
        int bloodPressureCount = Record.where("type", "BloodPressure").withCurrentUser().get().size();
        int healthRecordCount = 0;

        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList(
                new PieChart.Data("Health Record", healthRecordCount),
                new PieChart.Data("Temperature", temperatureCount),
                new PieChart.Data("Weight", weightCount),
                new PieChart.Data("Blood Pressure", bloodPressureCount)
        );

        this.recordCountPieChart.getData().addAll(pieData);

        this.totalRecordEntriesTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        this.totalRecordEntriesTable.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("name"));
        this.totalRecordEntriesTable.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("count"));

        ObservableList<RecordType> recordOverviewItems = FXCollections.observableArrayList(
                new RecordType("Health Record", healthRecordCount),
                new RecordType("Temperature",temperatureCount),
                new RecordType("Weight",weightCount),
                new RecordType("Blood Pressure", bloodPressureCount)
        );

        this.totalRecordEntriesTable.getItems().addAll(recordOverviewItems);
        this.totalRecordEntriesTable.setSelectionModel(null);
        this.totalRecordEntriesTable.getColumns().forEach((n)->{
            n.setSortable(false);
        });

    }
}
