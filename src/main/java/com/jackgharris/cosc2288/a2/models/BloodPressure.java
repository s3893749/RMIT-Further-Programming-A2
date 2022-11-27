//**** PACKAGE ****\\
package com.jackgharris.cosc2288.a2.models;

//**** START BLOOD PRESSURE CLASS ***\\
public class BloodPressure extends Record{

    //**** CLASS VARIABLES ****\\

    //Systolic blood pressure
    private int systolic;

    //Diastolic blood pressure
    private int diastolic;

    //**** CONSTRUCTOR ****\\
    //The blood pressure class extends the record class to add new functionality, in this case we can
    //see that the blood pressure will split the value into two separate variables and then pass all
    //other parameters to the parent.
    public BloodPressure(int id, String type, int userId, String value, String date, String time) {
        super(id, type, userId, value, date, time);

        //set the systolic & diastolic blood pressure
        this.systolic = Integer.parseInt(this.value.split("/")[0]);
        this.diastolic = Integer.parseInt(this.value.split("/")[1]);
    }

    //**** GET SYSTOLIC METHOD ****\\
    //returns the systolic blood pressure.
    public Integer getSystolic(){
        return this.systolic;
    }

    //**** GET DIASTOLIC METHOD ****\\
    //returns the diastolic blood pressure
    public Integer getDiastolic(){
        return this.diastolic;
    }
}
