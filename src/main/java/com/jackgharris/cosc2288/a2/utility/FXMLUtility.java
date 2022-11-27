//**** PACKAGE ****\\
package com.jackgharris.cosc2288.a2.utility;

//**** PACKAGE IMPORTS \\
import com.jackgharris.cosc2288.a2.core.MyHealth;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

//**** START FXML UTILITY CLASS ****\\
//This class has the URL links to all our FXML files and provides an easy
//way for other classes to access them by having them as static URLs
public class FXMLUtility {

    //**** FXML URL LINKS ****\\
    public static URL loginFXML = MyHealth.class.getResource("stages/login.fxml");
    public static URL registrationFXML = MyHealth.class.getResource("stages/registration.fxml");

    public static URL dashboardFXML = MyHealth.class.getResource("stages/dashboard.fxml");

    public static URL menu = MyHealth.class.getResource("components/menu.fxml");

    public static URL recordOverview = MyHealth.class.getResource("components/recordWithLineChart.fxml");

    public static URL recentOverview = MyHealth.class.getResource("components/recentOverview.fxml");

    public static URL settingsPage = MyHealth.class.getResource("settings/settings.fxml");

    public static URL settingsMenu = MyHealth.class.getResource("settings/settingsMenu.fxml");

    public static URL settingsAccountPage = MyHealth.class.getResource("settings/settingsAccountPage.fxml");

    public static URL settingsThemePage = MyHealth.class.getResource("settings/settingsThemePage.fxml");

    public static URL settingsImportExportPage = MyHealth.class.getResource("settings/settingsImportExportPage.fxml");

    public static URL settingsExportPreview = MyHealth.class.getResource("components/exportPreviewWindow.fxml");

    public static URL settingsImportPreview = MyHealth.class.getResource("components/importPreviewWindow.fxml");

    public static URL showRecord =  MyHealth.class.getResource("components/showRecordWindow.fxml");

    public static URL recordWithAreaChart = MyHealth.class.getResource("components/recordWithAreaChart.fxml");

    public static URL heathRecordPage = MyHealth.class.getResource("components/heathRecord.fxml");

    public static URL showHealthRecord = MyHealth.class.getResource("components/showHealthRecordWindow.fxml");

    //**** LOAD SCENE METHOD ****\\
    //This utility helper method accepts a scene url, stage and css file and will combine the three
    //before returning the loaded scene, this streamlines the loading of scene later in the program.
    public static Scene loadScene(URL sceneURL, Stage stage, String css) throws IOException {

        //Create our FXML loader and pass it the scene URL its in constructor
        FXMLLoader fxmlLoader = new FXMLLoader(sceneURL);

        //load the scene into a new scene
        Scene scene =  new Scene(fxmlLoader.load());

        //set the fill color to transparent
        scene.setFill(Color.TRANSPARENT);

        //add the CSS
        scene.getStylesheets().add(css);

        //Add the mouse press events to drag the stage around the monitor.
        scene.setOnMousePressed(pressEvent ->{
            scene.setOnMouseDragged(dragEvent ->{
                stage.setX(dragEvent.getScreenX() - pressEvent.getSceneX());
                stage.setY(dragEvent.getScreenY() - pressEvent.getSceneY());
            });
        });

        //lastly we return the scene
        return scene;
    }

}
