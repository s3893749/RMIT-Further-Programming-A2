package com.jackgharris.cosc2288.a2.tests;

import com.jackgharris.cosc2288.a2.core.MyHealth;
import com.jackgharris.cosc2288.a2.models.Record;
import com.jackgharris.cosc2288.a2.models.RecordCollection;
import com.jackgharris.cosc2288.a2.models.User;
import com.jackgharris.cosc2288.a2.utility.Resource;
import com.jackgharris.cosc2288.a2.utility.Time;
import javafx.collections.ObservableList;
import org.junit.Assert;
import org.junit.Test;

public class RecordCollectionModelTests {

    public RecordCollectionModelTests(){
        MyHealth app = new MyHealth();
        app.setUser(User.getByEmail("junit@testing.com"));
    }

    @Test
    public void recordCollectionLimit(){

        Record record = new Record(-1,"type",MyHealth.getInstance().getUser().getId(),"110","2022-11-26", Time.now());
        Record record2 = new Record(-1,"type",MyHealth.getInstance().getUser().getId(),"120","2022-11-27", Time.now());

        Record.add(record);
        Record.add(record2);

        RecordCollection recordCollection = new RecordCollection();

        ObservableList<Record> result = recordCollection.limit(1).get();

        Assert.assertEquals(result.size(),1);

        Record.withCurrentUser().get().forEach(Record::delete);

    }

    @Test
    public void recordCollectionWhere(){

        Record record = new Record(-1,"Weight",MyHealth.getInstance().getUser().getId(),"110","2022-11-26", Time.now());
        Record record2 = new Record(-1,"Weight",MyHealth.getInstance().getUser().getId(),"120","2022-11-27", Time.now());

        Record.add(record);
        Record.add(record2);

        ObservableList<Record> result = new RecordCollection().where("date","2022-11-26").where("type","Weight").withCurrentUser().get();

        Assert.assertEquals(1,result.size());

        Record.withCurrentUser().get().forEach(Record::delete);
    }

    @Test
    public void recordCollectionSort(){
        Record.withCurrentUser().get().forEach(Record::delete);


        Record.add(new Record(-1,"Weight",MyHealth.getInstance().getUser().getId(),"120","2022-11-26", Time.now()));
        Record.add(new Record(-1,"Weight",MyHealth.getInstance().getUser().getId(),"110","2022-11-26", Time.now()));

        ObservableList<Record> results = Record.withCurrentUser().where("type","Weight").where("date","2022-11-26").sort("value").get();

        Assert.assertEquals("110", results.get(0).getValue());
    }

}
