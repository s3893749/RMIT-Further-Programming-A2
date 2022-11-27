package com.jackgharris.cosc2288.a2.tests;

import com.jackgharris.cosc2288.a2.utility.Validation;
import org.junit.Assert;
import org.junit.Test;

public class ValidationUtilityTests {

    @Test
    public void validationUtilityIsFloatWithStringInput(){
        String value = "not a float";
        boolean outcome = Validation.isFloat(value);

        Assert.assertFalse(outcome);
    }

    @Test
    public void validationUtilityIsFloatWithValidFloat(){
        String value = "1.4";
        boolean outcome= Validation.isFloat(value);

        Assert.assertTrue(outcome);
    }

    @Test
    public void validationUtilityIsFloatWithInteger(){
        String value = "1";

        boolean outcome = Validation.isFloat(value);

        Assert.assertTrue(outcome);
    }

    @Test
    public void validationUtilityIsFloatWithNull(){
        String value =  null;

        boolean outcome = Validation.isFloat(value);

        Assert.assertFalse(outcome);
    }

    @Test
    public void validationUtilityIsFloatWithBlankString(){
        String value = "";

        boolean outcome = Validation.isFloat(value);

        Assert.assertFalse(outcome);
    }

    @Test
    public void validationUtilityIsIntegerWithStringInput(){
        String value = "not a integer";

        boolean outcome = Validation.isInteger(value);

        Assert.assertFalse(outcome);
    }

    @Test
    public void validationUtilityIsIntegerWithValidInteger(){
        String value = "1";

        boolean outcome = Validation.isInteger(value);

        Assert.assertTrue(outcome);
    }

    @Test
    public void validationUtilityIsIntegerWithFloat(){
        String value = "1.5";

        boolean outcome = Validation.isInteger(value);

        Assert.assertFalse(outcome);
    }

    @Test
    public void validationUtilityIsIntegerWithNull(){
        String value  = null;

        boolean outcome = Validation.isInteger(value);

        Assert.assertFalse(outcome);
    }

    @Test
    public void validationUtilityIsIntegerWithBlankString(){
        String value = "";

        boolean outcome = Validation.isInteger(value);

        Assert.assertFalse(outcome);
    }

    @Test
    public void validationUtilityIsBloodPressureWithString(){
        String value  = "Not a blood pressure";

        boolean outcome = Validation.isBloodPressure(value);

        Assert.assertFalse(outcome);
    }

    @Test
    public void validationUtilityIsBloodPressureWithValidBloodPressure(){
        String value = "120/80";

        boolean outcome = Validation.isBloodPressure(value);

        Assert.assertTrue(outcome);
    }

    @Test
    public void validationUtilityIsBloodPressureWithNull(){
        String value = null;

        boolean outcome = Validation.isBloodPressure(value);

        Assert.assertFalse(outcome);
    }

    @Test
    public void validationUtilityIsBloodPressureWithBlankString(){
        String value = "";

        boolean outcome = Validation.isBloodPressure(value);

        Assert.assertFalse(outcome);
    }




}
