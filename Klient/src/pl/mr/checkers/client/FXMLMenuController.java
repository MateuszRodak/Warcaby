package pl.mr.checkers.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class FXMLMenuController {

    @FXML
    private Label sendMessage;
    @FXML
    private Label makeMove;
    @FXML
    private Label changeNick;
    @FXML
    private Label joinLobby;
    @FXML
    private Label newLobby;
    @FXML
    private Label nazwaGracza;
    @FXML
    private TextField send_text;

    public void changeLoginScene(ActionEvent event) throws IOException {
        Parent dupa = FXMLLoader.load(getClass().getResource("gameMenu.fxml"));
        Scene scene = new Scene(dupa);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    public void sendMessage(){
        sendMessage.setText("<-----Nie Działa");
        SocketClient socketClient = new SocketClient();
        socketClient.openSocket();

        socketClient.sendMessage(send_text.getText());

        String recievedMessage = socketClient.readMessage();
        nazwaGracza.setText(recievedMessage);

        socketClient.closeSocket();

        sendMessage.setText("<-----Działa");
    }

    public void makeMove(){
        makeMove.setText("I moved my ass, your turn!");
    }
    public void changeNick(){
        changeNick.setText("the nick has been changed");
    }
    public void joinLobby(){
        joinLobby.setText("Connecting...");
    }
    public void newLobby(){
        newLobby.setText("Creating...");
    }
}
