package pl.mr.checkers.client.gui.utils;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pl.mr.checkers.client.UserSession;
import pl.mr.checkers.client.gui.AbstractController;
import pl.mr.checkers.client.SceneNames;
import pl.mr.checkers.model.GamePackage;
import pl.mr.checkers.model.PackageType;
import javafx.util.Pair;

import java.io.IOException;

public class Methods {
    public Pair<String, Object> sendPackage(AbstractController controller, PackageType packageType, Object content) {
        GamePackage sendPackage = new GamePackage();
        sendPackage.setType(packageType);
        sendPackage.setUser(UserSession.LOGIN);
        sendPackage.setContent(content);

        GamePackage getPackage;
        try {
            getPackage = controller.sendToServer(sendPackage);
        } catch (Exception e) {
            System.out.println(e.getMessage());

            //RIP=Serwera nie odnaleziono
            return new Pair<>("RIP", null);
        }

        return new Pair<>(getPackage.getResult(), getPackage.getContent());
    }

    public void goToScene(SceneNames sceneName, Stage stage, ActionEvent event) throws IOException {
        Parent root = null;

        switch (sceneName) {
            case LOGIN_SCENE:
                root = FXMLLoader.load(getClass().getResource("/fxml/loginPanel.fxml"));
                break;
            case MENU_SCENE:
                root = FXMLLoader.load(getClass().getResource("/fxml/gameMenu.fxml"));
                break;
            case GAME_SCENE:
                root = FXMLLoader.load(getClass().getResource("/fxml/game.fxml"));
                break;
        }

        Scene scene = new Scene(root);

        Stage window;
        if (stage != null) {
            window = stage;
            scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
        } else if (event != null) {
            window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        } else {
            throw new IOException();
        }

        window.setScene(scene);
        window.show();
    }
}
