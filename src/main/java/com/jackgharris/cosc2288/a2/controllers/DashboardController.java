package com.jackgharris.cosc2288.a2.controllers;

import com.jackgharris.cosc2288.a2.core.MyHealth;
import com.jackgharris.cosc2288.a2.utility.FXMLUtility;
import com.jackgharris.cosc2288.a2.utility.Resource;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class DashboardController {

    @FXML
    private AnchorPane parent;

    @FXML
    private VBox menu;

    @FXML
    public void initialize(){
        parent.setStyle("-fx-background-color: "+MyHealth.getTheme().getBackgroundColor());
        menu.setStyle("-fx-background-color: "+MyHealth.getTheme().getMenuColor());
    }

    public void showSettingsMenu(ActionEvent event) throws IOException {

        if(!MyHealth.isStageShown("settings")){
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.getIcons().add(Resource.settingsFavicon());
            stage.setTitle("Preferences");
            stage.setScene(FXMLUtility.loadScene(FXMLUtility.settingsFXML,stage,MyHealth.appCSS));
            stage.show();
            stage.getProperties().put("id", "settings");
        }else{
            Stage stage = MyHealth.getStageById("settings");
            stage.requestFocus();
        }


    }
}
