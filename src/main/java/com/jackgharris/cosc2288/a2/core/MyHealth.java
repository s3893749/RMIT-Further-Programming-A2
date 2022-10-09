package com.jackgharris.cosc2288.a2.core;

import com.jackgharris.cosc2288.a2.models.User;
import com.jackgharris.cosc2288.a2.utility.FXMLUtility;
import com.jackgharris.cosc2288.a2.utility.Resource;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

public class MyHealth extends javafx.application.Application {

    public static final String launcherCSS = Objects.requireNonNull(MyHealth.class.getResource("login.css")).toExternalForm();
    public static final String appCSS = Objects.requireNonNull(MyHealth.class.getResource("app.css")).toExternalForm();

    public static final String title = "My Health v0.1";


    public MyHealth(){
        System.out.println("MyHealth object created!");
        HashMap<String, String> userData = new HashMap<>();
        userData.put("username","st_tuff");
        userData.put("email","st_tuff@me.com");
        userData.put("password","Pa55w0rd");
        userData.put("firstname","Jack");
        userData.put("surname","Harris");
        userData.put("profileImage","BLANK");

        User[] users = new User[1];
        users[0] = new User(userData);
        User.bindUserData(users);

    }

    @Override
    public void start(Stage stage) throws IOException {
        System.out.println("Starting GUI");

        stage.setScene(FXMLUtility.loadScene(FXMLUtility.loginFXML, stage, MyHealth.launcherCSS));

        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setResizable(false);
        stage.getIcons().add(Resource.favicon());

        stage.show();
    }

    public static void main(String[] args) {

        launch();
    }
}