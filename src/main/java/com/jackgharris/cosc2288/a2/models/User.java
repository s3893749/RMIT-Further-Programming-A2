package com.jackgharris.cosc2288.a2.models;

import java.util.ArrayList;
import java.util.HashMap;

public class User {

    private final HashMap<String, String> data;

    private static User[] users;

    public User(HashMap<String, String> data){
        this.data = data;
    }

    public String getUsername(){
        return this.data.get("username");
    }

    public String getFirstname(){
        return this.data.get("firstname");
    }

    public String getSurname(){
        return this.data.get("surname");
    }

    public String getProfileImage(){
        return this.data.get("profileImage");
    }

    public String getValueWhereKey(String key){
        return this.data.get(key);
    }

    public static User[] where(String key, String value){

        ArrayList<User> searchResults = new ArrayList<>();

        for(User user: User.users){
            if(user.getValueWhereKey(key).equals(value)){
                searchResults.add(user);
            }
        }

        return searchResults.toArray(User[]::new);
    }

    public static void bindUserData(User[] users){
        User.users = users;
    }


}