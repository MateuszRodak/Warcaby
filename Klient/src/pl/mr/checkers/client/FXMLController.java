package pl.mr.checkers.client;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class FXMLController {

    @FXML
    private Label label;
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

    public void initialize() {
        String javaVersion = System.getProperty("java.version");
        String javafxVersion = System.getProperty("javafx.version");
        label.setText("Hello, JavaFX " + javafxVersion + "\nRunning on Java " + javaVersion + ".");
    }

    public void sendMessage(){
        sendMessage.setText("We will send the message to the universe!");
        SocketClient socketClient = new SocketClient("192.168.1.20",9876);
        socketClient.sendMessage(send_text.getText());

        String recievedMessage = socketClient.readMessage();
        nazwaGracza.setText(recievedMessage);

        socketClient.closeSocket();
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
