package pl.mr.checkers.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class MainApp extends Application {

    @Override
    public void start(Stage mojeStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("scene.fxml"));

        Scene mojaScena = new Scene(root);
        mojaScena.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

        mojeStage.setTitle("JavaFX 11 is the Title");
        mojeStage.setScene(mojaScena);
        mojeStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    void wyslijwiadomosc(){
        //funkcja1
    }
    void wykonanieruchu(){
        //funkcja2
    }
    void zmiennick(){
        //funkcja3
    }
    void dolaczdostolu(){
        //funkcja4
    }
    void nowystol(){
        //funkcja5
    }
}
