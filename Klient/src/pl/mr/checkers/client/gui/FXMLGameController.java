package pl.mr.checkers.client.gui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import pl.mr.checkers.client.SceneNames;
import pl.mr.checkers.client.UserSession;
import pl.mr.checkers.client.gui.utils.GameMethods;
import pl.mr.checkers.model.ChatMassage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class FXMLGameController extends AbstractController {
    @FXML
    public Pane hostPane;
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
    @FXML
    private Label playerName;

    private GameMethods gameMethods;

    public FXMLGameController() {
        gameMethods = new GameMethods();
    }

    //uzupełnienie okienek
    public void init() {

        //ponumerowanie wszystkich kratek
        ObservableList<Node> childrens = grid.getChildren();
        
        Pane pane;

        for (int i = 0; i < 64; i++) {
            int position = i;
            pane = (Pane) childrens.get(i);
            Pane finalPane = pane;

            //zaznaczanie pionków kliknięciem
            pane.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    ObservableList<Node> children = finalPane.getChildren();
                    if (children.isEmpty()) {
                        return;
                    }

                    ImageView imageView = (ImageView) children.get(0);

                    String url = imageView.getImage().getUrl();
                    String substring = url.substring(url.lastIndexOf('/') + 1);

                    if (!substring.equals("background.png")) {
//                    if (imageView.getImage() != null) {
//                        String url = imageView.getImage().getUrl();
//                        String substring = url.substring(url.lastIndexOf('/') + 1);
                        if (UserSession.LOGIN.equals(UserSession.GAME.getPlayers()[0]) && UserSession.GAME.isHostTurn()) {
                            if (substring.equals("pawnBlack.png") && !UserSession.PAWN_CLICKED) {
                                imageView.setImage(UserSession.PAWN_BLACK_CLICKED);
                                UserSession.PAWN_CLICKED = true;
                            } else if (substring.equals("pawnBlackClicked.png")) {
                                imageView.setImage(UserSession.PAWN_BLACK);
                                UserSession.PAWN_CLICKED = false;
                            }
                        } else if (UserSession.LOGIN.equals(UserSession.GAME.getPlayers()[1]) && !UserSession.GAME.isHostTurn()) {
                            if (substring.equals("pawnWhite.png") && !UserSession.PAWN_CLICKED) {
                                imageView.setImage(UserSession.PAWN_WHITE_CLICKED);
                                UserSession.PAWN_CLICKED = true;
                            } else if (substring.equals("pawnWhiteClicked.png")) {
                                imageView.setImage(UserSession.PAWN_WHITE);
                                UserSession.PAWN_CLICKED = false;
                            }
                        }
                    } else if (UserSession.PAWN_CLICKED) {
                        //ustawienie nowego
                        if (UserSession.LOGIN.equals(UserSession.GAME.getPlayers()[0])) {
                            imageView.setImage(UserSession.PAWN_BLACK);
                        } else if (UserSession.LOGIN.equals(UserSession.GAME.getPlayers()[1])) {
                            imageView.setImage(UserSession.PAWN_WHITE);
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
        }
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
        UserSession.CURRENT_SCENE =SceneNames.MENU_SCENE;
        gameMethods.goToScene(SceneNames.MENU_SCENE, null, event);
    }

    //odświeżanie gry co jakiś czas
    @Override
    protected void refresh() {
        if (UserSession.CURRENT_SCENE != SceneNames.GAME_SCENE || errorMessage == null) {
            return;
        }

        boolean notMyMove = (UserSession.LOGIN.equals(UserSession.GAME.getPlayers()[0]) && !UserSession.GAME.isHostTurn()) || (UserSession.LOGIN.equals(UserSession.GAME.getPlayers()[1]) && UserSession.GAME.isHostTurn());

//        if (notMyMove) {
        if (true) {
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
        //pobranie graczy grających
        String[] players = UserSession.GAME.getPlayers();

        ObservableList<ChatMassage> observableGameList = FXCollections.observableList(chatMassages);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (!UserSession.GAME.isHostTurn()) {
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
                chat.refresh();
//                UserSession.CURRENT_WINDOW.show();

                user1.setText(players[0]);
                user2.setText(players[1]);
            }
        });
    }

    @FXML
    public void wyslij() {
        gameMethods.convertBoardToServerFormat(grid);
        gameMethods.sendGame(this, errorMessage);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //do wypelnienia
        playerName.setText(UserSession.LOGIN);
        //uzupełnienie nazwy gry
        gameName.setText(UserSession.GAME_NAME);

        gameMethods.getGame(this, errorMessage);
        gameMethods.convertServerFormatToBoard(grid);
        init();
    }
}
