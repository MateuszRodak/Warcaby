package pl.mr.checkers.client.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import pl.mr.checkers.client.UserSession;
import pl.mr.checkers.model.GameInfo;
import pl.mr.checkers.model.GamePackage;
import pl.mr.checkers.model.PackageType;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class FXMLMenuController extends AbstractController {

    @FXML
    private Label userName;
    @FXML
    private ListView gameList;
    @FXML
    private ListView playerList;


    public void init(MouseEvent event) {
        userName.setText(UserSession.LOGIN);
        List<String> userList = getUserList2();
        ObservableList<String> observableUserList = FXCollections.observableList(userList);
        playerList.setItems(observableUserList);

        List<String> gameListStrings = getGameList2();
        ObservableList<String> observableGameList = FXCollections.observableList(gameListStrings);
        gameList.setItems(observableGameList);
    }

    private List<String> getUserList2() {
        List list = new ArrayList();
        list.add("jakies");
        list.add("jakiesinne");
        return list;
    }

    private List<String> getGameList2() {
        List list = new ArrayList();
        list.add("COS: [numero1 vs namber2]");
        list.add("COSINNEGO: [numer vs cwai]");
        list.add("TAMTEGO: [typo vs ???]");
        return list;
    }

    private List<String> getUserList() {
        GamePackage sendPackage = new GamePackage();
        sendPackage.setType(PackageType.GET_USER_LIST);

        GamePackage getPackage = null;
        try {
            getPackage = sendToServer(sendPackage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Set<String> stringSet = (Set<String>) getPackage.getContent();
        return new ArrayList<>(stringSet);
    }

    private List<String> getGameList() {
        GamePackage sendPackage = new GamePackage();
        sendPackage.setType(PackageType.GET_GAME_LIST);

        GamePackage getPackage = null;
        try {
            getPackage = sendToServer(sendPackage);
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
}
