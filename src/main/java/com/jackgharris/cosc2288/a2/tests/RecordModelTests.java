package com.jackgharris.cosc2288.a2.tests;

import com.jackgharris.cosc2288.a2.core.MyHealth;
import com.jackgharris.cosc2288.a2.models.Record;
import com.jackgharris.cosc2288.a2.models.User;
import com.jackgharris.cosc2288.a2.utility.Time;
import org.junit.Assert;
import org.junit.Test;

public class RecordModelTests {

    public RecordModelTests(){
        MyHealth app = new MyHealth();
        app.setUser(User.getByEmail("junit@testing.com"));
    }

    @Test
    public void addAndDeleteRecord()
    {
        Record record = new Record(-1,"Temperature",MyHealth.getInstance().getUser().getId(),"104.8","2022-11-26", Time.now());
        Record.add(record);

        Assert.assertNotNull(Record.withCurrentUser().where("value","104.8").where("date","2022-11-26").get().get(0));

        Record.withCurrentUser().get().forEach(Record::delete);
    }

    @Test
    public void getId(){
        Record record = new Record(-523,"Temperature",MyHealth.getInstance().getUser().getId(),"104.8","2022-11-26", Time.now());
        Record.add(record);

        Assert.assertEquals(record.getId(), -523);

        Record.withCurrentUser().get().forEach(Record::delete);
    }

    @Test
    public void getUserId(){
        Record record = new Record(-1,"Temperature",MyHealth.getInstance().getUser().getId(),"104.8","2022-11-26", Time.now());
        Record.add(record);

        Assert.assertEquals(MyHealth.getInstance().getUser().getId(), record.getUserId());

        Record.withCurrentUser().get().forEach(Record::delete);
    }

    @Test
    public void getValue(){
        String value = "104.8";

        Record record = new Record(-1,"Temperature",MyHealth.getInstance().getUser().getId(),value,"2022-11-26", Time.now());
        Record.add(record);

        Assert.assertEquals(record.getValue(), value);

        Record.withCurrentUser().get().forEach(Record::delete);
    }

    @Test
    public void getDate(){
        String date = "2022-11-26";

        Record record = new Record(-1,"Temperature",MyHealth.getInstance().getUser().getId(),"104.8",date, Time.now());
        Record.add(record);

        Assert.assertEquals(record.getDate().toString(), date);

        Record.withCurrentUser().get().forEach(Record::delete);
    }

    @Test
    public void updateRecordDetails(){
        Record record = new Record(-1,"Temperature",MyHealth.getInstance().getUser().getId(),"104.8","2022-11-26", Time.now());
        Record.add(record);
        record = Record.withCurrentUser().where("date","2022-11-26").where("type","Temperature").get().get(0);

        record.setValue("65");
        record.updateDetails();


        Assert.assertEquals(record.getValue(),"65");

        Record.withCurrentUser().get().forEach(Record::delete);
    }
}
