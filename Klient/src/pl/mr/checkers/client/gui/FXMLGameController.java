package pl.mr.checkers.client.gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import pl.mr.checkers.client.UserSession;
import pl.mr.checkers.model.ChatMassage;
import pl.mr.checkers.model.Game;
import pl.mr.checkers.model.GamePackage;
import pl.mr.checkers.model.PackageType;

import java.io.IOException;
import java.util.Collections;
import java.util.List;


public class FXMLGameController extends AbstractController {

    private boolean initialized;
    @FXML
    private Label user1;
    @FXML
    private Label user2;
    @FXML
    private Label gameName;
    @FXML
    private ListView<ChatMassage> chat;
    @FXML
    private TextField chatMessage;


    public void init(MouseEvent event) {
        if (initialized) {
            return;
        }
//        errorMessage.setText("");
        gameName.setText(UserSession.GAME_NAME);

        Game game = getGame();

        //ustawienie pionków na start
        char[] board = game.getBoard();

        //pobranie graczy grających
        String[] players = game.getPlayers();
        user1.setText(players[0]);
        user2.setText(players[1]);

        //lista wiadomosci na chacie
        List<ChatMassage> chatMassages = game.getChatMassages();

        ///zaslepka
//        ChatMassage chatMassage = new ChatMassage(UserSession.LOGIN, "ble ble");
//        chatMassages.add(chatMassage);

        ObservableList<ChatMassage> observableGameList = FXCollections.observableList(chatMassages);
        chat.setItems(observableGameList);

        initialized = true;
    }

    private Game getGame() {
        GamePackage sendPackage = new GamePackage();
        sendPackage.setType(PackageType.GET_GAME);
        sendPackage.setUser(UserSession.LOGIN);
        sendPackage.setContent(UserSession.GAME_NAME);

        GamePackage getPackage = null;
        try {
            getPackage = sendToServer(sendPackage);
            if (!getPackage.getResult().equals("OK")) {
                System.out.println(getPackage.getResult());
//                errorMessage.setText("Can't download user list");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return (Game) getPackage.getContent();
    }

    public void sendMessage() {
        GamePackage sendPackage = new GamePackage();
        sendPackage.setType(PackageType.SEND_GAME);
        sendPackage.setUser(UserSession.LOGIN);

        ChatMassage chat = new ChatMassage(UserSession.LOGIN, chatMessage.getText());
        UserSession.GAME.getChatMassages().add(chat);
        sendPackage.setContent(UserSession.GAME);

        Game game = UserSession.GAME;

        GamePackage getPackage;
        try {
            getPackage = sendToServer(sendPackage);
            if (!getPackage.getResult().equals("OK")) {
                System.out.println(getPackage.getResult());
//                errorMessage.setText("Can't download user list");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        chatMessage.setText("");
        initialized = false;
        init(null);
    }

    public void goBack(ActionEvent event) throws IOException {
        Parent menu = FXMLLoader.load(getClass().getResource("gameMenu.fxml"));
        Scene scene = new Scene(menu);

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
}
