package com.jackgharris.cosc2288.a2.models;

public class Activity {

    private String caller;
    private String description;
    private String userName;

    public Activity(Object caller, User user ,String description){
        this.caller = caller.getClass().getName();
        this.description = description;
        this.userName = user.getFirstname()+" "+user.getSurname();
    }

    public String getCaller(){
        return this.caller;
    }

    public String getDescription(){
        return this.description;
    }

    public String getUserName(){
        return this.userName;
    }

}
