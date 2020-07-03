package pl.mr.checkers.client.gui.utils;

import javafx.scene.control.Label;
import javafx.util.Pair;
import pl.mr.checkers.client.UserSession;
import pl.mr.checkers.client.gui.AbstractController;
import pl.mr.checkers.model.Game;
import pl.mr.checkers.model.GameInfo;
import pl.mr.checkers.model.PackageType;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MenuMethods extends Methods {

    public List<String> getUserList(AbstractController controller, Label errorMessage) {

        Pair<String, Object> result = sendPackage(controller, PackageType.GET_USER_LIST, null);

        if (result.getKey().equals("RIP")) {
            errorMessage.setText("Server error connection");
        } else if (!result.getKey().equals("OK")) {
            errorMessage.setText("Can't download user list");
        }
        Set<String> strings = (Set<String>) result.getValue();
        return new ArrayList<>(strings);
    }

    public List<String> getGameList(AbstractController controller, Label errorMessage) {
        List<String> stringList = new ArrayList<>();

        Pair<String, Object> result = sendPackage(controller, PackageType.GET_GAME_LIST, null);

        if (result.getKey().equals("RIP")) {
            errorMessage.setText("Server error connection");
        } else if (!result.getKey().equals("OK")) {
            errorMessage.setText("Can't download game list");
        }

        String nameGame;

        //ustawienie formatu zapisu na ekranie
        List<GameInfo> gameInfoList = (List<GameInfo>) result.getValue();
        for (GameInfo gameInfo : gameInfoList) {
            String player2 = gameInfo.getPlayers()[1];

            if (player2 == null) {
                player2 = "???";
            }

            nameGame = gameInfo.getTableName().toUpperCase() + ": [" + gameInfo.getPlayers()[0] + " vs " + player2 + "]";
            stringList.add(nameGame);
        }

        return stringList;
    }

    public void createNewGame(AbstractController controller, Label errorMessage, String tableName){
        Pair<String, Object> result = sendPackage(controller, PackageType.CREATE_NEW_GAME, tableName);

        if (result.getKey().equals("RIP")) {
            errorMessage.setText("Server error connection");
        } else if (!result.getKey().equals("OK")) {
            errorMessage.setText("Can't create new game");
        }

        UserSession.GAME = (Game) result.getValue();
        UserSession.GAME_NAME = tableName;
    }

    public void joinToGame(AbstractController controller, Label errorMessage, String tableName){
        Pair<String, Object> result = sendPackage(controller, PackageType.ACCESS_TO_GAME, tableName);

        if (result.getKey().equals("RIP")) {
            errorMessage.setText("Server error connection");
        } else if (!result.getKey().equals("OK")) {
            errorMessage.setText("Can't join to game");
        }

        UserSession.GAME = (Game) result.getValue();
        UserSession.GAME_NAME = tableName;
    }
}
