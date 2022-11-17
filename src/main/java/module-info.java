module com.jackgharris.cosc2288.a2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.xerial.sqlitejdbc;
    requires java.sql;
    requires commons.validator;

    exports com.jackgharris.cosc2288.a2.core;
    exports com.jackgharris.cosc2288.a2.controllers;
    exports com.jackgharris.cosc2288.a2.models;
    exports com.jackgharris.cosc2288.a2.controllers.components;

    opens com.jackgharris.cosc2288.a2.core to javafx.fxml;
    opens com.jackgharris.cosc2288.a2.controllers to javafx.fxml;
    opens com.jackgharris.cosc2288.a2.controllers.components to javafx.fxml;
    opens com.jackgharris.cosc2288.a2.models to javafx.base, javafx.fxml;
}