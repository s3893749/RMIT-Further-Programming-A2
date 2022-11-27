//**** PACKAGE ****\\
package com.jackgharris.cosc2288.a2.models;

//**** PACKAGE IMPORTS ****\\
import com.jackgharris.cosc2288.a2.core.Database;
import com.jackgharris.cosc2288.a2.core.EasyImage;
import com.jackgharris.cosc2288.a2.core.MyHealth;
import javafx.scene.image.Image;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Vector;

//**** START USER CLASS ****\\
public class User {

    ///**** CLASS VARIABLES ****\\

    //ID, this is an id assigned to a user by SQL
    private int id;

    //Username, this is the first part of a user email
    private String username;

    //Firstname, this is the firstname specified by a user
    private String firstname;

    //Lastname, this is the lat name specified by a user
    private String lastname;

    //Email, this is the email specified by the user.
    private String email;

    //Password, this is the users' password (hashed)
    private String password;

    //Photo -> this is an easy image object holding the users profile image.
    private EasyImage photo;

    //Photo serialised, this is a base64 bit serialised photo bits
    private String photoSerialized;

    //Theme this is the users theme object.
    private Theme theme;

    //Boolean hide recent logins, this is the preference for if the user activity
    //should show or hide logins.
    private boolean hideRecentLogins;

    //Last page, this string is the last page the user has accessed.
    private String lastPage;

    //**** CONSTRUCTOR ****\\
    //The constructor accepts all the variables above and sets them to class variables.
    public User(int id, String username, String firstname, String lastname, String email, String password, String photo, String hideRecentLogins, String lastPage){
        this.id = id;
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.photo = EasyImage.deSerialize(photo);
        this.photoSerialized = photo;
        this.theme = Theme.get(this.id);
        this.hideRecentLogins = Boolean.parseBoolean(hideRecentLogins);
        this.lastPage = lastPage;

        //check if the theme requires inserting, if so add it
        if(theme.themeRequiresInserting()){
           Theme.add(this);
        }
    }

    //**** GET USERNAME METHOD ****\\
    //Returns the username
    public String getUsername(){
        return this.username;
    }

    //**** GET FIRSTNAME METHOD ****\\
    //returns the firstname of the user.
    public String getFirstname(){
        return this.firstname;
    }

    //**** GET LAST NAME METHOD ****\\
    //returns the user surname.
    public String getSurname(){
        return this.lastname;
    }

    //**** GET EMAIL METHOD ****\\
    //returns the users email address.
    public String getEmail(){
        return this.email;
    }

    //**** GET PASSWORD ****\\
    //returns the users password.
    public String getPassword(){
        return this.password;
    }

    //**** GET PROFILE IMAGE METHOD ****\\
    //returns the users profile image
    public Image getProfileImage(){
      return this.photo.getImage();
    }

    //**** GET PHOTO METHOD ****\\
    //returns the users photo (base64 serialized version)
    public String getPhoto(){
        return this.photoSerialized;
    }

    //**** SET PROFILE IMAGE METHOD ****\\
    //This method will set the profile image for a user
    public void setProfileImage(String image){
        this.photo = EasyImage.deSerialize(image);
        this.photoSerialized = image;
    }

    //**** GET ID METHOD ****\\
    //This method returns the users ID
    public int getId(){
        return this.id;
    }

    //**** GET THEME METHOD ****\\
    //This method returns the users theme object
    public Theme getTheme(){
        return this.theme;
    }

    //**** SET LAST PAGE METHOD ****\\
    //This method will set the last page that the user has accessed
    public void setLastPage(String lastPage){
        //set the class variable
        this.lastPage = lastPage;

        //build the database query
        String query = "UPDATE `users` SET " +
                "`lastPage` = '"+lastPage+"' " +
                " WHERE id = "+this.id+"";

        //execute the database query.
        Database.queryWithBooleanResult(query);
    }

    //**** GET LAST PAGE METHOD ****\\
    //This method returns the last page that the user accessed.
    public String getLastPage(){
        return this.lastPage;
    }

    //**** SET FIRSTNAME METHOD ****\\
    //This method sets the firstname for the user.
    public void setFirstname(String firstname){
        this.firstname = firstname;
    }

    //**** SET LASTNAME METHOD ****\\
    //This method sets the last name for the user.
    public void setLastname(String lastname){
        this.lastname = lastname;
    }

    //**** SET EMAIL METHOD ****\\
    //This method sets the users email
    public void setEmail(String email){
        this.email = email;
    }

