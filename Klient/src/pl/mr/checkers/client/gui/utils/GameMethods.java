package pl.mr.checkers.client.gui.utils;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.util.Pair;
import pl.mr.checkers.client.UserSession;
import pl.mr.checkers.client.gui.AbstractController;
import pl.mr.checkers.model.ChatMassage;
import pl.mr.checkers.model.Game;
import pl.mr.checkers.model.GamePackage;
import pl.mr.checkers.model.PackageType;

public class GameMethods extends Methods{

    public void sendChatMessage(AbstractController controller, Label errorMessage, String chatMessage) {

        ChatMassage chat = new ChatMassage(UserSession.LOGIN, chatMessage);
        UserSession.GAME.getChatMassages().add(chat);

        Pair<String, Object> result = sendPackage(controller, PackageType.SEND_GAME, UserSession.GAME);

        if (result.getKey().equals("RIP")) {
            errorMessage.setText("Server error connection");
        } else if (!result.getKey().equals("OK")) {
            errorMessage.setText("Can't send new chat text");
        }
    }

    public void sendGame(AbstractController controller, Label errorMessage) {
        Pair<String, Object> result = sendPackage(controller, PackageType.SEND_GAME, UserSession.GAME);

        if (result.getKey().equals("RIP")) {
            errorMessage.setText("Server error connection");
        } else if (!result.getKey().equals("OK")) {
            errorMessage.setText("Can't send game to server");
        }
    }

    public boolean getGame(AbstractController controller, Label errorMessage) {
        Pair<String, Object> result = sendPackage(controller, PackageType.GET_GAME, UserSession.GAME_NAME);

        if (result.getKey().equals("RIP")) {
            errorMessage.setText("Server error connection");
            return true;
        } else if (!result.getKey().equals("OK")) {
            errorMessage.setText("Can't get game from server");
            return true;
        }

        Game game = (Game) result.getValue();
        UserSession.GAME = game;

        return false;
    }

    public void convertBoardToServerFormat(GridPane grid) {
        ObservableList<Node> childrens = grid.getChildren();

        Pane pane;
        ImageView imageView;

        //pobranie z serwera pozycji pionków
        char[] gameBoard = UserSession.GAME.getBoard();

        //czytanie pionków z planszy
        for (int i = 0; i < childrens.size(); i++) {
            pane = (Pane) childrens.get(i);
            if (pane.getChildren().isEmpty()) {
                gameBoard[i] = 0;
                continue;
            }
            imageView = (ImageView) pane.getChildren().get(0);
            Image image = imageView.getImage();

            if (image == null) {
                gameBoard[i] = 0;
                continue;
            }
            String url = image.getUrl();

            if (url.contains("pawnBlack.png")) {
                gameBoard[i] = 'p';
            } else if (url.contains("pawnWhite.png")) {
                gameBoard[i] = 'P';
            } else if (url.contains("queenBlack.png")) {
                gameBoard[i] = 'd';
            } else if (url.contains("queenWhite.png")) {
                gameBoard[i] = 'D';
            } else {
                gameBoard[i] = 0;
            }
            UserSession.GAME.setBoard(gameBoard);
        }
    }
    public void convertServerFormatToBoard(GridPane grid){
        //pobranie z serwera pozycji pionków
        ObservableList<Node> childrens = grid.getChildren();

        Pane pane;
        ImageView imageView = null;
        char[] gameBoard = UserSession.GAME.getBoard();
        char pawn;

        //wyświetlenie pionków na planszy
        for (int i = 0; i < gameBoard.length; i++) {
            pawn = gameBoard[i];
            pane = (Pane) childrens.get(i);

                if(!pane.getChildren().isEmpty()){
                    imageView = (ImageView) pane.getChildren().get(0);

                    if (pawn == 'p') {
                        imageView.setImage(UserSession.PAWN_BLACK);
                    } else if (pawn == 'P') {
                        imageView.setImage(UserSession.PAWN_WHITE);
                    } else if (pawn == 'd') {
                        imageView.setImage(UserSession.QUENN_BLACK);
                    } else if (pawn == 'D') {
                        imageView.setImage(UserSession.QUENN_WHITE);
                    } else {
                        imageView.setImage(UserSession.BACKGROUND);
                    }
                }
        }
    }
}
