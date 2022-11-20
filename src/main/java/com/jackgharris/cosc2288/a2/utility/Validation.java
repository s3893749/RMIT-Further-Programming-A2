package com.jackgharris.cosc2288.a2.utility;

public class Validation {

    public static boolean isFloat(String text){

        text = text.trim();

        if(text.isBlank() || text.isEmpty()){
            return false;
        }

        try{
            Float.parseFloat(text);
        }catch (Exception e){
            return false;
        }

        return true;
    }
}
