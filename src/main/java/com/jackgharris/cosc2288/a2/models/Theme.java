//**** PACKAGE ****\\
package com.jackgharris.cosc2288.a2.models;

//**** PACKAGE IMPORTS ****\\
import com.jackgharris.cosc2288.a2.core.Database;
import com.jackgharris.cosc2288.a2.core.MyHealth;

import java.util.HashMap;
import java.util.Vector;

//**** START THEME CLASS ****\\
public class Theme {

    //CLASS VARIABLES ****\\

    //Values hashmap, this stores all our key value pairs for this theme object.
    private HashMap<String, String> values;

    //Requires inserting boolean, this tracks if the theme needs to be inserted into the database
    private final boolean requiresInserting;

    //saved, this tracks if the theme has been saved to the database
    private boolean saved;

    //**** CONSTRUCTOR ****\\
    //The constructor accepts our hashmap of value and will remove the SQL id and user_id columns
    public Theme(HashMap<String, String> values){
        this.values = values;
        this.values.remove("id");
        this.values.remove("user_id");
        this.requiresInserting = false;
        this.saved = true;
    }

    //**** CONSTRUCTOR TWO ****\\
    //This overloaded constructor is called when no hashmap of values is provided, this sets the requirements inserting value
    //to true to indicate this needs to be saved to the database.
    public Theme(){
        this.requiresInserting = true;
        this.saved = true;
    }

    //**** THEME REQUIRES INSERTING METHOD ****\\
    //This method returns true or false depending on if this theme object needs
    //inserting into the database.
    public boolean themeRequiresInserting(){
        return this.requiresInserting;
    }

    //**** GET COLOR METHOD ***\\
    //This method accepts a key and will return the color value for that key.
    public String getColor(String key){
        return this.values.get(key);
    }

    //**** SET COLOR METHOD ****\\
    //This method will accept a key and a hex color and set the value for that
    //key, it will also update the current app styling to reflect the change.
    public void setColor(String key, String hex){
        this.saved = false;
        this.values.remove(key);
        this.values.put(key,hex);

        MyHealth.getInstance().getParent().setStyle(this.getStyle());
    }

    //**** GET STYLE METHOD ****\\
    //This method will condense the hashmap into a style string that can
    //be applied to a java FX node style setter.
    public String getStyle(){
        StringBuilder style = new StringBuilder();

        this.values.forEach((k,v) ->{
            String entry = k+": "+v+"; ";
            style.append(entry);
        });

        return style.toString();
    }

    //**** RESET METHOD ****\\
    //This method will reset all the values to default.
    public void reset(boolean updateInstance){
        HashMap<String, String> values = new HashMap<>();

        values.put("-fx-error","#F75165");
        values.put("-fx-success","#7CCF37");

        values.put("-fx-button-primary-background","#4F7BFE");
        values.put("-fx-button-primary-background-hover","#4265d5");
        values.put("-fx-button-primary-text","white");

        values.put("-fx-button-danger-background","#F75165");
        values.put("-fx-button-danger-background-hover","#cc3e50");
        values.put("-fx-button-danger-text","white");

        values.put("-fx-background-primary","#25284B");
        values.put("-fx-background-secondary","#2D325A");

        //set the values to the default values array listed above
        this.values = values;

        //set saved to false
        this.saved = false;

        //update the app instance to reflect the new style.
        if(updateInstance){
            MyHealth.getInstance().getParent().setStyle(this.getStyle());
        }
    }

    //**** SAVE METHOD ****\\
    //This method will save the theme to the database
    public void save(){

        //get our current user
        User user = MyHealth.getInstance().getUser();

        //build our SQL query.
        String sql = "UPDATE `themePreferences` SET " +
                "`-fx-error`='"+this.getColor("-fx-error")+"', "+
                "`-fx-success`='"+this.getColor("-fx-success")+"', "+
                "`-fx-button-primary-background`='"+this.getColor("-fx-button-primary-background")+"', "+
                "`-fx-button-primary-text`='"+this.getColor("-fx-button-primary-text")+"', "+
                "`-fx-button-primary-background-hover`='"+this.getColor("-fx-button-primary-background-hover")+"', "+
                "`-fx-button-danger-background`='"+this.getColor("-fx-button-danger-background")+"', "+
                "`-fx-button-danger-text`='"+this.getColor("-fx-button-danger-text")+"', "+
                "`-fx-button-danger-background-hover`='"+this.getColor("-fx-button-danger-background-hover")+"', "+
                "`-fx-background-primary`='"+this.getColor("-fx-background-primary")+"', "+
                "`-fx-background-secondary`='"+this.getColor("-fx-background-secondary")+"'"+
                " WHERE user_id="+user.getId();

        //execute the query
        Database.queryWithBooleanResult(sql);

        //set saved to true
        this.saved = true;

        //add a new activity to indicate the theme has been saved.
        Activity.add(new Activity("Updated Theme Preferences Saved"));
    }

    //**** IS SAVED METHOD ****\\
    //returns the boolean saved status of the theme.
    public boolean isSaved(){
        return this.saved;
    }

    //**** GET THEME ****\\
    //This method will return the theme for a specific user id, if no theme is present
    //for that user then it will return a new theme that will be added to the database
    //by the user via the requiresInserting boolean variable.
    public static Theme get(int user_id){

        //build our SQL query
        String query = "SELECT * FROM themePreferences WHERE user_id='"+user_id+"'";

        //get our result set
        Vector<HashMap<String,String>> vector = Database.query(query);

        //check if the result if empty, if so make a new theme, else insert the results to a new theme
        if(!vector.isEmpty()){
            return new Theme(vector.get(0));
        }else{
            Theme theme = new Theme();
            theme.reset(false);
            return theme;
        }

    }

    //**** ADD METHOD ****\\
    //This method will add a theme to the database for a specific user
    public static boolean add(User user){

        //build our SQL insert statement
        String sql = "INSERT INTO themePreferences " +
                "(user_id, '-fx-error', '-fx-success', '-fx-button-primary-background', '-fx-button-primary-text', " +
                "'-fx-button-primary-background-hover', '-fx-button-danger-background', '-fx-button-danger-text', '-fx-button-danger-background-hover', " +
                "'-fx-background-primary', '-fx-background-secondary') " +
                "VALUES " +
                "('"+user.getId()+"', '"+user.getTheme().getColor("-fx-error")+"', '"+user.getTheme().getColor("-fx-success")+"','" +
                ""+user.getTheme().getColor("-fx-button-primary-background")+"','"+user.getTheme().getColor("-fx-button-primary-text")+"'," +
                "'"+user.getTheme().getColor("-fx-button-primary-background-hover")+"','"+user.getTheme().getColor("-fx-button-danger-background")+"'," +
                "'"+user.getTheme().getColor("-fx-button-danger-text")+"','"+user.getTheme().getColor("-fx-button-danger-background-hover")+"'," +
                "'"+user.getTheme().getColor("-fx-background-primary")+"','"+user.getTheme().getColor("-fx-background-secondary")+"')";

        //execute the statement and return the result as true or false for success or fail.
        return Database.queryWithBooleanResult(sql);
    }

}
