package com.jackgharris.cosc2288.a2.controllers;

import com.jackgharris.cosc2288.a2.core.MyHealth;
import com.jackgharris.cosc2288.a2.utility.FXMLUtility;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

import java.io.IOException;

public class TerminalSettingsController {


    public void backButton(ActionEvent event) throws IOException {
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Preferences");
        stage.setScene(FXMLUtility.loadScene(FXMLUtility.settingsFXML,stage, MyHealth.appCSS));
        stage.show();
    }


}
