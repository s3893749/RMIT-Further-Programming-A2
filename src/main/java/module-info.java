module com.jackgharris.cosc2288.a2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.xerial.sqlitejdbc;
    requires java.sql;
    requires org.apache.commons.codec;

    exports com.jackgharris.cosc2288.a2.core;
    exports com.jackgharris.cosc2288.a2.controllers;
    exports com.jackgharris.cosc2288.a2.models;
    exports com.jackgharris.cosc2288.a2.controllers.components;

    opens com.jackgharris.cosc2288.a2.models to javafx.base;
    opens com.jackgharris.cosc2288.a2.core to javafx.fxml;
    opens com.jackgharris.cosc2288.a2.controllers to javafx.fxml;
    opens com.jackgharris.cosc2288.a2.controllers.components to javafx.fxml;
    exports com.jackgharris.cosc2288.a2.controllers.components.popups;
    opens com.jackgharris.cosc2288.a2.controllers.components.popups to javafx.fxml;
}