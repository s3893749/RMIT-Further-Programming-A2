//**** PACKAGE ****\\
package com.jackgharris.cosc2288.a2.controllers.components;

//**** PACKAGE IMPORTS ****\\
import com.jackgharris.cosc2288.a2.core.MyHealth;
import com.jackgharris.cosc2288.a2.models.Activity;
import com.jackgharris.cosc2288.a2.models.HealthRecord;
import com.jackgharris.cosc2288.a2.models.Record;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

//**** START RECENT OVERVIEW CONTROLLER CLASS ****|\\
public class RecentOverviewController {


    //**** FXML IMPORTS ****\\
    //The follow class variables match the id's of the match fxml objects for this scene
    //this allows the class and its method to interact with them in an easy capacity.

    //Record count pie chart shows all the counts of the values in the database
    @FXML
    private PieChart recordCountPieChart;

    //Recent activity contains the list of all the recent activity actions
    @FXML
    private VBox recentActivityContainer;

    //Recent activity scroll pane is the parent for the recent activity and is scrollable.
    @FXML
    private ScrollPane recentActivityScrollPane;

    //Health Record label shows the count of all health records
    @FXML
    private Label healthRecordLabel;

    //Weight label shows the count of all weight records
    @FXML
    private Label weightLabel;

    //Temperature label shows the count of all temperature records
    @FXML
    private Label temperatureLabel;

    //Blood pressure label shows the count of all temperature records.
    @FXML
    private Label bloodPressureLabel;

    //Current activity count, stores the count of how many activity records are shown at once
    private int currentActivityCount;

    //Hide recent logins button, toggles the system to indicate if the recent logins should be shown as activity.
    @FXML
    private Button hideRecentLoginsButton;

    //**** INITIALIZE METHOD ****\\
    //This method is called when the fxml is loaded, in this context it is being used
    //as a constructor as we want to initialize these items on fxml load.
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
        int healthRecordCount = HealthRecord.getAllForCurrentUser(true).size();

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

        //set our current activity count to 0, this is our starting point
        this.currentActivityCount = 0;

        //Set our activity scroll pane and container styling
        this.recentActivityContainer.setStyle("-fx-padding: 32");
        this.recentActivityContainer.setSpacing(16);
        this.recentActivityScrollPane.getStylesheets().add(String.valueOf(MyHealth.class.getResource("css/scrollPane.css")));

        //Finally we update the status of the hide recent logins button to reflect the current users preference
        this.updateHideRecentLoginsButton();
    }

    //**** LOAD RECENT ACTIVITY METHOD ****\\
    //This method will get the recent activity from the database for the current user
    //and build the activity objects and append them to the activity container
    public void loadRecentActivity(){

        //Get our list of activity objects for the user, limit this to 10 and offset it by the current count
        ArrayList<Activity> activities = Activity.get(10,this.currentActivityCount,MyHealth.getInstance().getUser().shouldHideRecentLogins());

        //count how many we have loaded
        AtomicInteger count = new AtomicInteger();

        //for each activity we build a notification node to show the data
        activities.forEach((n)->{

            //create the node
            AnchorPane notification = new AnchorPane();
            //set the height and width
            notification.setPrefHeight(52);
            notification.setPrefWidth(-1);

            //add our style class and style sheet
            notification.getStyleClass().add("notification");
            notification.getStylesheets().add(String.valueOf(MyHealth.class.getResource("css/recentAction.css")));

            //create our image view for the activity icon
            ImageView imageView = new ImageView(String.valueOf(MyHealth.class.getResource("icons/action_icon.png")));

            //set the layout x,y and fit width & height for the image
            imageView.setFitHeight(30);
            imageView.setFitWidth(30);
            imageView.setLayoutX(12);
            imageView.setLayoutY(12);

            //add the image to the notification parent
            notification.getChildren().add(imageView);

            //create our text content that is a combination of the description and date
            String textContent = n.getDescription()+" at "+n.getTimeReadable();

            //create a label for the text content
            Label text = new Label(textContent);

            //set the layout x,y for the content and add it top the notification parent.
            text.setLayoutY(19);
            text.setLayoutX(54);
            notification.getChildren().add(text);

            count.getAndIncrement();

            //Finally we append the notification to the parent and rerun this foreach loop
            this.recentActivityContainer.getChildren().add(notification);
        });

        //lastly at the end we append the offset.
        this.currentActivityCount += count.get();

    }

    //**** HIDE RECENT LOGINS METHOD ****\\
    //This method toggles the status of hiding the recent logins from the user activity stream
    public void hideRecentLogins(){
        MyHealth.getInstance().getUser().setShouldHideLogins(!MyHealth.getInstance().getUser().shouldHideRecentLogins());
        this.updateHideRecentLoginsButton();
    }

    //**** UPDATE HIDE RECENT LOGINS BUTTON ****\\
    //This method updates the toggle button to that it reflects the real status of if the
    //recent logins should be shown or hidden.
    private void updateHideRecentLoginsButton(){

        if(MyHealth.getInstance().getUser().shouldHideRecentLogins()){

            //If we should hide the login (current) then set the toggle button to active
            //and set the text to reflect this.
            this.hideRecentLoginsButton.setText("Hiding Login Activity");
            this.hideRecentLoginsButton.getStyleClass().remove("toggle-button-inactive");
            this.hideRecentLoginsButton.getStyleClass().add("toggle-button-active");
        }else{
            //Else if we should show the logins then we should set it to active
            //and update the text to reflect this
            this.hideRecentLoginsButton.setText("Showing login Activity");
            this.hideRecentLoginsButton.getStyleClass().remove("toggle-button-active");
            this.hideRecentLoginsButton.getStyleClass().add("toggle-button-inactive");
        }

        //Clear all the old login items
        this.recentActivityContainer.getChildren().clear();
        //reset our activity counter to 0
        this.currentActivityCount = 0;
        //reload all our recent activities
        this.loadRecentActivity();
    }
}
