package com.jackgharris.cosc2288.a2.controllers;

import com.jackgharris.cosc2288.a2.core.MyHealth;
import com.jackgharris.cosc2288.a2.utility.FXMLUtility;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class TerminalSettingsController {

    @FXML
    private HBox header;

    @FXML
    private AnchorPane settings;

    @FXML
    public void initialize(){
        settings.setStyle("-fx-background-color: "+MyHealth.getTheme().getBackgroundColor());
        header.setStyle("-fx-background-color: "+MyHealth.getTheme().getMenuColor());
    }

    public void backButton(ActionEvent event) throws IOException {
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Preferences");
        stage.setScene(FXMLUtility.loadScene(FXMLUtility.settingsFXML,stage, MyHealth.appCSS));
        stage.show();
    }


}
