//**** PACKAGE ****\\
package com.jackgharris.cosc2288.a2.controllers.components;

//**** PACKAGE IMPORTS ****\\
import com.jackgharris.cosc2288.a2.controllers.settings.SettingsController;
import com.jackgharris.cosc2288.a2.core.MyHealth;
import com.jackgharris.cosc2288.a2.utility.FXMLUtility;
import com.jackgharris.cosc2288.a2.utility.Resource;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

//**** START MENU CONTROLLER CLASS ****|\\
public class MenuController {

    //**** FXML IMPORTS ****\\
    //The follow class variables match the id's of the match fxml objects for this scene
    //this allows the class and its method to interact with them in an easy capacity.

    //The VBox menu is the container that holds all our buttons or "Menu Items"
    @FXML
    private VBox menu;

    //The recent menu button on press loads the recent actions overview page
    @FXML
    private Button recentMenuButton;

    //The health record button on press loads the health records page
    @FXML
    private Button healthRecordButton;

    //The temperature record button loads the temperature page
    @FXML
    private Button temperatureRecordButton;

    //The weight record button loads the weight page
    @FXML
    private Button weightRecordButton;

    //The blood pressure record button loads the blood pressure page
    @FXML
    private Button bloodPressureRecordButton;

    //The settings button loads the settings page
    @FXML
    private Button settingsButton;

    //**** INITIALIZE METHOD ****\\
    //This method is called when the fxml is loaded, in this context it is being used
    //as a constructor as we want to initialize these items on fxml load.
    public void initialize() throws IOException {

        //Set the recent button (first in the list) to our active menu color
        this.recentMenuButton.setStyle("-fx-background-color: -fx-button-primary-background-hover");
    }

    //**** SET SELECTION FROM LAST PAGE METHOD ****\\
    //This method will load the page based on the last page string that is saved
    //inside the users database
    public void setSelectionFromLastPage() throws IOException {


        //Get our last loaded page from the user
        String lastPage = MyHealth.getInstance().getUser().getLastPage();

        //Check to see if it's a settings page, if so trim the string to only be "settings"
        //instead of "settings.x"
        if(lastPage.contains("settings.")){
            lastPage = "settings";
        }

        //Perform a switch statement to load the last page based on the variable, this will
        //trigger the page selection exeactly the same as if the user selected the button.
        switch (lastPage){
            case "recent" ->{
                this.switchToRecent(new ActionEvent(this.recentMenuButton,null));
            }
            case "healthRecord" ->{
                this.switchToHealthRecord(new ActionEvent(this.healthRecordButton,null));
            }
            case "temperature" ->{
                this.switchToTemperature(new ActionEvent(this.temperatureRecordButton,null));
            }
            case "weight" ->{
                this.switchToWeight(new ActionEvent(this.weightRecordButton,null));
            }
            case "bloodPressure"->{
                this.switchToBloodPressure(new ActionEvent(this.bloodPressureRecordButton,null));
            }
            case "settings" ->{
                this.preferencesButtonPress(new ActionEvent(this.settingsButton,null));
            }
        }

    }

    //**** SELECT TO RECENT METHOD ****\\
    //This method will load the recent overview page and set the button to active
    public void switchToRecent(ActionEvent event) throws IOException {

        this.loadPage((Button) event.getSource(),FXMLUtility.recentOverview, "recent");
    }

    //**** SELECT HEALTH RECORD METHOD ****\\
    //Load the health record page and set the last page and button to active
    public void switchToHealthRecord(ActionEvent event) throws IOException {

        this.loadPage((Button) event.getSource(), FXMLUtility.heathRecordPage, "healthRecord");
    }

    //**** SELECT TEMPERATURE METHOD ****\\
    //Load our record with line chart  page and set the record type to "Temperature" then call
    //the construct method.
    public void switchToTemperature(ActionEvent event) throws IOException {

        RecordWithLineChartController controller = (RecordWithLineChartController) this.loadPage((Button) event.getSource(),FXMLUtility.recordOverview,"temperature");

        controller.setRecordType("Temperature");
        controller.construct();
    }


