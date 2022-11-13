package com.jackgharris.cosc2288.a2.controllers.components;

import com.jackgharris.cosc2288.a2.core.MyHealth;
import com.jackgharris.cosc2288.a2.utility.FXMLUtility;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class SettingsMenuController {


    @FXML
    private ImageView profileImage;

    @FXML
    private Label profileName;

    @FXML
    private Label profileEmail;

    @FXML
    private VBox settingsMenu;

    private AnchorPane contentContainer;


    public void initialize(){
        this.profileImage.setImage(MyHealth.getInstance().getUser().getProfileImage());
        this.profileName.setText(MyHealth.getInstance().getUser().getFirstname()+" "+MyHealth.getInstance().getUser().getSurname());
        this.profileEmail.setText(MyHealth.getInstance().getUser().getEmail());
    }

    public void switchToAccountSettings(ActionEvent event) throws IOException {

        System.out.println("Switching to account");
        this.setActiveButton((Button) event.getSource());

        FXMLLoader loader = new FXMLLoader(FXMLUtility.settingsAccountPage);
        AnchorPane nodes = loader.load();


        this.contentContainer.getChildren().clear();
        this.contentContainer.getChildren().addAll(nodes.getChildren());
    }

    public void switchToThemeSettings(ActionEvent event){
        System.out.println("Switching to theme");
        this.setActiveButton((Button) event.getSource());

        this.contentContainer.getChildren().clear();
    }

    public void switchToImportExport(ActionEvent event){
        System.out.println("Switching to import/export");
        this.setActiveButton((Button) event.getSource());

        this.contentContainer.getChildren().clear();
    }

    public void setSettingsContentOuterContainer(AnchorPane container){
        this.contentContainer = container;
    }

    private void setActiveButton(Button button){

        this.settingsMenu.getChildren().forEach((n)->{
            if(n == button){
                button.setStyle("-fx-background-color:  #b20844");
            }else{
                n.setStyle("");
            }
        });
    }


}
