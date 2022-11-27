//**** PACKAGE ****\\
package com.jackgharris.cosc2288.a2.utility;

//**** PACKAGE IMPORTS ****\\
import javafx.scene.control.ColorPicker;
import javafx.scene.paint.Color;

//**** START COLOR UTILITY CLASS ****\\
public class ColorUtility {

    //**** STATIC GET HEX STRING FROM COLOR PICKER METHOD ****
    //This method will accept a color picker node and return the hex value from
    //its select color.
    public static String getHexStringFromColorPicker(ColorPicker colorPicker){

        //Get the color from the text picker
        Color color = colorPicker.getValue();

        //Get the RGBA values from the color
        int r = ((int) Math.round(color.getRed()     * 255)) << 24;
        int g = ((int) Math.round(color.getGreen()   * 255)) << 16;
        int b = ((int) Math.round(color.getBlue()    * 255)) << 8;
        int a = ((int) Math.round(color.getOpacity() * 255));

        //return the hex value
        return String.format("#%08X", (r + g + b + a));
    }

}
