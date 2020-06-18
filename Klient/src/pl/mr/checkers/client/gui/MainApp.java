package pl.mr.checkers.client.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class MainApp extends Application {

    @Override
    public void start(Stage mojeStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("loginPanel.fxml"));

        Scene mojaScena = new Scene(root);
        mojaScena.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

        mojeStage.setTitle("Checkers the game");
        mojeStage.setScene(mojaScena);
        mojeStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
