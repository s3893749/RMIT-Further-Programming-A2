package com.jackgharris.cosc2288.a2.controllers.components;

import com.jackgharris.cosc2288.a2.core.EasyImage;
import com.jackgharris.cosc2288.a2.core.MyHealth;
import com.jackgharris.cosc2288.a2.utility.FXMLUtility;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
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

    @FXML
    private Button accountSettingsButton;

    public static SettingsMenuController instance;


    public void initialize() throws IOException {
        this.updateUserDetails();
        this.accountSettingsButton.setStyle("-fx-background-color: -fx-button-primary-background-hover");
        SettingsMenuController.instance = this;
    }

    public void updateUserDetails(){
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

    public void switchToThemeSettings(ActionEvent event) throws IOException {
        System.out.println("Switching to theme");
        this.setActiveButton((Button) event.getSource());
        FXMLLoader loader = new FXMLLoader(FXMLUtility.settingsThemePage);
        AnchorPane nodes = loader.load();


        this.contentContainer.getChildren().clear();
        this.contentContainer.getChildren().addAll(nodes.getChildren());;
    }

    public void switchToImportExport(ActionEvent event) throws IOException {
        System.out.println("Switching to import/export");
        this.setActiveButton((Button) event.getSource());
        FXMLLoader loader = new FXMLLoader(FXMLUtility.settingsImportExportPage);
        AnchorPane nodes = loader.load();


        this.contentContainer.getChildren().clear();
        this.contentContainer.getChildren().addAll(nodes.getChildren());;
    }

    public void setSettingsContentOuterContainer(AnchorPane container) throws IOException {
        this.contentContainer = container;
        FXMLLoader loader = new FXMLLoader(FXMLUtility.settingsAccountPage);
        AnchorPane nodes = loader.load();


        this.contentContainer.getChildren().clear();
        this.contentContainer.getChildren().addAll(nodes.getChildren());
    }

    private void setActiveButton(Button button){

        this.settingsMenu.getChildren().forEach((n)->{
            if(n == button){
                button.setStyle("-fx-background-color:  -fx-button-primary-background-hover");
            }else{
                n.setStyle("");
            }
        });
    }

    public void setProfileImage(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Please select your profile picture!");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Portable Network Graphics","*.png"));
        File photo = fileChooser.showOpenDialog(MyHealth.getStageById("registration"));
        if(photo !=null){

            EasyImage selectedImage =  new EasyImage(new Image(photo.getAbsolutePath(),200,200, false, true));

            MyHealth.getInstance().getUser().setProfileImage(EasyImage.serialize(selectedImage));

            if(!MyHealth.getInstance().getUser().updateDetails()){
                System.out.println("Failed to save user details :(");
            }else{
                System.out.println("Details saved!");
            }

            this.profileImage.setImage(MyHealth.getInstance().getUser().getProfileImage());

        }
    }


}
