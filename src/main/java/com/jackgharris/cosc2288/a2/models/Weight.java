package com.jackgharris.cosc2288.a2.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Weight{

    private int userId;
    private int value;
    private String date;

    public Weight(int userId, int value, String date){
        this.userId = userId;
        this.value = value;
        this.date = date;
    }

    public int getUserId(){
        return this.userId;
    }

    public int getValue(){
        return this.value;
    }

    public String getDate(){
        return this.date;
    }

    private static final ObservableList<Weight> weightObservableList = FXCollections.observableArrayList();

    public static void add(Weight weight){
        Weight.weightObservableList.add(weight);
    }

    public static ObservableList<Weight> getAll(int userId){

        ObservableList<Weight> userWeights = FXCollections.observableArrayList();

        for (Weight weight : Weight.weightObservableList){
            if(weight.getUserId() == userId){
                userWeights.add(weight);
            }
        }

        return userWeights;
    }

    public static void remove(Weight weight){
        Weight.weightObservableList.remove(weight);
    }

    public static ObservableList<Weight> getAll(){

        return Weight.weightObservableList;
    }

}
