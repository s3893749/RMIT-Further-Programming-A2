package com.jackgharris.cosc2288.a2.utility;

import javafx.scene.control.ColorPicker;
import javafx.scene.paint.Color;

public class ColorUtility {

    public static String getHexStringFromColorPicker(ColorPicker colorPicker){

        Color color = colorPicker.getValue();

        int r = ((int) Math.round(color.getRed()     * 255)) << 24;
        int g = ((int) Math.round(color.getGreen()   * 255)) << 16;
        int b = ((int) Math.round(color.getBlue()    * 255)) << 8;
        int a = ((int) Math.round(color.getOpacity() * 255));

        return String.format("#%08X", (r + g + b + a));

    }

}
