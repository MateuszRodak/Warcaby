package pl.mr.checkers.client;

import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import pl.mr.checkers.model.Game;

public class UserSession {
    public static SceneNames CURRENT_SCENE;
    public static String SERVER_IP;
    public static int SERVER_PORT;

    public static ObservableList<String> CURRENT_PLAYER_LIST;
    public static ObservableList<String> CURRENT_TABLE_LIST;
//    public static Stage CURRENT_WINDOW;

    public static String LOGIN;
    public static Game GAME;
    public static String GAME_NAME;
    public static int FIELD_POSITION;
    public static int CURRENT_POSITION;
    public static boolean PAWN_CLICKED;

    public static Image PAWN_BLACK = new Image("/pawnBlack.png");
    public static Image PAWN_BLACK_CLICKED = new Image("/pawnBlackClicked.png");
    public static Image PAWN_WHITE = new Image("/pawnWhite.png");
    public static Image PAWN_WHITE_CLICKED = new Image("/pawnWhiteClicked.png");
    public static Image QUENN_BLACK = new Image("/queenBlack.png");
    public static Image QUENN_BLACK_CLICKED = new Image("/queenBlackClicked.png");
    public static Image QUENN_WHITE = new Image("/queenWhite.png");
    public static Image QUENN_WHITE_CLICKED = new Image("/queenWhiteClicked.png");
    public static Image BACKGROUND = new Image("/background.png");
}
