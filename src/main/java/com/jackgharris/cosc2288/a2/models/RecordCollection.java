package com.jackgharris.cosc2288.a2.models;

import com.jackgharris.cosc2288.a2.core.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;
import java.util.Vector;

public class RecordCollection {

    private String query;
    private boolean whereCalled;
    private boolean updateCacheCalled;

    public RecordCollection(){
        this.query = "SELECT * FROM records";
        this.whereCalled = false;
        this.updateCacheCalled = false;
    }

    public RecordCollection where(String key, String value){

        if(!this.whereCalled) {
            this.query += " WHERE " + key + "='" + value + "'";
            this.whereCalled = true;
        }else{
            this.query += " AND "  + key + "='" + value + "'";
        }
        return this;
    }

    public RecordCollection updateCache(){
        Record.setCache(this.get());
        this.updateCacheCalled = true;
        return this;
    }

    public ObservableList<Record> get(){
        if(this.updateCacheCalled){
            return Record.getCache();
        }else {
            System.out.println(this.query);

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
            return output;
        }
    }

    public ObservableList<Record> getAndCache(){

        if (Record.getCache() == null) {
            Record.setCache(this.get());
        }

        return Record.getCache();
    }

}
