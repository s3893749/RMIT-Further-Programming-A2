package com.jackgharris.cosc2288.a2.controllers;

import com.jackgharris.cosc2288.a2.core.EasyImage;
import com.jackgharris.cosc2288.a2.core.MyHealth;
import com.jackgharris.cosc2288.a2.models.User;
import com.jackgharris.cosc2288.a2.utility.FXMLUtility;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.validator.routines.EmailValidator;

import java.io.*;

public class RegistrationController {

    @FXML
    TextField firstNameInputField;

    @FXML
    TextField lastNameInputField;

    @FXML
    TextField emailInputField;

    @FXML
    TextField passwordInputField;

    @FXML
    TextField passwordConfirmationInputField;

    @FXML
    Label registrationFailedError;

    @FXML
    ImageView photoPreview;

    private boolean notification;



    public void backButtonPress(ActionEvent event) throws IOException {
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.getProperties().put("id","launcher");
        FXMLLoader loader = new FXMLLoader(FXMLUtility.loginFXML);
        stage.setScene(new Scene(loader.load()));
        Scene scene = stage.getScene();
        scene.setFill(Color.TRANSPARENT);
        scene.getStylesheets().add(MyHealth.launcherCSS);

        scene.setOnMousePressed(pressEvent ->{
            scene.setOnMouseDragged(dragEvent ->{
                stage.setX(dragEvent.getScreenX() - pressEvent.getSceneX());
                stage.setY(dragEvent.getScreenY() - pressEvent.getSceneY());
            });
        });
        LoginController loginController = loader.getController();
        if(this.notification){
            loginController.setNotification("Account '"+this.firstNameInputField.getText()+" "+this.lastNameInputField.getText()+"' was successfully created");
        }
        stage.show();

    }

    public void registerButtonPress(ActionEvent event) throws IOException {
        int failedFields =0;

        if(this.firstNameInputField.getText().isEmpty()){
            this.registrationFailedError.setText("Firstname cannot be empty");
            this.registrationFailedError.getStyleClass().remove("notification-hidden");
            this.firstNameInputField.getStyleClass().add("text-field-error");
            failedFields++;
        }else{
            this.firstNameInputField.getStyleClass().remove("text-field-error");
        }

        if(this.lastNameInputField.getText().isEmpty()){
            this.registrationFailedError.setText("Lastname cannot be empty");
            this.registrationFailedError.getStyleClass().remove("notification-hidden");
            this.lastNameInputField.getStyleClass().add("text-field-error");
            failedFields++;
        }else{
            this.lastNameInputField.getStyleClass().remove("text-field-error");
        }

        if(!EmailValidator.getInstance().isValid(this.emailInputField.getText())){
            this.registrationFailedError.setText("Invalid email address provided");
            this.registrationFailedError.getStyleClass().remove("notification-hidden");
            this.emailInputField.getStyleClass().add("text-field-error");
            failedFields++;
        }else{
            this.emailInputField.getStyleClass().remove("text-field-error");
        }

        if(this.passwordInputField.getText().length() < 8 || !this.passwordConfirmationInputField.getText().equals(this.passwordInputField.getText())){
            this.registrationFailedError.setText("Password must be 8 characters long and match confirmation!");
            this.registrationFailedError.getStyleClass().remove("notification-hidden");
            this.passwordInputField.getStyleClass().add("text-field-error");
            this.passwordConfirmationInputField.getStyleClass().add("text-field-error");
            failedFields++;
        }


        if(User.getByEmail(this.emailInputField.getText()) != null){
            this.registrationFailedError.setText("Email address already in use! Please select a different address");
            this.registrationFailedError.getStyleClass().remove("notification-hidden");
            this.emailInputField.getStyleClass().add("text-field-error");
            failedFields++;
        }else{
            this.emailInputField.getStyleClass().add("notification-hidden");
        }



        if(failedFields == 0){
            User user = new User(0,this.emailInputField.getText().split("@")[0], firstNameInputField.getText(),lastNameInputField.getText(),emailInputField.getText(), passwordInputField.getText(), EasyImage.serialize(new EasyImage(this.photoPreview.getImage())), "false",null);
            User.add(user);
            this.notification = true;

            this.backButtonPress(event);
        }else{
            if(failedFields > 1){
                this.registrationFailedError.setText("Registration failed! multiple fields have invalid inputs");
            }
        }


    }

    public void setPictureButtonPress(ActionEvent event){

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Please select your profile picture!");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Portable Network Graphics","*.png"));
        File photo = fileChooser.showOpenDialog(MyHealth.getStageById("registration"));
        if(photo !=null){
            EasyImage selectedImage =  new EasyImage(new Image(photo.getAbsolutePath(),200,200, false, true));
            this.photoPreview.setImage(selectedImage.getImage());

        }
    }
}
