//**** PACKAGE ****\\
package com.jackgharris.cosc2288.a2.models;

//**** PACKAGE IMPORTS ****\\
import com.jackgharris.cosc2288.a2.core.MyHealth;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.HashMap;

//**** START HEALTH RECORD CLASS ****\\
public class HealthRecord extends Record{

    //**** CLASS VARIABLES ****\\


    //Weight record stores the record object for the current weight.
    private final Record weightRecord;

    //Temperature record stores the temperature record object.
    private final Record temperatureRecord;

    //blood pressure record stores the blood pressure record object.
    private final Record bloodPressureRecord;

    //**** STATIC VARIABLES ****\\

    //To reduce SQL query's when disabling the data picker dates we use a static cache array.
    private static ObservableList<HealthRecord> cache;

    //**** GET CONSTRUCTOR ****\\
    //The health record extends the core Record class functionality to make it suit, most notability we can see that
    //the value provided is largely ignored in favor of loading the full sub records based on the type and date.
    public HealthRecord(int id, String type, int userId, String value, String date, String time) {
        super(id, type, userId, value, date, time);

        //get the weight record
        this.weightRecord = Record.where("type","Weight").withCurrentUser().where("date",date).get().get(0);

        //get the temperature record
        this.temperatureRecord = Record.where("type","Temperature").withCurrentUser().where("date",date).get().get(0);

        //get the blood pressure record (as a raw record object)
        Record bloodPressureRaw = Record.where("type","BloodPressure").withCurrentUser().where("date",date).get().get(0);

        //convert the raw blood pressure object to a real blood pressure object.
        this.bloodPressureRecord = new BloodPressure(bloodPressureRaw.getId(),bloodPressureRaw.getType(),bloodPressureRaw.getUserId(),bloodPressureRaw.getValue(),bloodPressureRaw.getDate().toString(), bloodPressureRaw.getTime());
    }

    //**** GET WEIGHT RECORD METHOD ****\\
    //Returns the weight record
    public Record getWeightRecord(){
        return this.weightRecord;
    }

    //**** GET TEMPERATURE RECORD METHOD ****\\
    //Returns the temperature record
    public Record getTemperatureRecord(){
        return this.temperatureRecord;
    }

    //**** GET BLOOD PRESSURE RECORD METHOD****\\
    //returns the blood pressure record
    public Record getBloodPressureRecord(){
        return this.bloodPressureRecord;
    }

    //**** GET THE TEMPERATURE VALUE METHOD ****\\
    //returns the temperature as a float.
    public Float getTemperature(){
        return Float.parseFloat(this.temperatureRecord.getValue());
    }

    //**** GET WEIGHT VALUE RECORD METHOD****\\
    //returns the weight as a float.
    public Float getWeight(){
        return Float.parseFloat(this.weightRecord.getValue());
    }

    //**** GET BLOOD PRESSURE VALUE METHOD
    //returns the blood pressure record value.
    public String getBloodPressure(){
        return this.bloodPressureRecord.getValue();
    }

    //**** GET ALL FOR CURRENT USER ****\\
    //This method will return all the full health records for the current user,
    //health records to be considered and return full require all three record
    //types to have an entry. This method will also accept an update cache boolean
    //result to see if this should update the cache, if not then it just returns the
    //cache.
    public static ObservableList<HealthRecord> getAllForCurrentUser(boolean updateCache){

        //if we are not updating the cache then simply return the cache value
        if(!updateCache){
            return HealthRecord.cache;
        }

        //Get all our records for this user
        ObservableList<Record> temperatures = Record.where("type","Temperature").withCurrentUser().get();
        ObservableList<Record> weights = Record.where("type","Weight").withCurrentUser().get();
        ObservableList<Record> bloodPressure = Record.where("type","BloodPressure").withCurrentUser().get();

        //create our health records hashmap
        HashMap<String, ArrayList<Record>> healthRecords = new HashMap<>();

        //for each temperature we check to see if the other records are valid for this date
        temperatures.forEach((n)->{

            //set the date and create our new records array
            String date = n.getDate().toString();
            ArrayList<Record> records = new ArrayList<>();

            //add this to the records
            records.add(n);

            //check the weights to see if It's for this date, if so add it
            weights.forEach((weight)->{
                if(weight.getDate().toString().equals(date)){
                    records.add(weight);
                }
            });

            //check the blood pressure to see if It's for this date, if so add it.
            bloodPressure.forEach((bpRecord)->{
                if(bpRecord.getDate().toString().equals(date)){
                    records.add(bpRecord);
                }
            });

            //next we check to see if all three records were added, if so then add it to the health records array.
            if(records.size() == 3){
                healthRecords.put(date,records);
            }

        });

        //declare our models array.
        ObservableList<HealthRecord> models = FXCollections.observableArrayList();

        //for each of our health records we add them
        healthRecords.forEach((k,v)->{
            String value = v.get(0).getValue()+","+v.get(1).getValue()+","+v.get(2).getValue();
            models.add(new HealthRecord(models.size(),"HealthRecord", MyHealth.getInstance().getUser().getId(),value,k,null));
        });

        //next we update the cache to be this new models array
        HealthRecord.cache = models;

        //lastly we return the models.
        return models;
    }
}
