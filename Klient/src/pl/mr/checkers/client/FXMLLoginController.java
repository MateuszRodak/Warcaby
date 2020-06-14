package pl.mr.checkers.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class FXMLLoginController {

    public void changeLoginScene(ActionEvent event) throws IOException {
        Parent dupa = FXMLLoader.load(getClass().getResource("gameMenu.fxml"));
        Scene scene = new Scene(dupa);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }


}
