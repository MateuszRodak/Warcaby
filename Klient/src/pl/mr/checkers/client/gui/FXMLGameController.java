package pl.mr.checkers.client.gui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import pl.mr.checkers.client.SceneNames;
import pl.mr.checkers.client.UserSession;
import pl.mr.checkers.client.gui.utils.GameMethods;
import pl.mr.checkers.model.ChatMassage;
import pl.mr.checkers.model.Game;
import pl.mr.checkers.model.GamePackage;
import pl.mr.checkers.model.PackageType;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;


public class FXMLGameController extends AbstractController {

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
    @FXML
    private GridPane grid;
    @FXML
    private Label errorMessage;

    private GameMethods gameMethods;

    public FXMLGameController() {
        gameMethods = new GameMethods();
    }

    //uzupełnienie okienek
    public void init(MouseEvent event) {

        //uzupełnienie nazwy gry
        try {
            gameName.setText(UserSession.GAME_NAME);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //ponumerowanie wszystkich kratek
        ObservableList<Node> childrens = grid.getChildren();


        Pane pane;
        ImageView imageView = null;

        //pobranie z serwera pozycji pionków
        gameMethods.getGame(this, errorMessage);
        char[] gameBoard = UserSession.GAME.getBoard();
        char pawn;

        //wyświetlenie pionków na planszy
        for (int i = 0; i < gameBoard.length; i++) {
            int position = i;
            pawn = gameBoard[i];
            pane = (Pane) childrens.get(i);
            Pane finalPane = pane;

            //ustawienie wszystkich obrazków pionków w kratkę
            final ImageView[] finalImageView = {imageView};

            //zaznaczanie pionków kliknięciem
            pane.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    ObservableList<Node> children = finalPane.getChildren();
                    if (children.isEmpty()) {
                        return;
                    }

                    finalImageView[0] = (ImageView) children.get(0);

                    if (finalImageView[0].getImage() != null) {
                        String url = finalImageView[0].getImage().getUrl();
                        String substring = url.substring(url.lastIndexOf('/') + 1);
                        if (UserSession.LOGIN.equals(UserSession.GAME.getPlayers()[0]) && UserSession.GAME.isHostTurn()) {
                            if (substring.equals("pawnBlack.png") && !UserSession.PAWN_CLICKED) {
                                finalImageView[0].setImage(UserSession.PAWN_BLACK_CLICKED);
                                UserSession.PAWN_CLICKED = true;
                            } else if (substring.equals("pawnBlackClicked.png")) {
                                finalImageView[0].setImage(UserSession.PAWN_BLACK);
                                UserSession.PAWN_CLICKED = false;
                            }
                        } else if (UserSession.LOGIN.equals(UserSession.GAME.getPlayers()[1]) && !UserSession.GAME.isHostTurn()) {
                            if (substring.equals("pawnWhite.png") && !UserSession.PAWN_CLICKED) {
                                finalImageView[0].setImage(UserSession.PAWN_WHITE_CLICKED);
                                UserSession.PAWN_CLICKED = true;
                            } else if (substring.equals("pawnWhiteClicked.png")) {
                                finalImageView[0].setImage(UserSession.PAWN_WHITE);
                                UserSession.PAWN_CLICKED = false;
                            }
                        }
                    } else if (UserSession.PAWN_CLICKED) {
                        //ustawienie nowego
                        if (UserSession.LOGIN.equals(UserSession.GAME.getPlayers()[0])) {
                            finalImageView[0].setImage(UserSession.PAWN_BLACK);
                        } else if (UserSession.LOGIN.equals(UserSession.GAME.getPlayers()[1])) {
                            finalImageView[0].setImage(UserSession.PAWN_WHITE);
                        }
                        //usuniecie starego
                        Pane oldPane = (Pane) childrens.get(UserSession.FIELD_POSITION);
                        ImageView oldPawn = (ImageView) oldPane.getChildren().get(0);
                        UserSession.PAWN_CLICKED = false;
                        oldPawn.setImage(UserSession.BACKGROUND);
                        wyslij();
                    }

                    UserSession.FIELD_POSITION = position;
                }
            });

            if (pawn > 0) {
                imageView = (ImageView) pane.getChildren().get(0);
            }

            if (pawn == 'p') {
                imageView.setImage(UserSession.PAWN_BLACK);
            } else if (pawn == 'P') {
                imageView.setImage(UserSession.PAWN_WHITE);
            } else if (pawn == 'd') {
                imageView.setImage(UserSession.QUENN_BLACK);
            } else if (pawn == 'D') {
                imageView.setImage(UserSession.QUENN_WHITE);
//            } else if (imageView != null){
//                imageView.setImage(UserSession.BACKGROUND);
            }
        }

//        Game game = gameMethods.getGame(this, errorMessage);

//        refresh();

        //pobranie graczy grających
        String[] players = UserSession.GAME.getPlayers();
        user1.setText(players[0]);
        user2.setText(players[1]);

    }

    //wysłanie wiadomości do czatu
    @FXML
    public void sendMessage() {
        gameMethods.sendChatMessage(this, errorMessage, chatMessage.getText());

        //wyczyszczenie pola wiadomości
        chatMessage.setText("");
        //TODO
//        init(null);
    }

    //powrót do menu gry
    @FXML
    public void goBack(ActionEvent event) throws IOException {
        gameMethods.goToScene(SceneNames.MENU_SCENE, null, event);
    }

    //odświeżanie gry co jakiś czas
    @Override
    protected void refresh() {
        if (UserSession.CURRENT_SCENE != SceneNames.GAME_SCENE || errorMessage == null) {
            return;
        }

        boolean notMyMove = (UserSession.LOGIN.equals(UserSession.GAME.getPlayers()[0]) && !UserSession.GAME.isHostTurn()) || (UserSession.LOGIN.equals(UserSession.GAME.getPlayers()[1]) && UserSession.GAME.isHostTurn());

        if (notMyMove) {
//        if (true) {
            System.out.println("moj ruch dziwny if dziala");
            try {
                gameMethods.getGame(this, errorMessage);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        //wyswietlanie pionków na planszy
                        gameMethods.convertServerFormatToBoard(grid);


                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        //lista wiadomosci na chacie
        List<ChatMassage> chatMassages = UserSession.GAME.getChatMassages();

        ObservableList<ChatMassage> observableGameList = FXCollections.observableList(chatMassages);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (notMyMove) {
                    user1.setUnderline(false);
                    user1.setStyle("-fx-text-fill: black");
                    user2.setUnderline(true);
                    user2.setStyle("-fx-text-fill: blue");
                } else {
                    user1.setUnderline(true);
                    user1.setStyle("-fx-text-fill: blue");
                    user2.setUnderline(false);
                    user2.setStyle("-fx-text-fill: black");
                }

                chat.setItems(observableGameList);
                chat.scrollTo(chatMassages.size());
            }
        });
    }

    @FXML
    public void wyslij() {
        gameMethods.convertBoardToServerFormat(grid);
        gameMethods.sendGame(this, errorMessage);
    }
}
