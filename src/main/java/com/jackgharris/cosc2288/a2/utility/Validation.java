package com.jackgharris.cosc2288.a2.utility;

public class Validation {

    public static boolean isFloat(String text){
        if(text == null){
            return false;
        }

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

    public static boolean isInteger(String text){
        if(text == null){
            return false;
        }

        text = text.trim();

        if(text.isBlank() || text.isEmpty()){
            return false;
        }

        try{
            Integer.parseInt(text);
        }catch (Exception e){
            return false;
        }

        return true;
    }
}
