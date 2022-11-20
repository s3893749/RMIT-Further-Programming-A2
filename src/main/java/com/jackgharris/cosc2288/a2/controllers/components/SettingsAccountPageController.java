package com.jackgharris.cosc2288.a2.controllers.components;

import com.jackgharris.cosc2288.a2.core.MyHealth;
import com.jackgharris.cosc2288.a2.models.Activity;
import com.jackgharris.cosc2288.a2.models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.apache.commons.validator.routines.EmailValidator;

public class SettingsAccountPageController {

    @FXML
    private TextField firstNameInput;

    @FXML
    private TextField lastNameInput;

    @FXML
    private TextField emailInput;

    @FXML
    private TextField currentPasswordInput;

    @FXML
    private TextField newPasswordInput;

    @FXML
    private TextField newPasswordConfirmationInput;

    @FXML
    private Button discardChangesButton;

    @FXML
    private Button discardPasswordChangesButton;


    @FXML
    private Label passwordSavedStatus;

    @FXML
    private Label personalDetailsSavedStatus;


    private boolean personalDetailsSaved;

    @FXML
    private Button savePersonalDetailsButton;

    private boolean passwordSaved;

    private boolean validCurrentPassword;

    @FXML
    private Button savePasswordButton;

    public void initialize(){

        User user = MyHealth.getInstance().getUser();

        this.firstNameInput.setText(user.getFirstname());
        this.lastNameInput.setText(user.getSurname());
        this.emailInput.setText(user.getEmail());

        this.savePasswordButton.setDisable(true);
        this.discardPasswordChangesButton.setDisable(true);

        this.newPasswordInput.setDisable(true);
        this.newPasswordConfirmationInput.setDisable(true);

        this.personalDetailsSaved = true;
        this.passwordSaved = true;

        this.updateSavedStatus();
    }

    private void updateSavedStatus(){

        if(!personalDetailsSaved) {
            this.personalDetailsSavedStatus.setText("Personal Details NOT Saved");
            this.personalDetailsSavedStatus.getStyleClass().add("alert-danger");
            this.personalDetailsSavedStatus.getStyleClass().removeAll("alert-success");
        }else{
            this.personalDetailsSavedStatus.setText("Personal Details Upto Date");
            this.personalDetailsSavedStatus.getStyleClass().removeAll("alert-danger");
            this.personalDetailsSavedStatus.getStyleClass().add("alert-success");
        }

        if(!passwordSaved){
            this.passwordSavedStatus.setText("New Password NOT Saved");
            this.passwordSavedStatus.getStyleClass().removeAll("alert-success");
            this.passwordSavedStatus.getStyleClass().add("alert-danger");
        }else{
            this.passwordSavedStatus.setText("Password Upto Date");
            this.passwordSavedStatus.getStyleClass().removeAll("alert-danger");
            this.passwordSavedStatus.getStyleClass().add("alert-success");
        }
    }

    public void discardChanges(){
        User user = MyHealth.getInstance().getUser();

        this.firstNameInput.setText(user.getFirstname());
        this.lastNameInput.setText(user.getSurname());
        this.emailInput.setText(user.getEmail());

        this.firstNameInput.getStyleClass().removeAll("text-field-error");
        this.lastNameInput.getStyleClass().removeAll("text-field-error");
        this.emailInput.getStyleClass().removeAll("text-field-error");


        this.personalDetailsSaved = true;
        this.updateSavedStatus();
    }

    public void personalDetailsInputChanged(){
        this.personalDetailsSaved = false;


        if(!EmailValidator.getInstance().isValid(this.emailInput.getText())){
            this.emailInput.getStyleClass().add("text-field-error");

        }else{
            this.emailInput.getStyleClass().removeAll("text-field-error");
        }

        if(this.firstNameInput.getText().trim().isBlank()){
            this.firstNameInput.getStyleClass().add("text-field-error");
        }else{
            this.firstNameInput.getStyleClass().removeAll("text-field-error");
        }

        if(this.lastNameInput.getText().trim().isBlank()){
            this.lastNameInput.getStyleClass().add("text-field-error");
        }else{
            this.lastNameInput.getStyleClass().removeAll("text-field-error");
        }


        this.updateSavedStatus();
    }

