module Klient {
    requires javafx.controls;
    requires javafx.fxml;

    opens pl.mr.checkers.client.gui to javafx.fxml;
    exports pl.mr.checkers.client.gui;
}