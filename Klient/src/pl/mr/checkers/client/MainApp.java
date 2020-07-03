package pl.mr.checkers.client;

import javafx.application.Application;
import javafx.stage.Stage;
import pl.mr.checkers.client.gui.utils.Methods;


public class MainApp extends Application {


    @Override
    public void start(Stage window) throws Exception {
        Methods methods = new Methods();
        methods.goToScene(SceneNames.LOGIN_SCENE, window, null);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
