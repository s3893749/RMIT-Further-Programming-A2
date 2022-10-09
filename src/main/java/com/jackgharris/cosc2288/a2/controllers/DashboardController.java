package com.jackgharris.cosc2288.a2.controllers;

import com.jackgharris.cosc2288.a2.core.MyHealth;
import com.jackgharris.cosc2288.a2.utility.FXMLUtility;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Menu;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class DashboardController {

    @FXML
    private Menu logoutButton;

    public void logout(ActionEvent event) throws IOException {
        System.out.println("Attempting logout");
        Stage dashboardStage = (Stage)this.logoutButton.getParentPopup().getScene().getWindow();
        Stage launcherStage = new Stage(StageStyle.UNDECORATED);
        launcherStage.setScene(FXMLUtility.loadScene(FXMLUtility.loginFXML,launcherStage, MyHealth.launcherCSS));
        launcherStage.show();
        dashboardStage.hide();
    }
}