    public void savePersonalDetails(){

        int failures = 0;

        if(!EmailValidator.getInstance().isValid(this.emailInput.getText())){
            failures ++;
        }

        if(this.firstNameInput.getText().trim().isBlank()){
            failures ++;
        }

        if(this.lastNameInput.getText().trim().isBlank()){
            failures ++;
        }

        if(failures == 0){
            User user = MyHealth.getInstance().getUser();
            user.setFirstname(this.firstNameInput.getText());
            user.setLastname(this.lastNameInput.getText());
            user.setEmail(this.emailInput.getText());
            if(user.updateDetails()){
                this.personalDetailsSaved = true;
                this.updateSavedStatus();
                SettingsMenuController.instance.updateUserDetails();
            }else{
                this.personalDetailsSaved = false;
                this.personalDetailsSavedStatus.getStyleClass().add("alert-danger");
                this.personalDetailsSavedStatus.setText("Technical error, cannot save user details to SQL at this time");
            }

        }else{
            this.personalDetailsSavedStatus.getStyleClass().add("alert-danger");
            this.personalDetailsSavedStatus.setText("Cannot save personal details! Please ensure all entries are correct");
        }


    }

    public void currentPasswordEntered(){
        if(User.hash(this.currentPasswordInput.getText()).equals(MyHealth.getInstance().getUser().getPassword())){
            this.currentPasswordInput.getStyleClass().removeAll("text-field-error");
            this.currentPasswordInput.getStyleClass().add("text-field-success");
            this.newPasswordInput.setDisable(false);
            this.newPasswordConfirmationInput.setDisable(false);
            this.validCurrentPassword = true;
        }else{
            this.currentPasswordInput.getStyleClass().removeAll("text-field-success");
            this.currentPasswordInput.getStyleClass().add("text-field-error");
            this.newPasswordInput.setDisable(true);
            this.newPasswordConfirmationInput.setDisable(true);
            this.validCurrentPassword = false;
        }
    }

    public void newPasswordEntered(){

        this.passwordSaved = false;
        this.updateSavedStatus();
        this.discardPasswordChangesButton.setDisable(false);

        if(this.newPasswordInput.getText().equals(this.newPasswordConfirmationInput.getText()) && newPasswordInput.getText().length() >= 8){

            this.newPasswordInput.getStyleClass().removeAll("text-field-error");
            this.newPasswordInput.getStyleClass().add("text-field-success");

            this.newPasswordConfirmationInput.getStyleClass().removeAll("text-field-error");
            this.newPasswordConfirmationInput.getStyleClass().add("text-field-success");

            this.savePasswordButton.setDisable(false);



        }else{
            this.newPasswordInput.getStyleClass().removeAll("text-field-success");
            this.newPasswordConfirmationInput.getStyleClass().removeAll("text-field-success");
            this.newPasswordConfirmationInput.getStyleClass().add("text-field-error");
            this.newPasswordInput.getStyleClass().add("text-field-error");

            this.savePasswordButton.setDisable(true);


        }
    }

    public void discardPasswordChange(){
        this.savePasswordButton.setDisable(true);

        this.passwordSaved = true;
        this.updateSavedStatus();
        this.validCurrentPassword = false;
        this.currentPasswordInput.setText(null);
        this.newPasswordInput.setText(null);
        this.newPasswordConfirmationInput.setText(null);

        this.newPasswordConfirmationInput.getStyleClass().removeAll("text-field-success");
        this.newPasswordConfirmationInput.getStyleClass().removeAll("text-field-danger");
        this.newPasswordConfirmationInput.setDisable(true);

        this.newPasswordInput.getStyleClass().removeAll("text-field-success");
        this.newPasswordInput.getStyleClass().removeAll("text-field-danger");
        this.newPasswordInput.setDisable(true);

        this.currentPasswordInput.getStyleClass().removeAll("text-field-success");
        this.currentPasswordInput.getStyleClass().removeAll("text-field-danger");

        this.passwordSavedStatus.setText("Password change cancelled");
        this.discardPasswordChangesButton.setDisable(true);

    }

    public void savePassword(){
        User user = MyHealth.getInstance().getUser();
        user.setPassword(User.hash(this.newPasswordInput.getText()));
        user.updateDetails();
        Activity.add(new Activity("Password updated"));

        this.savePasswordButton.setDisable(true);

        this.passwordSaved = true;
        this.updateSavedStatus();
        this.validCurrentPassword = false;
        this.currentPasswordInput.setText(null);
        this.newPasswordInput.setText(null);
        this.newPasswordConfirmationInput.setText(null);

        this.newPasswordConfirmationInput.getStyleClass().removeAll("text-field-success");
        this.newPasswordConfirmationInput.getStyleClass().removeAll("text-field-danger");
        this.newPasswordConfirmationInput.setDisable(true);

        this.newPasswordInput.getStyleClass().removeAll("text-field-success");
        this.newPasswordInput.getStyleClass().removeAll("text-field-danger");
        this.newPasswordInput.setDisable(true);

        this.currentPasswordInput.getStyleClass().removeAll("text-field-success");
        this.currentPasswordInput.getStyleClass().removeAll("text-field-danger");

        this.passwordSavedStatus.setText("new Password Saved");
        this.discardPasswordChangesButton.setDisable(true);
    }

}
