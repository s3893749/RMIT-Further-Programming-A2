package com.jackgharris.cosc2288.a2.utility;

import com.jackgharris.cosc2288.a2.core.MyHealth;
import javafx.scene.image.Image;

import java.util.Objects;

public class Resource {

    public static Image favicon(){
        return new Image(Objects.requireNonNull(MyHealth.class.getResourceAsStream("favicon.png")));
    }

    public static Image importExportFavicon(){
        return new Image(Objects.requireNonNull(MyHealth.class.getResourceAsStream("icons/sync_icon_alt.png")));
    }

    public static Image faviconWarning(){
        return new Image(Objects.requireNonNull(MyHealth.class.getResourceAsStream("icons/warning_icon.png")));
    }

}
