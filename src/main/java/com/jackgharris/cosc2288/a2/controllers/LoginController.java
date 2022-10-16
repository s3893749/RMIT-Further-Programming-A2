package com.jackgharris.cosc2288.a2.controllers;

import com.jackgharris.cosc2288.a2.core.MyHealth;
import com.jackgharris.cosc2288.a2.utility.FXMLUtility;
import com.jackgharris.cosc2288.a2.utility.Resource;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;



public class LoginController {

    @FXML
    private TextField emailInputField;

    @FXML
    private TextField passwordInputField;

    @FXML
    private Label loginErrorLabel;


    public void registerButtonPress(ActionEvent event) throws IOException {
        System.out.println("Back Button Pressed!");
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.setScene(FXMLUtility.loadScene(FXMLUtility.registrationFXML,stage, MyHealth.launcherCSS));
        stage.getProperties().put("id","registration");
        stage.show();
    }

    public void loginButtonPress(ActionEvent event) throws IOException {
        System.out.println("Login Button Pressed!");
        System.out.println("Email = "+this.emailInputField.getText());
        System.out.println("Password = "+this.passwordInputField.getText());

        if(!this.emailInputField.getText().equals("example@example.com")){

            this.loginErrorLabel.getStyleClass().remove("notification-hidden");
            this.emailInputField.getStyleClass().add("text-field-error");
            this.passwordInputField.getStyleClass().add("text-field-error");

        }else{
            Stage dashboardStage = new Stage(StageStyle.DECORATED);
            Stage launcherStage = (Stage)((Node) event.getSource()).getScene().getWindow();


            dashboardStage.setScene(FXMLUtility.loadScene(FXMLUtility.dashboardFXML,launcherStage, MyHealth.appCSS));
            dashboardStage.setResizable(true);
            dashboardStage.setMaximized(true);
            dashboardStage.setTitle(MyHealth.title);
            dashboardStage.getIcons().add(Resource.favicon());
            dashboardStage.show();
            dashboardStage.getProperties().put("id","dashboard");
            launcherStage.hide();
        }
    }

    public void closeButtonPress(ActionEvent event){
        System.out.println("Closing application");
        System.exit(0);
    }

    public void helpButtonPress(ActionEvent event){
        System.out.println("Help button pressed!");
    }

}
