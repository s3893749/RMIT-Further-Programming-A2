package com.jackgharris.cosc2288.a2.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Weight extends Record{

    private static final ObservableList<Weight> weightObservableList = FXCollections.observableArrayList();

    public Weight(int userId, Float value, String date) {
        super(userId, value, date);
    }

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
