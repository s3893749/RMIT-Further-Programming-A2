package com.jackgharris.cosc2288.a2.models;

import com.jackgharris.cosc2288.a2.core.Database;
import com.jackgharris.cosc2288.a2.core.MyHealth;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

public class RecordCollection {

    private String query;
    private boolean shouldUpdateCache;
    private String orderBy;
    private final HashMap<String, String> where;
    private int limit;
    private boolean shouldUseCache;

    public RecordCollection(){
        this.query = "SELECT * FROM records";
        this.shouldUpdateCache = false;
        this.orderBy = "";
        this.where = new HashMap<>();
        this.limit = 0;
    }

    public RecordCollection where(String key, String value){

        this.where.put(key,value);

        return this;
    }

    public RecordCollection updateCache(){
        this.shouldUpdateCache = true;
        return this;
    }

    public RecordCollection fromCache(){
        this.shouldUseCache = true;
        return this;
    }

    public RecordCollection withCurrentUser(){

        this.where.put("user_id",String.valueOf(MyHealth.getInstance().getUser().getId()));

        return this;
    }

    public RecordCollection sort(String column){
        this.orderBy = " ORDER BY "+column;
        return this;
    }

    public RecordCollection limit(int limit){
        this.limit = limit;
        return this;
    }

    public ObservableList<Record> get(){

        if(this.shouldUseCache){
            return Record.getCache();
        }

        AtomicInteger count = new AtomicInteger();
        count.set(0);

        this.where.forEach((key,value) ->{

            if(count.get() == 0){
                this.query += " WHERE "+key+"='"+value+"'";
            }else{
                this.query += " AND "+key+"='"+value+"'";

            }
            count.getAndIncrement();
        });

        this.query += this.orderBy;

        if(this.limit > 0){
        this.query += " LIMIT "+this.limit;
        }


        ObservableList<Record> output = FXCollections.observableArrayList();

        Vector<HashMap<String, String>> resultSet = Database.query(this.query);

        resultSet.forEach((n) -> {
            output.add(new Record(
                    Integer.parseInt(n.get("id")),
                    n.get("type"),
                    Integer.parseInt(n.get("user_id")),
                    n.get("value"),
                    n.get("date")));
        });


        if(this.shouldUpdateCache){
            Record.setCache(output);
            System.out.println(this.query);

        }

        return output;
    }
}