    //**** SELECT WEIGHT METHOD ****\\
    //Load our record with line chart page and set the record type to "Temperature" then call
    //the construct method.
    public void switchToWeight(ActionEvent event) throws IOException {

        RecordWithLineChartController controller = (RecordWithLineChartController) this.loadPage((Button) event.getSource(),FXMLUtility.recordOverview,"weight");

        controller.setRecordType("Weight");
        controller.construct();
    }

    //**** SELECT BLOOD PRESSURE METHOD ****\\
    //Load our record with area chart page and set the record type to "BloodPressure" then
    //call the construct method.
    public void switchToBloodPressure(ActionEvent event) throws IOException {

        RecordWithAreaChartController controller = (RecordWithAreaChartController) this.loadPage((Button)event.getSource(),FXMLUtility.recordWithAreaChart,"bloodPressure");

        controller.setRecordType("BloodPressure");
        controller.construct();
    }

    //**** SELECT SETTINGS PAGE METHOD ****\\
    //Load our settings page and then call the load last page method on its controller,
    //this is required as the settings page has sub-pages that will need to load.
    public void preferencesButtonPress(ActionEvent event) throws IOException {

        SettingsController controller = (SettingsController)this.loadPage((Button) event.getSource(),FXMLUtility.settingsPage,null);

        controller.loadLastPage();
    }

    //**** SELECT LOGOUT METHOD ****\\
    //This method will close this current stage and create a new launcher / login stage for
    //the user to interact with.
    public void logoutButtonPress() throws IOException {

        //close the current stage
        MyHealth.getStageById("dashboard").close();

        //create a new stage
        Stage stage = new Stage();

        //set the stage scene
        stage.setScene(FXMLUtility.loadScene(FXMLUtility.loginFXML, stage, Objects.requireNonNull(MyHealth.class.getResource("login.css")).toExternalForm()));

        //make the window transparent and not resizable
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setResizable(false);

        //add the icon
        stage.getIcons().add(Resource.favicon());

        //set the stage id
        stage.getProperties().put("id","launcher");

        //show the login stage
        stage.show();
    }

    //**** LOAD PAGE METHOD ****\\
    //This method is called by the button presses and faked button press via the load last page method
    //and is used to load the FXML page into the content container and set the active button and
    //last page variable.
    private Object loadPage(Button selectedButton, URL fxml, String lastPageId) throws IOException {

        //Set the active button to the button that was pressed
        this.setActiveButton(selectedButton);

        //Check if the last page id is not null, if not then set it
        if(lastPageId != null){
            MyHealth.getInstance().getUser().setLastPage(lastPageId);
        }

        //get our dashboard stage and root parent
        Stage stage = MyHealth.getStageById("dashboard");
        AnchorPane root = (AnchorPane) stage.getScene().getRoot();

        //get our content container and clear it of old children / fxml nodes
        AnchorPane contentContainerOuter = (AnchorPane) root.getChildren().get(1);
        contentContainerOuter.getChildren().clear();

        //load the fxml into the program
        FXMLLoader loader = new FXMLLoader(fxml);
        AnchorPane nodes = loader.load();

        //add the fxml nodes to the content container
        contentContainerOuter.getChildren().addAll(nodes.getChildren());

        //finally we return the controller in case any other methods need to be called.
        return loader.getController();
    }


    //**** SET ACTIVE BUTTON METHOD ****\\
    //This method will accept a button and then set the style of that button to
    //be the active button background color whilst also removing any background
    //color calls on the other buttons.
    private void setActiveButton(Button button){

        //Loop over all menu items
        this.menu.getChildren().forEach((n)->{

            //check if they are the button we want to set
            if(n == button){
                //if so set the color
                button.setStyle("-fx-background-color:  -fx-button-primary-background-hover");
            }else{
                //else remove the color
                n.setStyle("");
            }
        });
    }

}
