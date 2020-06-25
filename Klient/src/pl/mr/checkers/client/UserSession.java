package pl.mr.checkers.client;

import javafx.scene.image.Image;
import pl.mr.checkers.model.Game;

public class UserSession {
    public static String LOGIN;
    public static Game GAME;
    public static String GAME_NAME;
//    public static int PAWN_POSITION;
    public static int FIELD_POSITION;
    public static boolean PAWN_CLICKED;

    public static Image PAWN_BLACK = new Image("/pawnBlack.png");
    public static Image PAWN_BLACK_CLICKED = new Image("/pawnBlackClicked.png");
    public static Image PAWN_WHITE = new Image("/pawnWhite.png");
    public static Image PAWN_WHITE_CLICKED = new Image("/pawnWhiteClicked.png");
    public static Image QUENN_BLACK = new Image("/queenBlack.png");
    public static Image QUENN_BLACK_CLICKED = new Image("/queenBlackClicked.png");
    public static Image QUENN_WHITE = new Image("/queenWhite.png");
    public static Image QUENN_WHITE_CLICKED = new Image("/queenWhiteClicked.png");
//    public static Image PAWN_BACKGROUND = new Image("/background.png");
}
