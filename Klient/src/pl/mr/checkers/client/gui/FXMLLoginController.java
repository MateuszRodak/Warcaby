package pl.mr.checkers.client.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import pl.mr.checkers.client.UserSession;
import pl.mr.checkers.client.gui.utils.LoginMethods;
import pl.mr.checkers.client.SceneNames;

import java.io.IOException;


public class FXMLLoginController extends AbstractController {

    @FXML
    private TextField serverIP;
    @FXML
    private TextField serverPort;
    @FXML
    private TextField yourName;
    @FXML
    private Label errorMessage;

    LoginMethods loginMethods;

    public FXMLLoginController() {
        loginMethods = new LoginMethods();
    }

    /**
     * Przycisk logowania
     */
    @FXML
    public void login(ActionEvent event) throws IOException {
        errorMessage.setText("");

        UserSession.LOGIN = yourName.getText();
        UserSession.SERVER_IP = serverIP.getText();
        UserSession.SERVER_PORT = Integer.parseInt(serverPort.getText());

        // sprawdzenie unikalnosci loginu
        boolean isWrongLogin = loginMethods.isWrongLogin(this, errorMessage);

        if (isBadValidate() || isWrongLogin) {
            return;
        }

        // wyświetl menu po poprawnym zalogowaniu
        loginMethods.goToScene(SceneNames.MENU_SCENE, null, event);
    }


    /**
     * sprawdzanie czy pola nie są puste
     */
    private boolean isBadValidate() {
        if (serverIP.getText().isBlank() || serverPort.getText().isBlank() || yourName.getText().isBlank()) {
            errorMessage.setText("Fill all fields!");
            return true;
        }
        return false;
    }

    /**
     * metoda timera wykonywana cały czas
     */
    @Override
    protected void completeTask() {
        //noop
    }
}
