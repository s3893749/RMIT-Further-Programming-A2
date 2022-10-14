package com.jackgharris.cosc2288.a2.core;

import com.jackgharris.cosc2288.a2.utility.FXMLUtility;
import com.jackgharris.cosc2288.a2.utility.Resource;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;

public class MyHealth extends javafx.application.Application {



    public static final String launcherCSS = Objects.requireNonNull(MyHealth.class.getResource("login.css")).toExternalForm();
    public static final String appCSS = Objects.requireNonNull(MyHealth.class.getResource("app.css")).toExternalForm();

    public static final String title = "My Health v0.1";

    private static Theme theme;



    public MyHealth(){
        System.out.println("MyHealth object created!");
        MyHealth.theme = new Theme("#b463ae",null,null,null,null);

        Database.table("test");
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

    public static Theme getTheme(){
        return MyHealth.theme;
    }
}