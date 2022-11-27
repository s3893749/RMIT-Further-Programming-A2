package com.jackgharris.cosc2288.a2.tests;


import com.jackgharris.cosc2288.a2.core.MyHealth;
import com.jackgharris.cosc2288.a2.models.Activity;
import com.jackgharris.cosc2288.a2.models.User;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class ActivityModelTests {


    public ActivityModelTests(){
        MyHealth app = new MyHealth();
        app.setUser(User.getByEmail("junit@testing.com"));
    }

    @Test
    public void addActivityAndDelete(){

        AtomicBoolean outcome = new AtomicBoolean(false);

        Activity activity = new Activity("JUNIT TEST");
        Activity.add(activity);

        Activity.get(100,0,true).forEach((n)->{
            System.out.println(n.getDescription());
            outcome.set(n.getDescription().equals("JUNIT TEST"));
        });

        Assert.assertTrue(outcome.get());

        Activity.delete(activity);
    }

    @Test
    public void getActivityDescription() {

        String description = "JUnit test description";
        Activity activity = new Activity(description);
        Activity.add(activity);

        Assert.assertEquals(Activity.get(10, 0, true).get(0).getDescription(), description);

        Activity.delete(activity);
    }

    @Test
    public void getActivityTime(){
        Activity activity = new Activity("JUnit time test");

        Activity.add(activity);

        Assert.assertNotNull(Activity.get(100,0,true).get(0).getTimeReadable());

        Activity.delete(activity);
    }

    @Test
    public void getActivities(){

        Activity.get(1000,0,false).forEach(Activity::delete);

        ArrayList<Activity> activities = new ArrayList<>();

        activities.add(new Activity("A1"));
        activities.add(new Activity("A2"));
        activities.add(new Activity("A3"));
        activities.add(new Activity("A4"));
        activities.add(new Activity("A5"));

        activities.forEach(Activity::add);

        Assert.assertEquals(Activity.get(5,0,true).size(), 5);

        activities.forEach(Activity::delete);

    }

    @Test
    public void getActivitiesWithOffset(){

        //clear all old activities
        Activity.get(1000,0,false).forEach(Activity::delete);

        ArrayList<Activity> activities = new ArrayList<>();

        activities.add(new Activity("A1"));
        activities.add(new Activity("A2"));
        activities.add(new Activity("A3"));
        activities.add(new Activity("A4"));
        activities.add(new Activity("A5"));

        activities.forEach(Activity::add);

        Assert.assertEquals(2,Activity.get(5,3,true).size());

        activities.forEach(Activity::delete);
    }


}
