package com.jackgharris.cosc2288.a2.controllers;

import com.jackgharris.cosc2288.a2.core.EasyImage;
import com.jackgharris.cosc2288.a2.core.MyHealth;
import com.jackgharris.cosc2288.a2.models.User;
import com.jackgharris.cosc2288.a2.utility.FXMLUtility;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;

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

    @FXML
    ImageView photoPreview;



    public void backButtonPress(ActionEvent event) throws IOException {
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.setScene(FXMLUtility.loadScene(FXMLUtility.loginFXML,stage, MyHealth.launcherCSS));
        stage.getProperties().put("id","launcher");
        stage.show();
    }

    public void registerButtonPress(ActionEvent event){
        System.out.println("Register Button Pressed!");

        User user = new User(0,usernameInputField.getText(), firstNameInputField.getText(),lastNameInputField.getText(),emailInputField.getText(), passwordInputField.getText(), EasyImage.serialize(new EasyImage(this.photoPreview.getImage())));

        User.add(user);

    }

    public void setPictureButtonPress(ActionEvent event){

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Please select your profile picture!");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Portable Network Graphics","*.png"));
        File photo = fileChooser.showOpenDialog(MyHealth.getStageById("registration"));

        EasyImage selectedImage =  new EasyImage(new Image(photo.getAbsolutePath(),200,200, false, true));

        this.photoPreview.setImage(selectedImage.getImage());
    }
}
