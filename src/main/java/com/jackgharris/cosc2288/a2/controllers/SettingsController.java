package com.jackgharris.cosc2288.a2.controllers;

import com.jackgharris.cosc2288.a2.core.MyHealth;
import com.jackgharris.cosc2288.a2.utility.FXMLUtility;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;

public class SettingsController {

    @FXML
    private AnchorPane settings;

    @FXML
    private HBox header;

    @FXML
    public void initialize(){
        settings.setStyle("-fx-background-color: "+MyHealth.getTheme().getBackgroundColor());
        header.setStyle("-fx-background-color: "+MyHealth.getTheme().getMenuColor());
    }

    public void showTerminal(ActionEvent event) throws IOException {
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Preferences | Terminal");
        stage.setScene(FXMLUtility.loadScene(FXMLUtility.terminalFXML,stage, MyHealth.appCSS));
        stage.show();
    }

    public void showThemeSettings(ActionEvent event) throws IOException{
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Preferences | Theme");
        stage.setScene(FXMLUtility.loadScene(FXMLUtility.themeSettingsFXML,stage, MyHealth.appCSS));
        stage.show();
    }


}
