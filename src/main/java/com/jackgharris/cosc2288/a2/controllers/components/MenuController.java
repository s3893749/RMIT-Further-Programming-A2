package com.jackgharris.cosc2288.a2.controllers.components;

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

    public void initialize(){
        this.recentMenuButton.setStyle("-fx-background-color: -fx-button-primary-background-hover");
    }


    public void preferencesButtonPress(ActionEvent event) throws IOException {

        this.setActiveButton((Button) event.getSource());

        Stage stage = MyHealth.getStageById("dashboard");
        AnchorPane root = (AnchorPane) stage.getScene().getRoot();

        AnchorPane contentContainerOuter = (AnchorPane) root.getChildren().get(1);
        contentContainerOuter.getChildren().clear();


        FXMLLoader loader = new FXMLLoader(FXMLUtility.settingsPage);
        AnchorPane nodes = loader.load();

        contentContainerOuter.getChildren().clear();
        contentContainerOuter.getChildren().addAll(nodes.getChildren());

    }

    public void switchToRecent(ActionEvent event) throws IOException {

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

        this.setActiveButton((Button) event.getSource());


        Stage stage = MyHealth.getStageById("dashboard");
        AnchorPane root = (AnchorPane) stage.getScene().getRoot();

        AnchorPane contentContainerOuter = (AnchorPane) root.getChildren().get(1);
        contentContainerOuter.getChildren().clear();
    }

    public void switchToTemperature(ActionEvent event) throws IOException {

        this.setActiveButton((Button) event.getSource());

        Stage stage = MyHealth.getStageById("dashboard");
        AnchorPane root = (AnchorPane) stage.getScene().getRoot();

        AnchorPane contentContainerOuter = (AnchorPane) root.getChildren().get(1);
        contentContainerOuter.getChildren().clear();


        FXMLLoader loader = new FXMLLoader(FXMLUtility.recordOverview);
        AnchorPane nodes = loader.load();
        ((RecordWithLineChartController)loader.getController()).setRecordType("Temperature");

        contentContainerOuter.getChildren().clear();
        contentContainerOuter.getChildren().addAll(nodes.getChildren());
    }

    public void switchToWeight(ActionEvent event) throws IOException {
        this.setActiveButton((Button) event.getSource());

        Stage stage = MyHealth.getStageById("dashboard");
        AnchorPane root = (AnchorPane) stage.getScene().getRoot();

        AnchorPane contentContainerOuter = (AnchorPane) root.getChildren().get(1);
        contentContainerOuter.getChildren().clear();


        FXMLLoader loader = new FXMLLoader(FXMLUtility.recordOverview);
        AnchorPane nodes = loader.load();
        ((RecordWithLineChartController)loader.getController()).setRecordType("Weight");

        contentContainerOuter.getChildren().clear();
        contentContainerOuter.getChildren().addAll(nodes.getChildren());

    }

    public void switchToBloodPressure(ActionEvent event) throws IOException {
        this.setActiveButton((Button) event.getSource());

        Stage stage = MyHealth.getStageById("dashboard");
        AnchorPane root = (AnchorPane) stage.getScene().getRoot();

        AnchorPane contentContainerOuter = (AnchorPane) root.getChildren().get(1);
        contentContainerOuter.getChildren().clear();


        FXMLLoader loader = new FXMLLoader(FXMLUtility.recordOverview);
        AnchorPane nodes = loader.load();
        ((RecordWithLineChartController)loader.getController()).setRecordType("BloodPressure");

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
