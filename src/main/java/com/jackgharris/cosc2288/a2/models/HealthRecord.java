package com.jackgharris.cosc2288.a2.models;

import com.jackgharris.cosc2288.a2.core.MyHealth;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.HashMap;

public class HealthRecord extends Record{

    private Float temperature;
    private Float weight;
    private int systolic;
    private int diastolic;

    private Record weightRecord;
    private Record temperatureRecord;
    private Record bloodPressureRecord;

    private String bloodPressure;

    private static ObservableList<HealthRecord> cache;

    public HealthRecord(int id, String type, int userId, String value, String date) {
        super(id, type, userId, value, date);

        String[] values = value.split(",");
        this.temperature = Float.parseFloat(values[0]);
        this.weight = Float.parseFloat(values[1]);
        this.bloodPressure =values[2];
        this.systolic = Integer.parseInt(values[2].split("/")[0]);
        this.diastolic = Integer.parseInt(values[2].split("/")[1]);

        this.weightRecord = Record.where("type","Weight").withCurrentUser().where("date",date).get().get(0);
        this.temperatureRecord = Record.where("type","Temperature").withCurrentUser().where("date",date).get().get(0);
        Record bloodPressureRaw = Record.where("type","BloodPressure").withCurrentUser().where("date",date).get().get(0);
        this.bloodPressureRecord = new BloodPressure(bloodPressureRaw.getId(),bloodPressureRaw.getType(),bloodPressureRaw.getUserId(),bloodPressureRaw.getValue(),bloodPressureRaw.getDate().toString());
    }

    public Record getWeightRecord(){
        return this.weightRecord;
    }

    public Record getTemperatureRecord(){
        return this.temperatureRecord;
    }

    public Record getBloodPressureRecord(){
        return this.bloodPressureRecord;
    }

    public Float getTemperature(){
        return this.temperature;
    }

    public Float getWeight(){
        return this.weight;
    }

    public String getBloodPressure(){
        return this.bloodPressure;
    }

    public static ObservableList<HealthRecord> getAllForCurrentUser(boolean updateCache){

        if(!updateCache){
            return HealthRecord.cache;
        }

        ObservableList<Record> temperatures = Record.where("type","Temperature").withCurrentUser().get();
        ObservableList<Record> weights = Record.where("type","Weight").withCurrentUser().get();
        ObservableList<Record> bloodPressure = Record.where("type","BloodPressure").withCurrentUser().get();

        HashMap<String, ArrayList<Record>> healthRecords = new HashMap<>();

        temperatures.forEach((n)->{
            String date = n.getDate().toString();
            ArrayList<Record> records = new ArrayList<>();

            records.add(n);

            weights.forEach((weight)->{
                if(weight.getDate().toString().equals(date)){
                    records.add(weight);
                }
            });

            bloodPressure.forEach((bpRecord)->{
                if(bpRecord.getDate().toString().equals(date)){
                    records.add(bpRecord);
                }
            });

            if(records.size() == 3){
                healthRecords.put(date,records);
            }

        });

        ObservableList<HealthRecord> models = FXCollections.observableArrayList();

        healthRecords.forEach((k,v)->{
            String value = v.get(0).getValue()+","+v.get(1).getValue()+","+v.get(2).getValue();
            models.add(new HealthRecord(models.size(),"HealthRecord", MyHealth.getInstance().getUser().getId(),value,k));
        });

        HealthRecord.cache = models;
        return models;
    }
}
