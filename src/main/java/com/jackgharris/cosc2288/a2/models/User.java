package com.jackgharris.cosc2288.a2.models;


import com.jackgharris.cosc2288.a2.core.Database;
import com.jackgharris.cosc2288.a2.core.EasyImage;
import com.jackgharris.cosc2288.a2.core.MyHealth;
import javafx.scene.image.Image;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Vector;

public class User {


    private int id;
    private String username;
    private String firstname;
    private String lastname;
    private String email;
    private String password;

    private EasyImage photo;
    private String photoSerialized;

    private Theme theme;
    private boolean hideRecentLogins;

    private String lastPage;

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

        if(theme.themeRequiresInserting()){
           Theme.add(this);
        }
    }

    public String getUsername(){
        return this.username;
    }

    public String getFirstname(){
        return this.firstname;
    }

    public String getSurname(){
        return this.lastname;
    }

    public String getEmail(){
        return this.email;
    }

    public String getPassword(){
        return this.password;
    }

    public Image getProfileImage(){
      return this.photo.getImage();
    }

    public String getPhoto(){
        return this.photoSerialized;
    }

    public void setProfileImage(String image){
        this.photo = EasyImage.deSerialize(image);
        this.photoSerialized = image;
    }

    public int getId(){
        return this.id;
    }

    public Theme getTheme(){
        return this.theme;
    }

    public void createTheme(){
        this.theme = new Theme();
        this.theme.reset(false);
    }

    public void setFirstname(String firstname){
        this.firstname = firstname;
    }

    public void setLastname(String lastname){
        this.lastname = lastname;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public boolean shouldHideRecentLogins(){
        return this.hideRecentLogins;
    }

    public void setShouldHideLogins(boolean show){
        this.hideRecentLogins = show;
        this.updateDetails();
    }

    public boolean updateDetails(){

        String query = "UPDATE `users` SET " +
                "`firstname` = '"+this.firstname+"', " +
                "`lastname` = '"+this.lastname+"', " +
                "`email` = '"+this.email+"', " +
                "`password` = '"+this.password+"', " +
                "`photo` = '"+this.getPhoto()+"', " +
                "`hideRecentLogins` ='" +this.hideRecentLogins+"', "+
                "`lastPage` = '"+this.lastPage+"' "+
                " WHERE id = "+this.id+"";

        Activity.add(new Activity("User details updated for account "+this.email));


        return Database.queryWithBooleanResult(query);
    }


    public static boolean login(String email, String password){
        String query = "SELECT * FROM users WHERE email='"+email+"' AND password='"+ User.hash(password) +"'";

        Vector<HashMap<String,String>> result =  Database.query(query);

        return !result.isEmpty();

    }

    public static User getByEmail(String email){
        String query = "SELECT * FROM users WHERE email='"+email+"'";

        Vector<HashMap<String ,String>> data = Database.query(query);

        if(data.isEmpty()){
            return null;
        }

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

    public static String hash(String password){


        byte[] hashedPassword;

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(MyHealth.getInstance().getEncryptionPepper().getBytes(StandardCharsets.UTF_8));
            hashedPassword = md.digest(password.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        StringBuilder stringBuilder = new StringBuilder(hashedPassword.length *2);
        for(byte b : hashedPassword){
            stringBuilder.append(String.format("%X",b));
        }

        return stringBuilder.toString();

    }

    public static boolean add(User user) {

        String sql = "INSERT INTO users (username, firstname, lastname, email, password, photo, hideRecentLogins, lastPage) VALUES " +
                "('" + user.getUsername() + "','" + user.getFirstname() + "', '" + user.getSurname() + "','" + user.getEmail() + "','" + User.hash(user.password) + "','" +
                user.getPhoto() + "', '"+user.shouldHideRecentLogins()+"','"+user.lastPage+"')";


        return Database.queryWithBooleanResult(sql);

    }
}