package com.jackgharris.cosc2288.a2.controllers.components;

import com.jackgharris.cosc2288.a2.core.MyHealth;
import com.jackgharris.cosc2288.a2.utility.ColorUtility;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;


public class SettingsThemeController {

    //Primary Button
    @FXML
    private ColorPicker primaryButtonBackgroundPicker;
    @FXML
    private ColorPicker primaryButtonHoverPicker;
    @FXML
    private ColorPicker primaryButtonTextPicker;

    @FXML
    private ColorPicker dangerButtonBackgroundPicker;
    @FXML
    private ColorPicker dangerButtonHoverPicker;

    @FXML
    private ColorPicker dangerButtonTextPicker;

    @FXML
    private ColorPicker appBackgroundColorPicker;

    @FXML
    private ColorPicker appBackgroundColorSecondaryPicker;

    @FXML
    private Label themeSaveStatus;


    public void initialize(){

        //Primary button
        this.primaryButtonBackgroundPicker.setValue(Color.web(MyHealth.getInstance().getUser().getTheme().getColor("-fx-button-primary-background")));
        this.primaryButtonHoverPicker.setValue(Color.web(MyHealth.getInstance().getUser().getTheme().getColor("-fx-button-primary-background-hover")));
        this.primaryButtonTextPicker.setValue(Color.web(MyHealth.getInstance().getUser().getTheme().getColor("-fx-button-primary-text")));

        //Danger button
        this.dangerButtonBackgroundPicker.setValue(Color.web(MyHealth.getInstance().getUser().getTheme().getColor("-fx-button-danger-background")));
        this.dangerButtonHoverPicker.setValue(Color.web(MyHealth.getInstance().getUser().getTheme().getColor("-fx-button-danger-background-hover")));
        this.dangerButtonTextPicker.setValue(Color.web(MyHealth.getInstance().getUser().getTheme().getColor("-fx-button-danger-text")));

        this.appBackgroundColorPicker.setValue(Color.web(MyHealth.getInstance().getUser().getTheme().getColor("-fx-background-primary")));
        this.appBackgroundColorSecondaryPicker.setValue(Color.web(MyHealth.getInstance().getUser().getTheme().getColor("-fx-background-secondary")));

        this.updateThemeSavedStatus();

    }

    private void updateThemeSavedStatus(){
        if(MyHealth.getInstance().getUser().getTheme().isSaved()){
            this.themeSaveStatus.setText("Theme Settings Upto Date");
            this.themeSaveStatus.getStyleClass().remove("alert-danger");
            this.themeSaveStatus.getStyleClass().add("alert-success");
        }else{
            this.themeSaveStatus.setText("Theme NOT Saved");
            this.themeSaveStatus.getStyleClass().remove("alert-success");
            this.themeSaveStatus.getStyleClass().add("alert-danger");
        }
    }

    public void updatePrimaryBackground(){
        MyHealth.getInstance().getUser().getTheme().setColor("-fx-button-primary-background",ColorUtility.getHexStringFromColorPicker(this.primaryButtonBackgroundPicker));
        this.updateThemeSavedStatus();
    }

    public void updatePrimaryHover(){
        MyHealth.getInstance().getUser().getTheme().setColor("-fx-button-primary-background-hover",ColorUtility.getHexStringFromColorPicker(this.primaryButtonHoverPicker));
        this.updateThemeSavedStatus();
    }

    public void updatePrimaryText(){
        MyHealth.getInstance().getUser().getTheme().setColor("-fx-button-primary-text",ColorUtility.getHexStringFromColorPicker(this.primaryButtonTextPicker));
        this.updateThemeSavedStatus();
    }

    public void updateDangerBackground(){
        MyHealth.getInstance().getUser().getTheme().setColor("-fx-button-danger-background",ColorUtility.getHexStringFromColorPicker(this.dangerButtonBackgroundPicker));
        this.updateThemeSavedStatus();
    }

    public void updateDangerHover(){
        MyHealth.getInstance().getUser().getTheme().setColor("-fx-button-danger-background-hover",ColorUtility.getHexStringFromColorPicker(this.dangerButtonHoverPicker));
        this.updateThemeSavedStatus();
    }

    public void updateDangerText(){
        MyHealth.getInstance().getUser().getTheme().setColor("-fx-button-danger-text",ColorUtility.getHexStringFromColorPicker(this.dangerButtonTextPicker));
        this.updateThemeSavedStatus();
    }

    public void updateAppBackgroundPrimary(){
        MyHealth.getInstance().getUser().getTheme().setColor("-fx-background-primary",ColorUtility.getHexStringFromColorPicker(this.appBackgroundColorPicker));
        this.updateThemeSavedStatus();
    }

    public void updateAppBackgroundSecondary(){
        MyHealth.getInstance().getUser().getTheme().setColor("-fx-background-secondary",ColorUtility.getHexStringFromColorPicker(this.appBackgroundColorSecondaryPicker));
        this.updateThemeSavedStatus();
    }


    public void saveChanges(){
        MyHealth.getInstance().getUser().getTheme().save();
        this.updateThemeSavedStatus();
    }

    public void discardChanges(){

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Resetting MyHealth Theme to default");
        alert.setContentText("Are you sure you want to reset the theme to default?");
        ButtonType okButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
        alert.getButtonTypes().setAll(okButton, noButton);
        alert.showAndWait().ifPresent(type -> {

            if (type == okButton) {
                MyHealth.getInstance().getUser().getTheme().reset(true);
                this.updateThemeSavedStatus();
            }
        });
    }





}
