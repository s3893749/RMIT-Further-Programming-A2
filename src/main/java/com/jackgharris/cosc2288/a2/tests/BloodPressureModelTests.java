package com.jackgharris.cosc2288.a2.tests;

import com.jackgharris.cosc2288.a2.core.MyHealth;
import com.jackgharris.cosc2288.a2.models.BloodPressure;
import com.jackgharris.cosc2288.a2.models.Record;
import com.jackgharris.cosc2288.a2.models.User;
import com.jackgharris.cosc2288.a2.utility.Time;
import org.junit.Assert;
import org.junit.Test;

public class BloodPressureModelTests {


    public BloodPressureModelTests(){
        MyHealth app = new MyHealth();
        app.setUser(User.getByEmail("junit@testing.com"));
    }

    @Test
    public void addBloodPressureAndDelete(){

        String value = "120/80";

        BloodPressure bloodPressure = new BloodPressure(-1,"BloodPressure",MyHealth.getInstance().getUser().getId(),value,"2022-11-26", Time.now());

        Record.add(bloodPressure);

        Record result = Record.where("type","BloodPressure").withCurrentUser().limit(1).get().get(0);

        Assert.assertEquals(result.getValue(),value);

        Record.delete(result);
    }

    @Test
    public void getBloodPressureSystolic(){

        String value = "120/80";

        BloodPressure bloodPressure = new BloodPressure(-1,"BloodPressure",MyHealth.getInstance().getUser().getId(),value,"2022-11-26", Time.now());

        Record.add(bloodPressure);

        Record result = Record.where("type","BloodPressure").withCurrentUser().limit(1).get().get(0);

        BloodPressure bloodPressure1 = new BloodPressure(result.getId(),result.getType(),result.getUserId(),result.getValue(),result.getDate().toString(), Time.now());

        Assert.assertEquals(120, (int) bloodPressure1.getSystolic());

        Record.delete(result);
    }

    @Test
    public void getBloodPressureDiastolic(){

        String value = "120/80";

        BloodPressure bloodPressure = new BloodPressure(-1,"BloodPressure",MyHealth.getInstance().getUser().getId(),value,"2022-11-26", Time.now());

        Record.add(bloodPressure);

        Record result = Record.where("type","BloodPressure").withCurrentUser().limit(1).get().get(0);

        BloodPressure bloodPressure1 = new BloodPressure(result.getId(),result.getType(),result.getUserId(),result.getValue(),result.getDate().toString(), Time.now());

        Assert.assertEquals(80, (int) bloodPressure1.getDiastolic());

        Record.delete(result);
    }

}
