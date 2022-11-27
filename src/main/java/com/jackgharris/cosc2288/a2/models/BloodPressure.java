package com.jackgharris.cosc2288.a2.models;

public class BloodPressure extends Record{

    private int systolic;
    private int diastolic;


    public BloodPressure(int id, String type, int userId, String value, String date, String time) {
        super(id, type, userId, value, date, time);

        this.systolic = Integer.parseInt(this.value.split("/")[0]);
        this.diastolic = Integer.parseInt(this.value.split("/")[1]);
    }

    public Integer getSystolic(){
        return this.systolic;
    }

    public Integer getDiastolic(){
        return this.diastolic;
    }
}
