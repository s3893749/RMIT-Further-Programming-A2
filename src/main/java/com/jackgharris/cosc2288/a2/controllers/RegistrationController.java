package com.jackgharris.cosc2288.a2.controllers;

import com.jackgharris.cosc2288.a2.core.MyHealth;
import com.jackgharris.cosc2288.a2.utility.FXMLUtility;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

import java.io.IOException;

public class RegistrationController {


    public RegistrationController(){
        System.out.println("Registration Controller Created!");
    }

    public void backButton(ActionEvent event) throws IOException {
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.setScene(FXMLUtility.loadScene(FXMLUtility.loginFXML,stage, MyHealth.css));
        stage.show();
    }

    public void registerButton(ActionEvent event){
        System.out.println("Register Button Pressed!");

    }
}
