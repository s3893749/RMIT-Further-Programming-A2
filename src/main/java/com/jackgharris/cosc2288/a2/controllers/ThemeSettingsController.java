package com.jackgharris.cosc2288.a2.controllers;

import com.jackgharris.cosc2288.a2.core.MyHealth;
import com.jackgharris.cosc2288.a2.utility.ColorUtility;
import com.jackgharris.cosc2288.a2.utility.FXMLUtility;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class ThemeSettingsController {

    @FXML
    private ColorPicker backgroundColorPicker;
    @FXML
    private ColorPicker menuColorPicker;

    @FXML
    private ColorPicker buttonBackgroundColorPicker;

    @FXML
    private ColorPicker buttonTextColorPicker;

    @FXML
    private AnchorPane settings;

    @FXML
    private HBox header;

    @FXML
    public void initialize(){
        backgroundColorPicker.setValue(Color.valueOf(MyHealth.getTheme().getBackgroundColor()));
        menuColorPicker.setValue(Color.valueOf(MyHealth.getTheme().getMenuColor()));
    }

    public void backButton(ActionEvent event) throws IOException {
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Preferences");
        stage.setScene(FXMLUtility.loadScene(FXMLUtility.settingsFXML,stage, MyHealth.appCSS));
        stage.show();
    }

    public void updateBackgroundColor(ActionEvent event){
        MyHealth.getTheme().setBackgroundColor(ColorUtility.getHexStringFromColorPicker(this.backgroundColorPicker));
        settings.setStyle("-fx-background-color: "+MyHealth.getTheme().getBackgroundColor());
        MyHealth.getTheme().updateAppColors();
    }

    public void updateMenuColor(ActionEvent event){
        MyHealth.getTheme().setMenuColor(ColorUtility.getHexStringFromColorPicker(this.menuColorPicker));
        header.setStyle("-fx-background-color: "+MyHealth.getTheme().getMenuColor());
        MyHealth.getTheme().updateAppColors();
    }
}
