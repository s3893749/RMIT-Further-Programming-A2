package com.jackgharris.cosc2288.a2.tests;

import com.jackgharris.cosc2288.a2.core.Database;
import com.jackgharris.cosc2288.a2.core.MyHealth;
import com.jackgharris.cosc2288.a2.models.HealthRecord;
import com.jackgharris.cosc2288.a2.models.Record;
import com.jackgharris.cosc2288.a2.models.User;
import com.jackgharris.cosc2288.a2.utility.Time;
import javafx.collections.ObservableList;
import org.junit.Assert;
import org.junit.Test;


public class HealthRecordModelTests {

    public HealthRecordModelTests(){
        MyHealth app = new MyHealth();
        app.setUser(User.getByEmail("junit@testing.com"));
    }

    @Test
    public void addAndDeleteHealthRecord(){
        //clear the current records
        Record.withCurrentUser().get().forEach(Record::delete);

        int user_id = MyHealth.getInstance().getUser().getId();
        String date = "2022-11-26";
        String date2 = "2022-11-27";

        Record temperature = new Record(-1,"Temperature",user_id,"37.8",date, Time.now());
        Record weight = new Record(-1,"Weight",user_id,"105.4",date, Time.now());
        Record bloodPressure = new Record(-1,"BloodPressure",user_id,"120/80",date, Time.now());

        Record.add(temperature);
        Record.add(weight);
        Record.add(bloodPressure);

        Record temperature2 = new Record(-1,"Temperature",user_id,"37.8",date2, Time.now());
        Record weight2 = new Record(-1,"Weight",user_id,"105.4",date2, Time.now());
        Record bloodPressure2 = new Record(-1,"BloodPressure",user_id,"120/80",date2, Time.now());


        Record.add(temperature2);
        Record.add(weight2);
        Record.add(bloodPressure2);

        ObservableList<HealthRecord> healthRecord = HealthRecord.getAllForCurrentUser(true);

        Assert.assertEquals(2, healthRecord.size());

        Record.withCurrentUser().get().forEach(Record::delete);
    }

    @Test
    public void addAndGetFromCache(){
        //clear the current records
        Record.withCurrentUser().get().forEach(Record::delete);


        int user_id = MyHealth.getInstance().getUser().getId();
        String date = "2022-11-26";

        Record temperature = new Record(-1,"Temperature",user_id,"37.8",date, Time.now());
        Record weight = new Record(-1,"Weight",user_id,"105.4",date, Time.now());
        Record bloodPressure = new Record(-1,"BloodPressure",user_id,"120/80",date, Time.now());

        Record.add(temperature);
        Record.add(weight);
        Record.add(bloodPressure);

        //reset our database query count
        Database.resetQueryCount();

        //load two from database
        ObservableList<HealthRecord> healthRecord = HealthRecord.getAllForCurrentUser(true);
        ObservableList<HealthRecord> healthRecord2 = HealthRecord.getAllForCurrentUser(true);
        //load two from cache
        ObservableList<HealthRecord> healthRecord3 = HealthRecord.getAllForCurrentUser(false);
        ObservableList<HealthRecord> healthRecord4 = HealthRecord.getAllForCurrentUser(false);

        //6 query's per health record, so two from database is 12
        Assert.assertEquals(12, Database.getQueryCount());

        Record.withCurrentUser().get().forEach(Record::delete);
    }

}
