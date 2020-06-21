package pl.mr.checkers.client.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pl.mr.checkers.client.UserSession;
import pl.mr.checkers.model.GamePackage;
import pl.mr.checkers.model.PackageType;

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

    public void login(ActionEvent event) throws IOException {

        errorMessage.setText("");

//        sprawdzenie unikalnosci loginu
        if (isBadValidate() || isWrongLogin()) {
            return;
        }

        UserSession.LOGIN = yourName.getText();

        Parent menu = FXMLLoader.load(getClass().getResource("gameMenu.fxml"));
        Scene scene = new Scene(menu);

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    private boolean isWrongLogin() {
        GamePackage sendPackage = new GamePackage();
        sendPackage.setType(PackageType.LOGIN);
        sendPackage.setUser(yourName.getText());

        GamePackage getPackage;
        try {
            getPackage = sendToServer(sendPackage);
        } catch (Exception e) {
            e.printStackTrace();
            errorMessage.setText("Server error connection");
            return true;
        }

        String result = getPackage.getResult();
        if (result.equals("OK")) {
            return false;
        } else {
            System.out.println(getPackage.getResult());
            errorMessage.setText("This name already exists");
            return true;
        }
    }

    private boolean isBadValidate() {
        if (serverIP.getText().isBlank() || serverPort.getText().isBlank() || yourName.getText().isBlank()) {
            errorMessage.setText("Fill all fields!");
            return true;
        }
        return false;
    }
}
