package com.jackgharris.cosc2288.a2.models;

import com.jackgharris.cosc2288.a2.core.Database;
import com.jackgharris.cosc2288.a2.core.MyHealth;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

public class Activity {

    private final String description;
    private final Long time;

    public Activity(String description){
        this.description = description;
        this.time = Instant.now().getEpochSecond();
    }

    public Activity(String description, String time){
        this.description = description;
        this.time = Long.parseLong(time);
    }


    public String getDescription(){
        return this.description;
    }


    public Long getTime(){
        return this.time;
    }

    public String getTimeReadable(){

        Date date = Date.from(Instant.ofEpochSecond(this.time));
        return new SimpleDateFormat("h:mm a").format(date)+" on "+new SimpleDateFormat("dd/MM/yyyy").format(date);
    }

    public static boolean add(Activity activity){
        String sql = "INSERT INTO activities (user_id, description, time) VALUES ('"+MyHealth.getInstance().getUser().getId()+"', '"+activity.getDescription()+"','"+activity.getTime()+"')";

        return Database.queryWithBooleanResult(sql);
    }

    public static ArrayList<Activity> get(int limit, int offset, boolean excludeLogins){

        ArrayList<Activity> output = new ArrayList<>();
        String sql;

        if(excludeLogins){
            sql = "SELECT * FROM activities WHERE user_id='"+MyHealth.getInstance().getUser().getId()+"' AND description<>'Logged in' ORDER BY time DESC "+" LIMIT "+limit+" OFFSET "+offset;
        }else{
            sql = "SELECT * FROM activities WHERE user_id='"+MyHealth.getInstance().getUser().getId()+"' ORDER BY time DESC "+" LIMIT "+limit+" OFFSET "+offset;
        }

        Vector<HashMap<String, String>> result = Database.query(sql);
        result.forEach((n)->{
            output.add(new Activity(n.get("description"), String.valueOf(n.get("time"))));
        });
        return output;
    }

    public static boolean delete(Activity activity){

        String sql =  "DELETE FROM activities WHERE time='"+activity.getTime()+"' AND description='"+activity.getDescription()+"'";

        return Database.queryWithBooleanResult(sql);
    }

}
