package com.jackgharris.cosc2288.a2.controllers.components;

import com.jackgharris.cosc2288.a2.utility.FXMLUtility;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class SettingsController {

    @FXML
    private AnchorPane menuContainerOuter;

    @FXML
    private AnchorPane contentContainerOuter;

    public void initialize() throws IOException {
        this.menuContainerOuter.setStyle("-fx-background-color: rgba(0,0,0,0.125)");

        FXMLLoader menuLoader = new FXMLLoader(FXMLUtility.settingsMenu);
        AnchorPane menuNodes = menuLoader.load();

        SettingsMenuController settingsMenuController = menuLoader.getController();

        settingsMenuController.setSettingsContentOuterContainer(this.contentContainerOuter);
        this.menuContainerOuter.getChildren().add(menuNodes.getChildren().get(0));

        VBox menu = (VBox) this.menuContainerOuter.getChildren().get(0);

        menu.setPrefWidth(this.menuContainerOuter.getPrefWidth());
    }
}
