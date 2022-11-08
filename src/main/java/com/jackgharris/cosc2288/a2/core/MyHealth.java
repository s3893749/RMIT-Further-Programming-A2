package com.jackgharris.cosc2288.a2.core;

import com.jackgharris.cosc2288.a2.models.Record;
import com.jackgharris.cosc2288.a2.models.User;
import com.jackgharris.cosc2288.a2.utility.FXMLUtility;
import com.jackgharris.cosc2288.a2.utility.Resource;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class MyHealth extends javafx.application.Application {

    public static final String launcherCSS = Objects.requireNonNull(MyHealth.class.getResource("login.css")).toExternalForm();
    public static final String appCSS = Objects.requireNonNull(MyHealth.class.getResource("app.css")).toExternalForm();

    public static final String title = "My Health v0.1";

    private String selectedRecordType;

    public static HashMap<String, Object> data = new HashMap<>();

    private User user;

    private Theme theme;

    private static MyHealth instance;

    private String encryptionPepper;

    public MyHealth(){
        MyHealth.instance = this;
        this.selectedRecordType = null;
        this.encryptionPepper = "Pepper&SteakPie";
        this.theme = new Theme("#363742","#444550",null,null,null);
    }

    @Override
    public void start(Stage stage) throws IOException {
        System.out.println("Starting GUI");

        stage.setScene(FXMLUtility.loadScene(FXMLUtility.loginFXML, stage, MyHealth.launcherCSS));

        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setResizable(false);
        stage.getIcons().add(Resource.favicon());
        stage.getProperties().put("id","launcher");

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public Theme getTheme(){
        return this.theme;
    }

    public User getUser(){
        return this.user;
    }

    public void setUser(User user){
        this.user = user;
    }

    public void setSelectedRecordType(String type){
        this.selectedRecordType = type;
    }

    public String getSelectedRecordType(){
        return this.selectedRecordType;
    }

    public String getEncryptionPepper(){
        return this.encryptionPepper;
    }

    public static boolean isStageShown(String id){
        List<Window> windows = Window.getWindows();
        AtomicBoolean outcome = new AtomicBoolean(false);

        windows.forEach((n) -> {
            if(n.getProperties().containsKey("id") && n.getProperties().get("id").equals(id)){
                outcome.set(true);
            }
        });

        return outcome.get();
    }

    public static Stage getStageById(String id){
        List<Window> windows = Window.getWindows();
        AtomicReference<Stage> stage = new AtomicReference<>();

        windows.forEach((n) -> {
            if(n.getProperties().containsKey("id") && n.getProperties().get("id").equals(id)){
                stage.set((Stage) n);
            }
        });

        return  stage.get();
    }

    public static MyHealth getInstance(){
        return MyHealth.instance;
    }
}