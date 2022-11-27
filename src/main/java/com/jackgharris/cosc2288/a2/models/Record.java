//**** PACKAGE ****\\
package com.jackgharris.cosc2288.a2.models;

//**** PACKAGE IMPORTS ****\\
import com.jackgharris.cosc2288.a2.core.Database;
import com.jackgharris.cosc2288.a2.core.MyHealth;
import javafx.collections.ObservableList;

import java.time.LocalDate;

//**** START RECORD CLASS ****\\
public class Record {

    //**** CLASS VARIABLES ****\\

    //Record id variable
    private int id;

    //Record type variable
    private final String type;

    //Record user_id variable
    private int userId;

    //Record value
    protected String value;

    //Record date variable
    private String date;

    //Record time variable
    private String time;

    //Record dateTime, this is the time & date combined and
    //returned as a string.
    private String dateTime;

    //**** STATIC VARIABLES ****\\

    //The cache variable will be used to reduce the amount of SQL query's executed.
    private static ObservableList<Record> cache;

    //The last added id variable will store the last added record id and can be used to required that id as required.
    public static int lastAddedId = -1;

    //**** CONSTRUCTOR ****\\
    //The record constructor accepts all the parameters and will set the class variables.
    public Record(int id, String type, int userId, String value, String date, String time){
        this.id = id;
        this.type = type;
        this.userId = userId;
        this.value = value;
        this.date = date;
        this.time = time;
        this.dateTime = time+" on "+date;
    }

    //**** GET DATE TIME METHOD ****\\
    //This method returns the date time string in a readable format.
    public String getDateTime(){
        return this.dateTime;
    }

    //**** GET USER ID METHOD ****\\
    //This method returns the user id assisted with the user who created it.
    public int getUserId(){
        return this.userId;
    }

    //**** GET VALUE METHOD ****\\
    //This method returns the current value as a string
    public String getValue(){
        return this.value;
    }

    //**** GET DATE METHOD ****\\
    //This method returns the date as a local date object.
    public LocalDate getDate(){
        return LocalDate.parse(this.date);
    }

    //**** SET DATE METHOD ****\\
    //This method will accept a date string and set it.
    public void setDate(String date){
        this.date = date;
    }

    //**** SET VALUE METHOD ****\\
    //This method will accept a string value and set the value to the class variable.
    public void setValue(String value){
        this.value = value;
    }

    //**** GET TIME METHOD ****\\
    //This method gets the time, and if its null will return time not set
    public String getTime(){
        if(!(this.time == null)){
            return this.time;
        }else{
            return "time not set";
        }
    }

    //**** GET TYPE METHOD ****\\
    //This method returns the type of the record
    public String getType(){
        return this.type;
    }

    //**** GET ID METHOD ****\\
    //This method will return the ID of this record.
    public int getId(){
        return this.id;
    }

    //**** SET USER ID METHOD ****\\
    //This method will accept an integer id and set the user id to that id.
    public void setUserId(int id){
        this.userId = id;
    }

    //**** UPDATE DETAILS METHOD ****\\
    //This method will go ahead and update the database details for this record in the database
    public boolean updateDetails(){

        //build our sql query
        String query = "UPDATE `records` SET " +
                "`value` = '"+this.value+"', " +
                "`date` = '"+this.date+"' " +
                " WHERE id="+this.id+"";

        //add an activity reporting the record change
        Activity.add(new Activity(this.getType()+" record updated to value of "+this.getValue()+" on date "+this.getDate()));

        //return the result of the sql execution.
        return Database.queryWithBooleanResult(query);
    }

    //**** STATIC WITH CURRENT USER METHOD ****\\
    //This static method will return a RecordCollection object and pre-set the where variable to limit to
    //the current user_id.
    public static RecordCollection withCurrentUser(){

        return new RecordCollection().where("user_id",String.valueOf(MyHealth.getInstance().getUser().getId()));
    }

    //**** STATIC WHERE METHOD ****\\
    //This method will record a record collection and pass the where key and value from this to that new object.
    public static RecordCollection where(String key, String value){
        return new RecordCollection().where(key, value);
    }

    //**** STATIC SET AND GET CACHE METHODS ****\\
    //These methods will set and return the cache value.
    public static void setCache(ObservableList<Record> cache){
        Record.cache = cache;
    }

    public static ObservableList<Record> getCache(){
        return Record.cache;
    }

    //**** ADD METHOD ****\\
    //This method will accept a record object and insert it into the database
    public static boolean add(Record record){

        //add a new activity stating the new record has been added
        Activity.add(new Activity("New record added "+record.getValue()+" for type "+record.getType()));

        //create our SQL query
        String sql = "INSERT INTO records (type, user_id, value, date, time) VALUES ('"+record.getType()+"','"+ MyHealth.getInstance().getUser().getId()+"', '"+record.getValue()+"','"+record.getDate()+"', '"+record.getTime()+"')";

        //create the outcome of the result
        boolean outcome = Database.queryWithBooleanResult(sql);

        //if the outcome is true then we update the last added id to the value of that new record.
        if(outcome){
            sql = "SELECT id FROM records WHERE user_id='"+record.getUserId()+"' AND value='"+record.getValue()+"' AND date='"+record.getDate()+"'";

            Record.lastAddedId = Integer.parseInt(Database.query(sql).get(0).get("id"));
        }

        //lastly we return the boolean result
        return outcome;
    }

    //**** DELETE METHOD ****\\
    //This accepts a record and will delete it from the database.
    public static boolean delete(Record record){

        //Add an activity to show it's been deleted.
        Activity.add(new Activity(record.getType()+" record entry deleted"));

        //create our SQL query.
        String sql =  "DELETE FROM records WHERE id='"+record.getId()+"'";

        //return the result of the delete.
        return Database.queryWithBooleanResult(sql);
    }
}
