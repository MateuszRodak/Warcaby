package pl.mr.checkers.client.gui;

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
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import pl.mr.checkers.client.UserSession;
import pl.mr.checkers.model.ChatMassage;
import pl.mr.checkers.model.Game;
import pl.mr.checkers.model.GamePackage;
import pl.mr.checkers.model.PackageType;

import java.io.IOException;
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
    @FXML
    private Pane pole0;
    @FXML
    private Pane pole1;
    @FXML
    private Pane pole3;
    @FXML
    private Pane pole5;
    @FXML
    private Pane pole7;
    @FXML
    private Pane pole8;
    @FXML
    private Pane pole10;
    @FXML
    private Pane pole12;
    @FXML
    private Pane pole14;
    @FXML
    private GridPane grid;

    public void init(MouseEvent event) {
        if (initialized) {
            return;
        }
//        errorMessage.setText("");
        gameName.setText(UserSession.GAME_NAME);
        ObservableList<Node> childrens = grid.getChildren();

        Pane pane;
        ImageView imageView = null;

        char[] gameBoard = UserSession.GAME.getBoard();
        char pawn;
        for (int i = 0; i < gameBoard.length; i++) {
            int position = i;
            pawn = gameBoard[i];

            pane = (Pane) childrens.get(i);
            Pane finalPane = pane;
            final ImageView[] finalImageView = {imageView};
            pane.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    System.out.println("pole " + position);

                    ObservableList<Node> children = finalPane.getChildren();
                    if (children.isEmpty()) {
                        return;
                    }

                    finalImageView[0] = (ImageView) children.get(0);

                    if (finalImageView[0].getImage() != null) {
                        String url = finalImageView[0].getImage().getUrl();
                        String substring = url.substring(url.lastIndexOf('/') + 1);
                        if (substring.equals("pawnBlack.png") && !UserSession.PAWN_CLICKED) {
                            finalImageView[0].setImage(UserSession.PAWN_BLACK_CLICKED);
                            UserSession.PAWN_CLICKED = true;
                        } else if (substring.equals("pawnBlackClicked.png")) {
                            finalImageView[0].setImage(UserSession.PAWN_BLACK);
                            UserSession.PAWN_CLICKED = false;
                        }
                    } else if (UserSession.PAWN_CLICKED) {
                        //ustawienie nowego
                        finalImageView[0].setImage(UserSession.PAWN_BLACK);
                        //usuniecie starego
                        Pane oldPane = (Pane) childrens.get(UserSession.PAWN_POSITION);
                        ImageView oldPawn = (ImageView) oldPane.getChildren().get(0);
                        UserSession.PAWN_CLICKED = false;
                        oldPawn.setImage(null);
                    }

                    UserSession.PAWN_POSITION = position;
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
                imageView.setImage(UserSession.PAWN_WHITE);
            } else if (pawn == 'D') {
                imageView.setImage(UserSession.PAWN_WHITE);
            }
        }

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

    public void dupa() {
        System.out.println("dupa");
    }
}
