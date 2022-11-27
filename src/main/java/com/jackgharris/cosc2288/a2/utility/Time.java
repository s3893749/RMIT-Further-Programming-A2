package com.jackgharris.cosc2288.a2.utility;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

public class Time {

    public static String now(){
        Date date = Date.from(Instant.ofEpochSecond(Instant.now().getEpochSecond()));
        return new SimpleDateFormat("h:mm a").format(date);
    }

}
