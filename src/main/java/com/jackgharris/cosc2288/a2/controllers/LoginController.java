package com.jackgharris.cosc2288.a2.controllers;

import com.jackgharris.cosc2288.a2.core.MyHealth;
import com.jackgharris.cosc2288.a2.utility.FXMLUtility;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

import java.io.IOException;


public class LoginController {

    public LoginController(){
        System.out.println("Registration Controller Created!");
    }


    public void registerButton(ActionEvent event) throws IOException {
        System.out.println("Back Button Pressed!");
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.setScene(FXMLUtility.loadScene(FXMLUtility.registrationFXML,stage, MyHealth.css));
        stage.show();
    }

    public void loginButton(ActionEvent event){
        System.out.println("Login Button Pressed!");
    }

    public void closeButton(ActionEvent event){
        System.out.println("Closing application");
        System.exit(0);
    }

    public void helpButton(ActionEvent event){
        System.out.println("Help button pressed!");
    }

}
