package com.jackgharris.cosc2288.a2.core;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Query {

    private String table;
    private ResultSet results;
    private int limit;

    public Query(String table){
        this.table = table;
    }

    public Query where(String key, String value){
        return  this;
    }

    public Query orderBy(String key){
         return this;
    }

    public Query limit(int limit){
        return this;
    }

    public Object first() throws SQLException {
        return this.results.first();
    }

    public ResultSet all(){
        return this.results;
    }
}
