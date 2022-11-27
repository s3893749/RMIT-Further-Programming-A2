//**** PACKAGE ****\\
package com.jackgharris.cosc2288.a2.core;

//**** PACKAGE IMPORTS ****\\
import com.jackgharris.cosc2288.a2.models.User;
import com.jackgharris.cosc2288.a2.utility.FXMLUtility;
import com.jackgharris.cosc2288.a2.utility.Resource;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

//**** START MY HEALTH CLASS ***\\
public class MyHealth extends javafx.application.Application {

    //**** STATIC VARIABLES ****\\

    //The instance variable is static, this allows for a single reference
    //of the object to be accessible from any location.
    private static MyHealth instance;

    //**** CLASS VARIABLES ****\\

    //The title variable is referenced from other classes to ensure the window
    //titling is consistent and shows the same version.
    private final String title;

    //Selected record type, this is used by the date picker recursive disable dates
    //callback to know what the current record is that we are looking at as the class
    //back prevents standard variable passing.
    private String selectedRecordType;

    //The user object is our user model for the current logged-in user.
    private User user;

    //The encryption pepper is what's used to ensure the passwords stored in the database
    //cannot simply be de hashed.
    private final String encryptionPepper;

    //The parent AnchorPane object is a reference that can be accessed to ensure the theme
    //styles update as required on a change for all current notes that are children of this
    //parent
    private AnchorPane parent;

    //**** CONSTRUCTOR ****\\
    //This is the MyHealth constructor, it will initialize some of our core variables.
    public MyHealth(){

        //set the static instance to this
        MyHealth.instance = this;

        //set the selectedRecordType to null
        this.selectedRecordType = null;

        //set the encryption pepper
        this.encryptionPepper = "Pepper&SteakPie";

        //set the title, this should include the version number.
        this.title ="My Health v1.0";
    }

    //**** OVERRIDE START METHOD ****\\
    //This method will be called by JavaFX and start the application
    @Override
    public void start(Stage stage) throws IOException {

        //Using our FXML Utility class we load the scene into the stage for the login
        stage.setScene(FXMLUtility.loadScene(FXMLUtility.loginFXML, stage, Objects.requireNonNull(MyHealth.class.getResource("login.css")).toExternalForm()));

        //set the stage style to transparent
        stage.initStyle(StageStyle.TRANSPARENT);

        //disable resize
        stage.setResizable(false);

        //set the favicon
        stage.getIcons().add(Resource.favicon());

        //set the stage id
        stage.getProperties().put("id","launcher");

        //lastly show the stage
        stage.show();
    }

    //**** GET & SET USER METHODS ****\\
    //These two methods will get and set the user object
    public User getUser(){
        return this.user;
    }

    public void setUser(User user){
        this.user = user;
    }

    //**** GET AND SET SELECTED RECORD TYPE METHODS ****\\
    //These two methods will set and get the selected record type, this record type
    //is referenced here in the singleton as it is required to be static as it needs
    //to be called inside the date pickers callback.
    public void setSelectedRecordType(String type){
        this.selectedRecordType = type;
    }

    public String getSelectedRecordType(){
        return this.selectedRecordType;
    }

    //**** GET ENCRYPTION PEPPER METHOD ****\\
    //This methods returns the encryption pepper.
    public String getEncryptionPepper(){
        return this.encryptionPepper;
    }

    //*** SET AND GET PARENT METHODS ****\\
    //sets and gets the main parent Anchor Pane
    public void setParent(AnchorPane parent){
        this.parent = parent;
    }

    public AnchorPane getParent(){
        return this.parent;
    }

    //**** GET TITLE ****\\
    //Returns the main title
    public String getTitle(){
        return this.title;
    }

    //**** STATIC IS STAGE SHOWN ****\\
    //This static method accepts an id and then checks to see if a stage with
    //that id is currently shown, if not it will return false, else it returns
    //true.
    public static boolean isStageShown(String id){

        //Get our list of windows
        List<Window> windows = Window.getWindows();

        //Create our response boolean outcome
        AtomicBoolean outcome = new AtomicBoolean(false);

        //Loop over all the windows to see if it contains and id and if the id
        //matches the one we are looking for, if so set the outcome to true.
        windows.forEach((n) -> {
            if(n.getProperties().containsKey("id") && n.getProperties().get("id").equals(id)){
                outcome.set(true);
            }
        });

        //finally return the outcome
        return outcome.get();
    }

    //**** GET STAGE BY ID METHOD ****\\
    //This method will accept an id and then return the stage matching that id
    //it's important to use this method in conjunction with the isShown method
    //to prevent errors.
    public static Stage getStageById(String id){

        //Get our list of windows
        List<Window> windows = Window.getWindows();

        //Create our stage atomic reference
        AtomicReference<Stage> stage = new AtomicReference<>();

        //loop over all our stages/windows and if we have one with that id
        //we set the stage reference to it.
        windows.forEach((n) -> {
            if(n.getProperties().containsKey("id") && n.getProperties().get("id").equals(id)){
                stage.set((Stage) n);
            }
        });

        //finally return our stage object.
        return  stage.get();
    }

    //**** STATIC GET INSTANCE METHOD ****\\
    //This method will return the current instance of the MyHealth core class.
    public static MyHealth getInstance(){
        return MyHealth.instance;
    }

    //**** STATIC MAIN METHOD ****\\
    //This method is the main entry point for the Java application, we can see all it
    //does is launch our JavaFX app via the static launch method.
    public static void main(String[] args) {
        launch();
    }
}