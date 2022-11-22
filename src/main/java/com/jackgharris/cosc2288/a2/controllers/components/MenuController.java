package com.jackgharris.cosc2288.a2.controllers.components;

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

public class MenuController {


    @FXML
    private VBox menu;

    @FXML
    private Button recentMenuButton;

    @FXML
    private Button healthRecordButton;

    @FXML
    private Button temperatureRecordButton;

    @FXML
    private Button weightRecordButton;

    @FXML
    private Button bloodPressureRecordButton;

    @FXML
    private Button settingsButton;

    public void initialize() throws IOException {
        this.recentMenuButton.setStyle("-fx-background-color: -fx-button-primary-background-hover");
    }

    public void setSelectionFromLastPage() throws IOException {

        String lastPage = MyHealth.getInstance().getUser().getLastPage();
        if(lastPage.contains("settings.")){
            lastPage = "settings";
        }

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


    public void preferencesButtonPress(ActionEvent event) throws IOException {

        this.setActiveButton((Button) event.getSource());

        Stage stage = MyHealth.getStageById("dashboard");
        AnchorPane root = (AnchorPane) stage.getScene().getRoot();

        AnchorPane contentContainerOuter = (AnchorPane) root.getChildren().get(1);
        contentContainerOuter.getChildren().clear();


        FXMLLoader loader = new FXMLLoader(FXMLUtility.settingsPage);
        AnchorPane nodes = loader.load();
        SettingsController controller = loader.getController();
        controller.loadLastPage();

        contentContainerOuter.getChildren().clear();
        contentContainerOuter.getChildren().addAll(nodes.getChildren());

    }

    public void switchToRecent(ActionEvent event) throws IOException {

        MyHealth.getInstance().getUser().setLastPage("recent");

        this.setActiveButton((Button) event.getSource());

        Stage stage = MyHealth.getStageById("dashboard");
        AnchorPane root = (AnchorPane) stage.getScene().getRoot();

        AnchorPane contentContainerOuter = (AnchorPane) root.getChildren().get(1);
        contentContainerOuter.getChildren().clear();


        FXMLLoader loader = new FXMLLoader(FXMLUtility.recentOverview);
        AnchorPane nodes = loader.load();

        contentContainerOuter.getChildren().clear();
        contentContainerOuter.getChildren().addAll(nodes.getChildren());
    }


    public void switchToHealthRecord(ActionEvent event){

        MyHealth.getInstance().getUser().setLastPage("healthRecord");

        this.setActiveButton((Button) event.getSource());

        Stage stage = MyHealth.getStageById("dashboard");
        AnchorPane root = (AnchorPane) stage.getScene().getRoot();

        AnchorPane contentContainerOuter = (AnchorPane) root.getChildren().get(1);
        contentContainerOuter.getChildren().clear();
    }

    public void switchToTemperature(ActionEvent event) throws IOException {

        MyHealth.getInstance().getUser().setLastPage("temperature");

        this.setActiveButton((Button) event.getSource());

        Stage stage = MyHealth.getStageById("dashboard");
        AnchorPane root = (AnchorPane) stage.getScene().getRoot();

        AnchorPane contentContainerOuter = (AnchorPane) root.getChildren().get(1);
        contentContainerOuter.getChildren().clear();


        FXMLLoader loader = new FXMLLoader(FXMLUtility.recordOverview);
        AnchorPane nodes = loader.load();
        ((RecordWithLineChartController)loader.getController()).setRecordType("Temperature");
        ((RecordWithLineChartController)loader.getController()).construct();

        contentContainerOuter.getChildren().clear();
        contentContainerOuter.getChildren().addAll(nodes.getChildren());
    }

    public void switchToWeight(ActionEvent event) throws IOException {

        MyHealth.getInstance().getUser().setLastPage("weight");

        this.setActiveButton((Button) event.getSource());

        Stage stage = MyHealth.getStageById("dashboard");
        AnchorPane root = (AnchorPane) stage.getScene().getRoot();

        AnchorPane contentContainerOuter = (AnchorPane) root.getChildren().get(1);
        contentContainerOuter.getChildren().clear();


        FXMLLoader loader = new FXMLLoader(FXMLUtility.recordOverview);
        AnchorPane nodes = loader.load();
        ((RecordWithLineChartController)loader.getController()).setRecordType("Weight");
        ((RecordWithLineChartController)loader.getController()).construct();

        contentContainerOuter.getChildren().clear();
        contentContainerOuter.getChildren().addAll(nodes.getChildren());

    }

    public void switchToBloodPressure(ActionEvent event) throws IOException {

        MyHealth.getInstance().getUser().setLastPage("bloodPressure");

        this.setActiveButton((Button) event.getSource());


        Stage stage = MyHealth.getStageById("dashboard");
        AnchorPane root = (AnchorPane) stage.getScene().getRoot();

        AnchorPane contentContainerOuter = (AnchorPane) root.getChildren().get(1);
        contentContainerOuter.getChildren().clear();


        FXMLLoader loader = new FXMLLoader(FXMLUtility.recordWithAreaChart);
        AnchorPane nodes = loader.load();
        ((RecordWithAreaChartController)loader.getController()).setRecordType("BloodPressure");
        ((RecordWithAreaChartController)loader.getController()).construct();


        contentContainerOuter.getChildren().clear();
        contentContainerOuter.getChildren().addAll(nodes.getChildren());

    }

    public void logoutButtonPress() throws IOException {
        MyHealth.getStageById("dashboard").close();
        Stage stage = new Stage();

        stage.setScene(FXMLUtility.loadScene(FXMLUtility.loginFXML, stage, MyHealth.launcherCSS));

        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setResizable(false);
        stage.getIcons().add(Resource.favicon());
        stage.getProperties().put("id","launcher");

        stage.show();
    }

    private void setActiveButton(Button button){

        this.menu.getChildren().forEach((n)->{
            if(n == button){
                button.setStyle("-fx-background-color:  -fx-button-primary-background-hover");
            }else{
                n.setStyle("");
            }
        });
    }

}