    //**** SET PASSWORD METHOD ****\\
    //This method will set the users password to the new password provided.
    public void setPassword(String password){
        this.password = password;
    }

    //**** SHOULD HIDE RECENT LOGINS ****\\
    //This method returns true or false for if the logins should be a hidden activity for this user.
    public boolean shouldHideRecentLogins(){
        return this.hideRecentLogins;
    }

    //**** SET SHOULD HIDE LOGINS ****\\
    //This method toggles the hide login status for the user
    public void setShouldHideLogins(boolean show){
        this.hideRecentLogins = show;
        this.updateDetails();
    }

    //**** UPDATE DETAILS METHOD ****\\
    //This method will update all the users details in the database
    public boolean updateDetails(){

        //build our query
        String query = "UPDATE `users` SET " +
                "`firstname` = '"+this.firstname+"', " +
                "`lastname` = '"+this.lastname+"', " +
                "`email` = '"+this.email+"', " +
                "`password` = '"+this.password+"', " +
                "`photo` = '"+this.getPhoto()+"', " +
                "`hideRecentLogins` ='" +this.hideRecentLogins+"', "+
                "`lastPage` = '"+this.lastPage+"' "+
                " WHERE id = "+this.id+"";

        //add our new activity
        Activity.add(new Activity("User details updated for account "+this.email));

        //return the execution result.
        return Database.queryWithBooleanResult(query);
    }


    //**** LOGIN METHOD ****\\
    //This method accepts an email and password and will return true or false for login fail or success
    public static boolean login(String email, String password){

        //build our query
        String query = "SELECT * FROM users WHERE email='"+email+"' AND password='"+ User.hash(password) +"'";

        //get our results
        Vector<HashMap<String,String>> result =  Database.query(query);

        //return the outcome of that
        return !result.isEmpty();

    }

    //**** GET BY EMAIL METHOD ****\\
    //This method will return a user based on the email address provided.
    public static User getByEmail(String email){

        //build our SQL query
        String query = "SELECT * FROM users WHERE email='"+email+"'";

        //execute the query and get our data results
        Vector<HashMap<String ,String>> data = Database.query(query);

        //check for empty result, if so return null.
        if(data.isEmpty()){
            return null;
        }

        //else we return a new user object.
        return new User(
                Integer.parseInt(data.get(0).get("id")),
                data.get(0).get("username"),
                data.get(0).get("firstname"),
                data.get(0).get("lastname"),
                data.get(0).get("email"),
                data.get(0).get("password"),
                data.get(0).get("photo"),
                data.get(0).get("hideRecentLogins"),
                data.get(0).get("lastPage"));
    }

    //**** HASH METHOD ****\\
    //This method will accept a password and hash it with SHA-512
    public static String hash(String password){

        //declare our hashed password byte array
        byte[] hashedPassword = new byte[0];

        //open our try catch
        try {

            //get our message digest instance for SHA-512
            MessageDigest md = MessageDigest.getInstance("SHA-512");

            //update it to include our new encryption pepper and to use the standard UTF 8 char set

            md.update(MyHealth.getInstance().getEncryptionPepper().getBytes(StandardCharsets.UTF_8));

            //set our hashed password to the result of the md.digest.
            hashedPassword = md.digest(password.getBytes(StandardCharsets.UTF_8));

        } catch (NoSuchAlgorithmException e) {

            //if we run into an error simply ouput to the console and continue
            System.out.println("Hashing failed");
        }

        //create a new string build object that is the length of the hashed password *2
        StringBuilder stringBuilder = new StringBuilder(hashedPassword.length *2);

        //loop over all our bytes and append them to the string build using %X
        for(byte b : hashedPassword){
            stringBuilder.append(String.format("%X",b));
        }

        //finally the hash is complete, and we return the result of the stringBuild.toString()
        return stringBuilder.toString();
    }

    //**** ADD USER METHOD ****\\
    //This method will accept a user object and add it to the database.
    public static boolean add(User user) {

        //build our SQL statement
        String sql = "INSERT INTO users (username, firstname, lastname, email, password, photo, hideRecentLogins, lastPage) VALUES " +
                "('" + user.getUsername() + "','" + user.getFirstname() + "', '" + user.getSurname() + "','" + user.getEmail() + "','" + User.hash(user.password) + "','" +
                user.getPhoto() + "', '"+user.shouldHideRecentLogins()+"','"+user.lastPage+"')";

        //execute the statement and return the result.
        return Database.queryWithBooleanResult(sql);

    }
}