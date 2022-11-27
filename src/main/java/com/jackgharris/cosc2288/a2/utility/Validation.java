//**** PACKAGE ****\\
package com.jackgharris.cosc2288.a2.utility;

//**** START VALIDATION CLASS ****\\
public class Validation {

    //**** STATIC IS FLOAT METHOD ****\\
    //This static helper method accepts a string and will
    //return true or false based on if it's a valid float
    //value or not.
    public static boolean isFloat(String text){

        //if its null return false
        if(text == null){
            return false;
        }

        //trim any white space
        text = text.trim();

        //if its blank or empty return false
        if(text.isBlank() || text.isEmpty()){
            return false;
        }

        //open our try catch and attempt to convert the string
        try{
            Float.parseFloat(text);
        }catch (Exception e){
            //if we catch an error return false
            return false;
        }

        //else if we reach this step its valid, and we return true.
        return true;
    }

    //**** STATIC IS INTEGER METHOD ****\\
    //This static helper method accepts a string and will
    //return true or false based on if it's a valid integer
    //value or not.
    public static boolean isInteger(String text){

        //if null return false
        if(text == null){
            return false;
        }

        //trim any white space
        text = text.trim();

        //if blank or empty return false
        if(text.isBlank() || text.isEmpty()){
            return false;
        }

        //open our try catch and attempt to convert the string
        try{
            Integer.parseInt(text);
        }catch (Exception e){
            //if we catch an error return false
            return false;
        }

        //else if we reach this step its valid, and we return true.
        return true;
    }

    //**** STATIC IS BLOOD PRESSURE METHOD ****\\
    //This static helper method accepts a string and will
    //return true or false based on if it's a valid blood
    //pressure value or not. target input  = x/y
    public static boolean isBloodPressure(String text){

        //if null return false
        if(text == null){
            return false;
        }

        //trim any white space
        text = text.trim();

        //if blank or empty return false
        if(text.isBlank() || text.isEmpty()){
            return false;
        }

        //check if we contain a slash, if not return false
        if(!text.contains("/")){
            return false;
        }

        //explode our data at the slash
        String[] data = text.split("/");

        //if we do not have two entrys then return false
        if(data.length != 2){
            return false;
        }

        //finally we return true or false based on if both of the inputs are integers.
        return Validation.isInteger(data[0]) & Validation.isInteger(data[1]);
    }
}
