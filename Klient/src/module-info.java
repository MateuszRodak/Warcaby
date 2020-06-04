module Klient {
    requires javafx.controls;
    requires javafx.fxml;

    opens pl.mr.checkers.client to javafx.fxml;
    exports pl.mr.checkers.client;
}