package pl.mr.checkers.client.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import pl.mr.checkers.client.SocketClient;
import pl.mr.checkers.client.UserSession;

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
    private Label userName;
    @FXML
    private TextField send_text;

    public void init(MouseEvent event)  {
        userName.setText(UserSession.LOGIN);

    }

    public void sendMessage(){
        sendMessage.setText("<-----Nie Działa");
        SocketClient socketClient = new SocketClient();
        socketClient.openSocket();

        socketClient.sendMessage(send_text.getText());

        String recievedMessage = socketClient.readMessage();
        userName.setText(recievedMessage);

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
