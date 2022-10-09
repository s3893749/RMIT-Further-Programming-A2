package com.jackgharris.cosc2288.a2.core;

import com.jackgharris.cosc2288.a2.utility.FXMLUtility;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;

public class MyHealth extends javafx.application.Application {

    public static final String css = Objects.requireNonNull(MyHealth.class.getResource("login.css")).toExternalForm();

    @Override
    public void start(Stage stage) throws IOException {

        stage.setScene(FXMLUtility.loadScene(FXMLUtility.loginFXML, stage, MyHealth.css));

        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setResizable(false);

        stage.show();
    }

    public static void main(String[] args) {
        new MyHealth();
        launch();
    }
}