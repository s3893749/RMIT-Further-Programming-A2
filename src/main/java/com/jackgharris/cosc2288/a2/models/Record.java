package com.jackgharris.cosc2288.a2.models;

import com.jackgharris.cosc2288.a2.core.Database;
import com.jackgharris.cosc2288.a2.core.MyHealth;
import javafx.collections.ObservableList;

import java.time.LocalDate;

public class Record {

    private int id;
    private final String type;
    private int userId;
    private String value;
    private String date;
    private static ObservableList<Record> cache;

    public Record(int id, String type, int userId, String value, String date){
        this.id = id;
        this.type = type;
        this.userId = userId;
        this.value = value;
        this.date = date;
    }

    public int getUserId(){
        return this.userId;
    }

    public String getValue(){
        return this.value;
    }

    public LocalDate getDate(){
        return LocalDate.parse(this.date);
    }

    public void setDate(String date){
        this.date = date;
    }

    public void setValue(Float value){
        this.value = String.valueOf(value);
    }

    public String getType(){
        return this.type;
    }

    public int getId(){
        return this.id;
    }

    public void setUserId(int id){
        this.userId = id;
    }

    public boolean updateDetails(){

        String query = "UPDATE `records` SET " +
                "`value` = '"+this.value+"', " +
                "`date` = '"+this.date+"' " +
                " WHERE id="+this.id+"";

        Activity.add(new Activity(this.getType()+" record updated to value of "+this.getValue()+" on date "+this.getDate()));

        return Database.queryWithBooleanResult(query);
    }

    public static RecordCollection where(String key, String value){
        return new RecordCollection().where(key, value);
    }

    public static void setCache(ObservableList<Record> cache){
        Record.cache = cache;
    }

    public static ObservableList<Record> getCache(){
        return Record.cache;
    }

    public static boolean add(Record record){

        Activity.add(new Activity("New record added "+record.getValue()+" for type "+record.getType()));

        String sql = "INSERT INTO records (type, user_id, value, date) VALUES ('"+record.getType()+"','"+ MyHealth.getInstance().getUser().getId()+"', '"+record.getValue()+"','"+record.getDate()+"')";

        return Database.queryWithBooleanResult(sql);
    }

    public static boolean delete(Record record){

        Activity.add(new Activity(record.getType()+" record entry deleted"));

        String sql =  "DELETE FROM records WHERE id='"+record.getId()+"'";

        return Database.queryWithBooleanResult(sql);
    }
}
