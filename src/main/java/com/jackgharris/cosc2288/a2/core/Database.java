//**** PACKAGE ****\\
package com.jackgharris.cosc2288.a2.core;

//**** PACKAGE IMPORTS ****\\
import com.jackgharris.cosc2288.a2.utility.Resource;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.sql.*;
import java.util.HashMap;
import java.util.Objects;
import java.util.Vector;

//**** START DATABASE CLASS ****\\
public class Database {

    //**** STATIC VARIABLES ****\\

    //jdbcURL, this static private variable stores our location to the database, this location prefixed with user.dir
    //will indicate to Java that what ever folder the program is run from it will look there for the file.
    private static final String jdbcURL = "jdbc:sqlite:"+System.getProperty("user.dir")+"/MyHealth.db";

    //Query count, this variable will track how many query's have been executed in a given session.
    private static int queryCount = 0;

    //**** QUERY METHOD ****\\
    //This is the primary SQL that, it accepts a query string and will return a vector of hashmaps containing the key
    //value pairs stored in the database.
    public static Vector<HashMap<String,String>> query(String query){

        //increment our count
        Database.queryCount ++;

        //declare our result set and connection
        Connection connection = null;
        ResultSet resultSet = null;

        //declare our data vector
        Vector<HashMap<String,String>> data = new Vector<>();

        //output the query for debugging
        System.out.println(query);

        //Start our try catch loop to catch any errors
        try{

            //get our connection
            connection = DriverManager.getConnection(Database.jdbcURL);

            //create our statement
            Statement statement = connection.createStatement();

            //load our result set.
            resultSet = statement.executeQuery(query);

            //while we have a result then we create a hashmap for it and push our key value pairs into it
            while(resultSet.next()){
                HashMap<String, String> data2 = new HashMap<>();
                int i = 1;
                while(i <= resultSet.getMetaData().getColumnCount()){
                    data2.put(resultSet.getMetaData().getColumnName(i),resultSet.getString(i));
                    i++;
                }
                //once completed add the hashmap to the vector.
                data.add(data2);
            }

            //lastly we close the connection
            connection.close();

        } catch (SQLException e) {
            //if we catch an error show the error alert.
            showErrorPage(e);
        }

        //return the data vector to the caller
        return data;
    }

    //**** QUERY WITH BOOLEAN RESULT METHOD ****\\
    //unlike the query method above that returns a vector of key pair results, this method
    //will execute a query adn then return true or false if it succeeds or fails.
    public static boolean queryWithBooleanResult(String query){

        //declare our connection
        Connection connection = null;

        //declare our outcome
        boolean outcome;

        //increment the query counter
        Database.queryCount ++;

        //output the query for debug
        System.out.println(query);

        //start our try catch loop to catch any errors
        try{
            //get our connection
            connection = DriverManager.getConnection(Database.jdbcURL);
            //create our statement
            Statement statement = connection.createStatement();

            //track the result
            int i = statement.executeUpdate(query);

            //if the result is not 0 we set the outcome to true
            outcome = i > 0;

            //close the connection
            connection.close();

        } catch (SQLException e) {
            //if we catch an error show the error page
            outcome = false;
            showErrorPage(e);
        }

        //lastly return the outcome
        return outcome;
    }

    //**** GET QUERY COUNT METHOD ****\\
    //This method will return the current query count
    public static int getQueryCount(){
        return Database.queryCount;
    }

    //**** RESET QUERY COUNT METHOD ***\\
    //This method will reset the query count to 0.
    public static void resetQueryCount(){
        Database.queryCount = 0;
    }

    //**** SHOW ERROR PAGE METHOD ****\\
    //This method will show the error page when a SQL error is caught.
    private static void showErrorPage(SQLException e){
        //Before we delete we ask one more time via a conformation alert
        Alert alert = new Alert(Alert.AlertType.ERROR);
        //set the title
        alert.setTitle("Fatal SQL Error");
        //set the context text description
        alert.setContentText(e.getMessage());
        //get our dialog pane
        DialogPane dialogPane = alert.getDialogPane();
        //insert out css file to that pane
        dialogPane.getStylesheets().add(Objects.requireNonNull(MyHealth.class.getResource("css/app.css")).toExternalForm());
        //add the css class of parent
        dialogPane.getStyleClass().add("parent");
        //get the stage
        Stage stage = (Stage) dialogPane.getScene().getWindow();
        //add the warning icon
        stage.getIcons().add(Resource.faviconWarning());
        //load the warning image into an image view
        ImageView imageView = new ImageView(Resource.faviconWarning());
        //set the fit width and high
        imageView.setFitWidth(64);
        imageView.setFitHeight(64);
        //set the graphite to that image
        alert.setGraphic(imageView);
        //declare our two button types, yes and no
        ButtonType okButton = new ButtonType("Ok", ButtonBar.ButtonData.YES);
        //set the buttons
        alert.getButtonTypes().setAll(okButton);
        alert.showAndWait().ifPresent(type -> {

            if (type == okButton) {
                alert.close();
            }
        });
    }
}
