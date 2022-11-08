package com.jackgharris.cosc2288.a2.utility;

import com.jackgharris.cosc2288.a2.core.MyHealth;
import javafx.scene.image.Image;

import java.util.Objects;

public class Resource {

    public static Image favicon(){
        return new Image(Objects.requireNonNull(MyHealth.class.getResourceAsStream("favicon.png")));
    };

}
