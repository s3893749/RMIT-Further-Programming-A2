//**** PACKAGE ****\\
package com.jackgharris.cosc2288.a2.models;

//**** PACKAGE IMPORTS ****\\
import com.jackgharris.cosc2288.a2.core.Database;
import com.jackgharris.cosc2288.a2.core.MyHealth;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

//**** START ACTIVITY CLASS ***\\
public class Activity {

    //**** CLASS VARIABLES ****\\

    //The description variable stores a description of the activity
    //that has taken place.
    private final String description;

    //The time variable stores the current unix time stamp at the time
    //the activity took place.
    private final Long time;

    //*** CONSTRUCTOR ***\\
    //This constructor makes an activity object with only a description
    //provided and time stamps it at the time its called.
    public Activity(String description){
        this.description = description;
        this.time = Instant.now().getEpochSecond();
    }

    //**** CONSTRUCTOR TWO ****\\
    //The second overloaded constructor builds an object with both the time
    //and description provided.
    public Activity(String description, String time){
        this.description = description;
        this.time = Long.parseLong(time);
    }


    //**** GET DESCRIPTION METHOD ****\\
    //returns the activity description.
    public String getDescription(){
        return this.description;
    }

    //**** GET TIME METHOD ***\\
    //returns the time as a unix time stamp
    public Long getTime(){
        return this.time;
    }

    //**** GET TIME READABLE METHOD ****\\
    //returns the readable formatted time as a string
    public String getTimeReadable(){
        Date date = Date.from(Instant.ofEpochSecond(this.time));
        return new SimpleDateFormat("h:mm a").format(date)+" on "+new SimpleDateFormat("dd/MM/yyyy").format(date);
    }

    //**** STATIC ADD METHOD ***\\
    //Accepts a activity and inserts it into the database
    public static boolean add(Activity activity){

        //build our SQL string
        String sql = "INSERT INTO activities (user_id, description, time) VALUES ('"+MyHealth.getInstance().getUser().getId()+"', '"+activity.getDescription()+"','"+activity.getTime()+"')";

        //return the result of the insert query.
        return Database.queryWithBooleanResult(sql);
    }

    //**** STATIC GET METHOD ****\\
    //This method will accept a limit, an offset and a boolean to represent if it
    //should include login events and will return a array list of activities based on
    //the parameters provided.
    public static ArrayList<Activity> get(int limit, int offset, boolean excludeLogins){

        //Create our output array
        ArrayList<Activity> output = new ArrayList<>();

        //declare our SQL
        String sql;

        //depending on if we need include or exclude the logging in activities set the SQL.
        if(excludeLogins){
            sql = "SELECT * FROM activities WHERE user_id='"+MyHealth.getInstance().getUser().getId()+"' AND description<>'Logged in' ORDER BY time DESC "+" LIMIT "+limit+" OFFSET "+offset;
        }else{
            sql = "SELECT * FROM activities WHERE user_id='"+MyHealth.getInstance().getUser().getId()+"' ORDER BY time DESC "+" LIMIT "+limit+" OFFSET "+offset;
        }

        //get our result from the database
        Vector<HashMap<String, String>> result = Database.query(sql);

        //for each result we build an activity object and add it to the output.
        result.forEach((n)->{
            output.add(new Activity(n.get("description"), String.valueOf(n.get("time"))));
        });

        //finally return the output
        return output;
    }

    //**** STATIC DELETE METHOD ****\\
    //This method accepts an activity and will delete it from the database.
    public static boolean delete(Activity activity){

        //build our sql string
        String sql =  "DELETE FROM activities WHERE time='"+activity.getTime()+"' AND description='"+activity.getDescription()+"'";

        //return the result of the sql query.
        return Database.queryWithBooleanResult(sql);
    }

}
