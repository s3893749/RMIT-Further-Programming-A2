package com.jackgharris.cosc2288.a2.models;


import com.jackgharris.cosc2288.a2.core.Database;

import java.util.HashMap;
import java.util.Vector;

public class User {


    private int id;
    private String username;
    private String firstname;
    private String lastname;
    private String email;
    private String password;

    public User(int id, String username, String firstname, String lastname, String email, String password){
        this.id = id;
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
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

    public String getProfileImage(){
        return null;
    }

    public int getId(){
        return this.id;
    }


    public static boolean login(String email, String password){
        String query = "SELECT * FROM users WHERE email='"+email+"' AND password='"+password+"'";

        Vector<HashMap<String,String>> result =  Database.query(query);

        boolean outcome = false;

        outcome = !result.isEmpty();

        return outcome;

    }

    public static User getByEmail(String email){
        String query = "SELECT * FROM users WHERE email='"+email+"'";

        Vector<HashMap<String ,String>> data = Database.query(query);

        return new User(
                Integer.parseInt(data.get(0).get("id")),
                data.get(0).get("username"),
                data.get(0).get("firstname"),
                data.get(0).get("lastname"),
                data.get(0).get("email"),
                data.get(0).get("password"));
    }


}