package com.jackgharris.cosc2288.a2.core;


import java.sql.*;
import java.util.HashMap;
import java.util.Vector;

public class Database {


    private static final String jdbcURL = "jdbc:sqlite:C:\\Users\\st_tu\\Documents\\RMIT\\Further Programming\\A2\\A2\\src\\main\\resources\\MyHealth.db";

    private static int queryCount = 0;

    public static Vector<HashMap<String,String>> query(String query){
        Database.queryCount ++;
        Connection connection = null;
        ResultSet resultSet = null;
        Vector<HashMap<String,String>> data = new Vector<>();

        System.out.println(query);

        try{
            connection = DriverManager.getConnection(Database.jdbcURL);
            Statement statement = connection.createStatement();

            resultSet = statement.executeQuery(query);

            while(resultSet.next()){
                HashMap<String, String> data2 = new HashMap<>();
                int i = 1;
                while(i <= resultSet.getMetaData().getColumnCount()){
                    data2.put(resultSet.getMetaData().getColumnName(i),resultSet.getString(i));
                    i++;
                }
                data.add(data2);
            }

            connection.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }



        return data;
    }

    public static boolean queryWithBooleanResult(String query){
        Connection connection = null;
        boolean outcome;
        Database.queryCount ++;

        System.out.println(query);

        try{
            connection = DriverManager.getConnection(Database.jdbcURL);
            Statement statement = connection.createStatement();

            int i = statement.executeUpdate(query);

            outcome = i > 0;

            connection.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        return outcome;
    }

    public static int getQueryCount(){
        return Database.queryCount;
    }

    public static void resetQueryCount(){
        Database.queryCount = 0;
    }
}
