package com.jackgharris.cosc2288.a2.models;

import java.time.LocalDate;

public class Record {

    protected int userId;
    protected Float value;
    protected String date;

    public Record(int userId, Float value, String date){
        this.userId = userId;
        this.value = value;
        this.date = date;
    }

    public int getUserId(){
        return this.userId;
    }

    public Float getValue(){
        return this.value;
    }

    public LocalDate getDate(){
        return LocalDate.parse(this.date);
    }

}
