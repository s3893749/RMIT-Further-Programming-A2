package com.jackgharris.cosc2288.a2.core;


import java.sql.*;

public class Database {

    public Database where(String key, String value){
        return this;
    }

    public Database orderBy(String key, String type){
        return  this;
    }

    private Database(String table){
        String jdbcURL = "jdbc:sqlite:C:\\Users\\st_tu\\Documents\\RMIT\\Further Programming\\A2\\A2\\chinook.db";
        Connection connection = null;
        try{
            connection = DriverManager.getConnection(jdbcURL);

            String sql = "SELECT * FROM customers";
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);

            System.out.println(result.toString());

            while(result.next()){
                System.out.println(result.getString("firstName"));
            }

        } catch (SQLException e) {
            System.out.println("Failed to connect to SQLite database '"+e.getMessage()+"'");
        }



    }

    public static Database table(String table){

        return new Database(table);
    }
}
