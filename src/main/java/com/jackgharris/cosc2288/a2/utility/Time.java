//**** PACKAGE ****\\
package com.jackgharris.cosc2288.a2.utility;

//**** PACKAGE IMPORTS ****\\
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

//**** START TIME CLASS ****\\
//The time class provides an easy way for any caller to get the current
//time as a readable string
public class Time {

    //**** STATIC NOW METHOD ****\\
    //The now method returns the current time in a human-readable string
    //format.
    public static String now(){

        //get the current date from the unix time stamp
        Date date = Date.from(Instant.ofEpochSecond(Instant.now().getEpochSecond()));

        //return the formatted date.
        return new SimpleDateFormat("h:mm a").format(date);
    }

}
