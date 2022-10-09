module com.jackgharris.cosc2288.a2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.xerial.sqlitejdbc;

    exports com.jackgharris.cosc2288.a2.core;
    exports com.jackgharris.cosc2288.a2.controllers;

    opens com.jackgharris.cosc2288.a2.core to javafx.fxml;
    opens com.jackgharris.cosc2288.a2.controllers to javafx.fxml;
}