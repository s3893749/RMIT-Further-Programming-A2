package com.jackgharris.cosc2288.a2.controllers;

import com.jackgharris.cosc2288.a2.core.MyHealth;
import com.jackgharris.cosc2288.a2.models.Activity;
import com.jackgharris.cosc2288.a2.models.User;
import com.jackgharris.cosc2288.a2.utility.FXMLUtility;
import com.jackgharris.cosc2288.a2.utility.Resource;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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

    public void loginButtonPress() throws IOException {
        System.out.println("Login Button Pressed!");
        System.out.println("Email = "+this.emailInputField.getText());
        System.out.println("Password = "+this.passwordInputField.getText());

        if(!User.login(this.emailInputField.getText(), this.passwordInputField.getText())){

            this.loginErrorLabel.getStyleClass().remove("notification-hidden");
            this.emailInputField.getStyleClass().add("text-field-error");
            this.passwordInputField.getStyleClass().add("text-field-error");

        }else{

            MyHealth.getInstance().setUser(User.getByEmail(this.emailInputField.getText()));
            Activity.add(new Activity("Logged in"));

            Stage dashboardStage = new Stage();

            FXMLLoader fxmlLoader = new FXMLLoader(FXMLUtility.dashboardFXML);
            dashboardStage.setScene(new Scene(fxmlLoader.load()));
            dashboardStage.getProperties().put("id","dashboard");
            dashboardStage.getProperties().put("controller",fxmlLoader.getController());
            dashboardStage.setResizable(true);
            dashboardStage.setMaximized(true);
            dashboardStage.setTitle(MyHealth.title);
            dashboardStage.getIcons().add(Resource.favicon());
            dashboardStage.show();

            MyHealth.getStageById("launcher").close();
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
