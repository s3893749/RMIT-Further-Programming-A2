//**** PACKAGE ****\\
package com.jackgharris.cosc2288.a2.models;

//**** PACKAGE IMPORTS ****\\
import com.jackgharris.cosc2288.a2.core.Database;
import com.jackgharris.cosc2288.a2.core.MyHealth;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

//**** START RECORD COLLECTION CLASS ****\\
//The record collection class is a returned from our static Record getters, this
//object is then used to build an easy-to-use SQL query.
public class RecordCollection {

    //**** CLASS VARIABLES ****\\

    //Query string, this is the query string that is built
    private String query;

    //Boolean should update cache, this variable tracks if the cache should
    //be updated
    private boolean shouldUpdateCache;

    //String orderBy, this will indicate what column we want to order by.
    private String orderBy;

    //Hashmap of where clauses, these are condensed on query execution.
    private final HashMap<String, String> where;

    //Hashmap of whereGreaterThan chains that are condensed and appended on query execution
    private final HashMap<String, String> whereGreaterThan;

    //Hashmap of whereLessThan chains that are condensed and appended on query execution
    private final HashMap<String, String> whereLessThan;

    //Integer limit, this is the limit of how many records should be returned.
    private int limit;

    //Boolean should use cache, this indicates if the query should use the cache instead of the database.
    private boolean shouldUseCache;

    //**** CONSTRUCTOR ****\\
    //This constructor initializes all our variables ready for the object chain to be constructed.
    public RecordCollection(){
        this.query = "SELECT * FROM records";
        this.shouldUpdateCache = false;
        this.orderBy = "";
        this.where = new HashMap<>();
        this.whereGreaterThan = new HashMap<>();
        this.whereLessThan = new HashMap<>();
        this.limit = 0;
    }

    //**** WHERE METHOD ****\\
    //Added a new key value pair for where
    public RecordCollection where(String key, String value){

        this.where.put(key,value);

        return this;
    }

    //**** WHERE GREATER THAN METHOD ****\\
    //Added a new key value pair for where greater than
    public RecordCollection whereGreaterThan(String key, String value){
        this.whereGreaterThan.put(key, value);
        return this;
    }

    //**** WHERE LESS THAN METHOD ****\\
    //Added a new key value pair for where less than
    public RecordCollection whereLessThan(String key, String value){
        this.whereLessThan.put(key, value);
        return this;
    }

    //**** UPDATE CACHE METHOD ****\\
    //toggles the update cache variable.
    public RecordCollection updateCache(){
        this.shouldUpdateCache = true;
        return this;
    }

    //**** FROM CACHE METHOD ****\\
    //toggles the from cache variable.
    public RecordCollection fromCache(){
        this.shouldUseCache = true;
        return this;
    }

    //**** WITH CURRENT USER METHOD ****\\
    //This is a short cut method to insert a where cause for the current user_id
    public RecordCollection withCurrentUser(){

        this.where.put("user_id",String.valueOf(MyHealth.getInstance().getUser().getId()));

        return this;
    }

    //**** SORT METHOD ****\\
    //This method accepts a column and will sort the query by the column
    public RecordCollection sort(String column){
        this.orderBy = " ORDER BY "+column;
        return this;
    }

    //**** LIMIT METHOD ****\\
    //This method will introduce a limit on the amount of records to be returned.
    public RecordCollection limit(int limit){
        this.limit = limit;
        return this;
    }

    //**** GET METHOD ****\\
    //This method will build our query, execute the query and return the list of
    //record objects back to the user
    public ObservableList<Record> get(){

        //check, if we should use the cache, if so then return it
        if(this.shouldUseCache){
            return Record.getCache();
        }

        //create our atomic integer count
        AtomicInteger count = new AtomicInteger();
        count.set(0);

        //add all our where query's, ensure the first in the chain is WHERE
        //then all others are AND
        this.where.forEach((key,value) ->{

            //check for query 1 for WHERE
            if(count.get() == 0){
                this.query += " WHERE "+key+"='"+value+"'";
                //else its and
            }else{
                this.query += " AND "+key+"='"+value+"'";

            }
            count.getAndIncrement();
        });

        //insert our where greater than query's replicating the same logic as above
        this.whereGreaterThan.forEach((key,value)->{

            if(count.get() == 0){
                this.query += " WHERE "+key+" >= '"+value+"'";
            }else{
                this.query += " AND "+key+" >= '"+value+"'";
            }
            count.getAndIncrement();
        });

        //insert our less than key values replicating the same logic as above.
        this.whereLessThan.forEach((key,value)->{

            if(count.get() == 0){
                this.query += " WHERE "+key+" <= '"+value+"'";
            }else{
                this.query += " AND "+key+" <= '"+value+"'";
            }
            count.getAndIncrement();
        });

        //append our order by
        this.query += this.orderBy;

        //check if we have a limit, if so add it to the query
        if(this.limit > 0){
        this.query += " LIMIT "+this.limit;
        }

        //create our output observable list
        ObservableList<Record> output = FXCollections.observableArrayList();

        //get our resultSet from the database query.
        Vector<HashMap<String, String>> resultSet = Database.query(this.query);

        //for each result in the result set create a new record object.
        resultSet.forEach((n) -> {
            output.add(new Record(
                    Integer.parseInt(n.get("id")),
                    n.get("type"),
                    Integer.parseInt(n.get("user_id")),
                    n.get("value"),
                    n.get("date"),
                    n.get("time")));
        });


        //check if we should update the cache, if so update it.
        if(this.shouldUpdateCache){
            Record.setCache(output);
        }

        //lastly return the output to the caller.
        return output;
    }
}
