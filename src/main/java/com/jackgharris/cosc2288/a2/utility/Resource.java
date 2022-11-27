//**** PACKAGE ****\\
package com.jackgharris.cosc2288.a2.utility;

//**** PACKAGE IMPORTS ****\\
import com.jackgharris.cosc2288.a2.core.MyHealth;
import javafx.scene.image.Image;

import java.util.Objects;


//**** START RESOURCE CLASS ****\\
//The resource class provides static access to specific resources such as art
public class Resource {

    //**** STATIC FAVICON METHOD ****\\
    //This method returns the main favicon icon for MyHealth
    public static Image favicon(){
        return new Image(Objects.requireNonNull(MyHealth.class.getResourceAsStream("favicon.png")));
    }

    //**** STATIC IMPORT EXPORT FAVICON METHOD ****\\
    //This method returns the favicon used by the import and export window
    public static Image importExportFavicon(){
        return new Image(Objects.requireNonNull(MyHealth.class.getResourceAsStream("icons/sync_icon_alt.png")));
    }

    //**** STATIC WARNING FAVICON METHOD ****\\
    //This method returns the favicon used by the warning alerts
    public static Image faviconWarning(){
        return new Image(Objects.requireNonNull(MyHealth.class.getResourceAsStream("icons/warning_icon.png")));
    }

}
