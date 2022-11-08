package com.jackgharris.cosc2288.a2.models;

import com.jackgharris.cosc2288.a2.core.Database;
import com.jackgharris.cosc2288.a2.core.MyHealth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

public class Activity {

    private final String description;
    private final String time;

    public Activity(String description){
        this.description = description;
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
        Date date = new Date();
        this.time = formatter.format(date);
    }

    public Activity(String description, String time){
        this.description = description;
        this.time = time;
    }


    public String getDescription(){
        return this.description;
    }


    public String getTime(){
        return this.time;
    }

    public static boolean add(Activity activity){
        String sql = "INSERT INTO activities (user_id, description, time) VALUES ('"+MyHealth.getInstance().getUser().getId()+"', '"+activity.getDescription()+"','"+activity.getTime()+"')";

        return Database.queryWithBooleanResult(sql);
    }

    public static ArrayList<Activity> get(int limit){
        ArrayList<Activity> output = new ArrayList<>();

        String sql = "SELECT * FROM activities WHERE user_id='"+MyHealth.getInstance().getUser().getId()+"' LIMIT "+limit;

        Vector<HashMap<String, String>> result = Database.query(sql);
        result.forEach((n)->{
            output.add(new Activity(n.get("description"), n.get("time")));
        });
        return output;
    }

}
