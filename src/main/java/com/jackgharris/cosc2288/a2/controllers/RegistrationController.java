package com.jackgharris.cosc2288.a2.controllers;

import com.jackgharris.cosc2288.a2.core.MyHealth;
import com.jackgharris.cosc2288.a2.utility.FXMLUtility;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class RegistrationController {

    @FXML
    TextField firstNameInputField;

    @FXML
    TextField lastNameInputField;

    @FXML
    TextField emailInputField;

    @FXML
    TextField usernameInputField;

    @FXML
    TextField passwordInputField;

    @FXML
    TextField passwordConfirmationInputField;

    @FXML
    Label registrationFailedError;



    public void backButtonPress(ActionEvent event) throws IOException {
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.setScene(FXMLUtility.loadScene(FXMLUtility.loginFXML,stage, MyHealth.launcherCSS));
        stage.show();
    }

    public void registerButtonPress(ActionEvent event){
        System.out.println("Register Button Pressed!");
        this.registrationFailedError.setText("Registration function not yet added");


        this.registrationFailedError.getStyleClass().remove("notification-hidden");
        this.firstNameInputField.getStyleClass().add("text-field-error");
        this.lastNameInputField.getStyleClass().add("text-field-error");
        this.emailInputField.getStyleClass().add("text-field-error");
        this.usernameInputField.getStyleClass().add("text-field-error");
        this.passwordInputField.getStyleClass().add("text-field-error");
        this.passwordConfirmationInputField.getStyleClass().add("text-field-error");

    }

    public void setPictureButtonPress(ActionEvent event){
        this.registrationFailedError.getStyleClass().remove("notification-hidden");
        this.registrationFailedError.setText("Set Picture feature not yet added");
    }
}
