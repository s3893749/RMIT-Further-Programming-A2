package com.jackgharris.cosc2288.a2.controllers.components;

import com.jackgharris.cosc2288.a2.core.MyHealth;
import com.jackgharris.cosc2288.a2.models.Activity;
import com.jackgharris.cosc2288.a2.models.Record;
import com.jackgharris.cosc2288.a2.models.RecordType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;


public class RecentOverviewController {

    @FXML
    private PieChart recordCountPieChart;

    @FXML
    private TableView<RecordType> totalRecordEntriesTable;

    @FXML
    private VBox recentActivityContainer;

    @FXML
    private ScrollPane recentActivityScrollPane;

    public void initialize() throws IOException {

        //-------------------------------------------------------------\\
        //                   GET RECORD TYPE COUNTS                    \\
        //-------------------------------------------------------------\\
        //
        //This method gets all the size counts for all our record types for
        //the current user.
        //
        int temperatureCount = Record.where("type", "Temperature").withCurrentUser().get().size();
        int weightCount = Record.where("type", "Weight").withCurrentUser().get().size();
        int bloodPressureCount = Record.where("type", "BloodPressure").withCurrentUser().get().size();
        int healthRecordCount = 0;

        //-------------------------------------------------------------\\
        //                   CREATE PIE CHART DATA                     \\
        //-------------------------------------------------------------\\
        //
        //Next we create our pie chart data and pass it the record counts
        //that we have declared above.
        //
        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList(
                new PieChart.Data("Health Record", healthRecordCount),
                new PieChart.Data("Temperature", temperatureCount),
                new PieChart.Data("Weight", weightCount),
                new PieChart.Data("Blood Pressure", bloodPressureCount)
        );

        this.recordCountPieChart.getData().addAll(pieData);

        //-------------------------------------------------------------\\
        //                         TABLE DATA                          \\
        //-------------------------------------------------------------\\
        //
        //Next we need to set define our column factory's and then create
        //our list that will contain all the values
        //
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

        //-------------------------------------------------------------\\
        //                   RECENT USER ACTIONS                       \\
        //-------------------------------------------------------------\\
        //
        //Finally we can append all our user actions into the scene.
        //

        this.recentActivityScrollPane.setFitToWidth(true);


        this.recentActivityContainer.setStyle("-fx-padding: 32");
        this.recentActivityContainer.setSpacing(16);
        this.recentActivityScrollPane.getStylesheets().add(String.valueOf(MyHealth.class.getResource("css/scrollPane.css")));

        ArrayList<Activity> activities = Activity.get(10);


        activities.forEach((n)->{
            AnchorPane notification = new AnchorPane();
            notification.setPrefHeight(52);
            notification.setPrefWidth(-1);
            notification.getStyleClass().add("notification");
            notification.getStylesheets().add(String.valueOf(MyHealth.class.getResource("css/recentAction.css")));

            ImageView imageView = new ImageView(String.valueOf(MyHealth.class.getResource("icons/action_icon.png")));
            imageView.setFitHeight(30);
            imageView.setFitWidth(30);
            imageView.setLayoutX(12);
            imageView.setLayoutY(12);
            notification.getChildren().add(imageView);

            Label text = new Label(n.getDescription()+" "+n.getTime());
            text.setLayoutY(19);
            text.setLayoutX(54);
            notification.getChildren().add(text);
            this.recentActivityContainer.getChildren().add(notification);
        });



    }
}
