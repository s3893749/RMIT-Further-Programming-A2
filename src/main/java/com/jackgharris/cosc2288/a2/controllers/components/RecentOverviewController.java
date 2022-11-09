package com.jackgharris.cosc2288.a2.controllers.components;

import com.jackgharris.cosc2288.a2.core.MyHealth;
import com.jackgharris.cosc2288.a2.models.Activity;
import com.jackgharris.cosc2288.a2.models.Record;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;


public class RecentOverviewController {

    @FXML
    private PieChart recordCountPieChart;

    @FXML
    private VBox recentActivityContainer;

    @FXML
    private ScrollPane recentActivityScrollPane;

    @FXML
    private Label healthRecordLabel;

    @FXML
    private Label weightLabel;

    @FXML
    private Label temperatureLabel;

    @FXML
    private Label bloodPressureLabel;
    private int currentActivityCount;

    public void initialize(){

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
        //                       RECORD COUNTS                         \\
        //-------------------------------------------------------------\\
        //
        //Next we set all our record counts
        //

        this.healthRecordLabel.setText(this.healthRecordLabel.getText()+healthRecordCount);
        this.temperatureLabel.setText(this.temperatureLabel.getText()+temperatureCount);
        this.weightLabel.setText(this.weightLabel.getText()+weightCount);
        this.bloodPressureLabel.setText(this.bloodPressureLabel.getText()+bloodPressureCount);

        //-------------------------------------------------------------\\
        //                   RECENT USER ACTIONS                       \\
        //-------------------------------------------------------------\\
        //
        //Finally we can append all our user actions into the scene.
        //

        this.recentActivityScrollPane.setFitToWidth(true);
        this.recentActivityScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        this.currentActivityCount = 0;


        this.recentActivityContainer.setStyle("-fx-padding: 32");
        this.recentActivityContainer.setSpacing(16);
        this.recentActivityScrollPane.getStylesheets().add(String.valueOf(MyHealth.class.getResource("css/scrollPane.css")));

        this.loadRecentActivity();
        this.currentActivityCount +=10;



    }

    public void loadRecentActivity(){

        ArrayList<Activity> activities = Activity.get(10,this.currentActivityCount);

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

            String textContent = n.getDescription()+" at "+n.getTimeReadable();
            if(textContent.length() >60){
                textContent = textContent.substring(0, 60);
                textContent+="..";
            }
            Label text = new Label(textContent);

            text.setLayoutY(19);
            text.setLayoutX(54);
            notification.getChildren().add(text);
            this.recentActivityContainer.getChildren().add(notification);
        });

        this.currentActivityCount += 10;

    }
}
