package com.jackgharris.cosc2288.a2.controllers.components;

import com.jackgharris.cosc2288.a2.core.MyHealth;
import com.jackgharris.cosc2288.a2.models.User;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

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

    public void initialize(){

        User user = MyHealth.getInstance().getUser();

        this.firstNameInput.setText(user.getFirstname());
        this.lastNameInput.setText(user.getSurname());
        this.emailInput.setText(user.getEmail());

    }

}
