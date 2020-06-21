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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import pl.mr.checkers.client.UserSession;
import pl.mr.checkers.model.Game;
import pl.mr.checkers.model.GameInfo;
import pl.mr.checkers.model.GamePackage;
import pl.mr.checkers.model.PackageType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class FXMLMenuController extends AbstractController {
    private boolean initialized;

    @FXML
    private Label userName;
    @FXML
    private ListView<String> gameList;
    @FXML
    private ListView<String> playerList;
    @FXML
    private Label errorMessage;
//    @FXML
//    private Button joinToGame;
//    @FXML
//    private Button createTable;
    @FXML
    private TextField newTableName;
    @FXML
    private Label selectedGameName;

    public void init(MouseEvent event) {
        if (initialized) {
            return;
        }
        errorMessage.setText("");
        userName.setText(UserSession.LOGIN);
        List<String> userList = getUserList();
        ObservableList<String> observableUserList = FXCollections.observableList(userList);
        playerList.setItems(observableUserList);

        List<String> gameListStrings = getGameList();
        ObservableList<String> observableGameList = FXCollections.observableList(gameListStrings);
        gameList.setItems(observableGameList);

        gameList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                int pending = newValue.indexOf("???");
                if (!(pending >0)){
                    selectedGameName.setText("");
                    return;
                }

                int indexOf = newValue.indexOf(":");
                String substring = newValue.substring(0, indexOf);
                selectedGameName.setText(substring);
//                System.out.println("Selected item: " + oldValue);
//                System.out.println("Selected item: " + newValue);
            }
        });
        initialized = true;
    }

    private List<String> getUserList() {
        GamePackage sendPackage = new GamePackage();
        sendPackage.setType(PackageType.GET_USER_LIST);
        sendPackage.setUser(UserSession.LOGIN);

        GamePackage getPackage = null;
        try {
            getPackage = sendToServer(sendPackage);
            if (!getPackage.getResult().equals("OK")) {
                System.out.println(getPackage.getResult());
                errorMessage.setText("Can't download user list");
                return Collections.EMPTY_LIST;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Set<String> stringSet = (Set<String>) getPackage.getContent();
        return new ArrayList<>(stringSet);
    }

    private List<String> getGameList() {
        GamePackage sendPackage = new GamePackage();
        sendPackage.setType(PackageType.GET_GAME_LIST);
        sendPackage.setUser(UserSession.LOGIN);

        GamePackage getPackage = null;
        try {
            getPackage = sendToServer(sendPackage);
            if (!getPackage.getResult().equals("OK")) {
                System.out.println(getPackage.getResult());
                errorMessage.setText("Can't download game list");
                return Collections.EMPTY_LIST;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<String> stringList = new ArrayList<>();
        String nameGame;
        List<GameInfo> infoList = (List<GameInfo>) getPackage.getContent();

        for (GameInfo gameInfo : infoList) {
            String player2 = gameInfo.getPlayers()[1];

            if (player2 == null) {
                player2 = "???";
            }

            nameGame = gameInfo.getTableName().toUpperCase() + ": [" + gameInfo.getPlayers()[0] + " vs " + player2 + "]";
            stringList.add(nameGame);
        }
        return stringList;
    }
//
//    public void selectGame(MouseEvent event) {
////        System.out.println("cokolwiek");
////        System.out.println(event.getEventType());
////        System.out.println(event.getEventType().getName());
//    }

    public void createGame(ActionEvent event) throws IOException {
        if (newTableName.getText().equals("")) {
            errorMessage.setText("Pusta nazwa");
            return;
        }
        String tableName = newTableName.getText().toUpperCase();
        GamePackage sendPackage = new GamePackage();
        sendPackage.setType(PackageType.CREATE_NEW_GAME);
        sendPackage.setUser(UserSession.LOGIN);
        sendPackage.setContent(tableName);

        GamePackage getPackage;
        try {
            getPackage = sendToServer(sendPackage);
            if (!getPackage.getResult().equals("OK")) {
                System.out.println(getPackage.getResult());
                errorMessage.setText("Can't create new game");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        UserSession.GAME = (Game) getPackage.getContent();
        UserSession.GAME_NAME = tableName;
        play(event);
    }

    public void join(ActionEvent event) throws IOException {
        String gameNameText = selectedGameName.getText();
        GamePackage sendPackage = new GamePackage();
        sendPackage.setType(PackageType.ACCESS_TO_GAME);
        sendPackage.setUser(UserSession.LOGIN);
        sendPackage.setContent(gameNameText);

        GamePackage getPackage;
        try {
            getPackage = sendToServer(sendPackage);
            if (!getPackage.getResult().equals("OK")) {
                System.out.println(getPackage.getResult());
                errorMessage.setText("Can't join to stupid game");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        UserSession.GAME = (Game) getPackage.getContent();
        UserSession.GAME_NAME = gameNameText;
        play(event);
    }

    private void play(ActionEvent event) throws IOException {
        Parent menu = FXMLLoader.load(getClass().getResource("game.fxml"));
        Scene scene = new Scene(menu);

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
}
