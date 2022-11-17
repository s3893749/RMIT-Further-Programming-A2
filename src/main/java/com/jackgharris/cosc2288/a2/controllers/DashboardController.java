package com.jackgharris.cosc2288.a2.controllers;

import com.jackgharris.cosc2288.a2.core.MyHealth;
import com.jackgharris.cosc2288.a2.utility.FXMLUtility;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class DashboardController{

    @FXML
    private AnchorPane parent;

    @FXML
    private AnchorPane menuContainerOuter;


    @FXML
    public void initialize() throws IOException {
        //Load the menu
        FXMLLoader menuLoader = new FXMLLoader(FXMLUtility.menu);
        AnchorPane menuNodes = menuLoader.load();
        this.menuContainerOuter.getChildren().add(menuNodes.getChildren().get(0));

        FXMLLoader loader = new FXMLLoader(FXMLUtility.recentOverview);
        AnchorPane nodes = loader.load();

        AnchorPane contentOuterContainer = (AnchorPane) this.parent.getChildren().get(1);
        contentOuterContainer.getChildren().addAll(nodes.getChildren());


        //detect and apply theme changes
        this.parent.setStyle(MyHealth.getInstance().getUser().getTheme().getStyle());
        MyHealth.getInstance().setParent(this.parent);
    }


}
