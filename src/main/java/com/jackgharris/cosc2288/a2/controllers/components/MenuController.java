package com.jackgharris.cosc2288.a2.controllers.components;

import com.jackgharris.cosc2288.a2.core.MyHealth;
import com.jackgharris.cosc2288.a2.utility.FXMLUtility;
import com.jackgharris.cosc2288.a2.utility.Resource;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


import java.io.IOException;

public class MenuController {

    public void preferencesButtonPress() throws IOException {

        if(!MyHealth.isStageShown("preferences")){
            Stage stage =  new Stage();
            stage.getProperties().put("id","preferences");
            stage.setScene(FXMLUtility.loadScene(FXMLUtility.settingsFXML,stage,MyHealth.appCSS));
            stage.setResizable(false);
            stage.show();
        }else{
            MyHealth.getStageById("preferences").requestFocus();
        }

    }

    public void switchToRecent(){
        Stage stage = MyHealth.getStageById("dashboard");
        AnchorPane root = (AnchorPane) stage.getScene().getRoot();

        AnchorPane contentContainerOuter = (AnchorPane) root.getChildren().get(1);
        contentContainerOuter.getChildren().clear();
    }


    public void switchToHealthRecord(){
        Stage stage = MyHealth.getStageById("dashboard");
        AnchorPane root = (AnchorPane) stage.getScene().getRoot();

        AnchorPane contentContainerOuter = (AnchorPane) root.getChildren().get(1);
        contentContainerOuter.getChildren().clear();
    }

    public void switchToTemperature() throws IOException {
        Stage stage = MyHealth.getStageById("dashboard");
        AnchorPane root = (AnchorPane) stage.getScene().getRoot();

        AnchorPane contentContainerOuter = (AnchorPane) root.getChildren().get(1);
        contentContainerOuter.getChildren().clear();


        FXMLLoader loader = new FXMLLoader(FXMLUtility.recordOverview);
        AnchorPane nodes = loader.load();
        ((RecordController)loader.getController()).setRecordType("Temperature");

        contentContainerOuter.getChildren().clear();
        contentContainerOuter.getChildren().addAll(nodes.getChildren());
    }

    public void switchToWeight() throws IOException {

        Stage stage = MyHealth.getStageById("dashboard");
        AnchorPane root = (AnchorPane) stage.getScene().getRoot();

        AnchorPane contentContainerOuter = (AnchorPane) root.getChildren().get(1);
        contentContainerOuter.getChildren().clear();


        FXMLLoader loader = new FXMLLoader(FXMLUtility.recordOverview);
        AnchorPane nodes = loader.load();
        ((RecordController)loader.getController()).setRecordType("Weight");

        contentContainerOuter.getChildren().clear();
        contentContainerOuter.getChildren().addAll(nodes.getChildren());

    }

    public void switchToBloodPressure() throws IOException {
        Stage stage = MyHealth.getStageById("dashboard");
        AnchorPane root = (AnchorPane) stage.getScene().getRoot();

        AnchorPane contentContainerOuter = (AnchorPane) root.getChildren().get(1);
        contentContainerOuter.getChildren().clear();


        FXMLLoader loader = new FXMLLoader(FXMLUtility.recordOverview);
        AnchorPane nodes = loader.load();
        ((RecordController)loader.getController()).setRecordType("BloodPressure");

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

}
