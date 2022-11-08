package com.jackgharris.cosc2288.a2.controllers;

import com.jackgharris.cosc2288.a2.controllers.components.popups.DeleteRecordPopupController;
import com.jackgharris.cosc2288.a2.controllers.components.popups.EditRecordPopupController;
import com.jackgharris.cosc2288.a2.core.MyHealth;
import com.jackgharris.cosc2288.a2.models.Weight;
import com.jackgharris.cosc2288.a2.utility.FXMLUtility;
import com.jackgharris.cosc2288.a2.utility.Resource;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class DashboardController{

    @FXML
    private AnchorPane parent;

    @FXML
    private AnchorPane menuContainerOuter;


    @FXML
    private TableView<Weight> weightTable;


    @FXML
    public void initialize() throws IOException {
        //Load the menu
        FXMLLoader menuLoader = new FXMLLoader(FXMLUtility.menu);
        AnchorPane menuNodes = menuLoader.load();
        this.menuContainerOuter.getChildren().add(menuNodes.getChildren().get(0));

        //set the background colors
        this.menuContainerOuter.setStyle("-fx-background-color: "+MyHealth.getInstance().getTheme().getMenuColor());
        this.parent.setStyle("-fx-background-color: "+MyHealth.getInstance().getTheme().getBackgroundColor());


        FXMLLoader loader = new FXMLLoader(FXMLUtility.recentOverview);
        AnchorPane nodes = loader.load();

        AnchorPane contentOuterContainer = (AnchorPane) this.parent.getChildren().get(1);
        contentOuterContainer.getChildren().addAll(nodes.getChildren());
    }


}
