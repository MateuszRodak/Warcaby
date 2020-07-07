package pl.mr.checkers.client.gui;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import pl.mr.checkers.client.SceneNames;
import pl.mr.checkers.client.UserSession;
import pl.mr.checkers.client.gui.utils.MenuMethods;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class FXMLMenuController extends AbstractController {

    @FXML
    private Label userName;
    @FXML
    private ListView<String> gameList;
    @FXML
    private ListView<String> playerList;
    @FXML
    private Label errorMessage;
    @FXML
    private TextField newTableName;
    @FXML
    private Label selectedGameName;

    private final MenuMethods menuMethods;

    public FXMLMenuController() {
        menuMethods = new MenuMethods();
    }

    /**
     * dodanie nowego stołu do listy stołów w menu
     */
    @FXML
    public void createGame(ActionEvent event) throws IOException {
        errorMessage.setText("");
        //potrzebna nazwa stołu
        if (newTableName.getText().equals("")) {
            errorMessage.setText("Podaj nazwe");
            return;
        }

        String tableName = newTableName.getText().toUpperCase();
        boolean isError = menuMethods.createNewGame(this, errorMessage, tableName);

        if (isError) {
            return;
        }

        //dołącz do utworzonej swojej gry
        UserSession.CURRENT_SCENE =SceneNames.GAME_SCENE;
        menuMethods.goToScene(SceneNames.GAME_SCENE, null, event);
    }

    /**
     * dołączenie do stołu
     */
    @FXML
    public void join(ActionEvent event) throws IOException {
        errorMessage.setText("");
        String tableName = selectedGameName.getText();

        //dołączanie do stołu
        boolean isError = menuMethods.joinToGame(this, errorMessage, tableName);

        if (isError) {
            return;
        }

        //przełączenie okna na okno z grą
        UserSession.CURRENT_SCENE =SceneNames.GAME_SCENE;
        menuMethods.goToScene(SceneNames.GAME_SCENE, null, event);
    }

    //metoda timera wykonywana cały czas
    @Override
    protected void refresh() {

        if (UserSession.CURRENT_SCENE != SceneNames.MENU_SCENE || menuMethods == null || errorMessage == null || playerList == null) {
//        if (UserSession.CURRENT_SCENE != SceneNames.MENU_SCENE) {
            return;
        }
        try {
            menuMethods.getGameList(this, errorMessage);
            menuMethods.getUserList(this, errorMessage);


            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    playerList.setItems(UserSession.CURRENT_PLAYER_LIST);
                    playerList.refresh();

                    gameList.setItems(UserSession.CURRENT_TABLE_LIST);
                    gameList.refresh();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //pobranie z lokalnego pliku nazwy użytkownika podanego przy logowaniu
        userName.setText(UserSession.LOGIN);


        //po kliknieciu na rozgrywaną gre wyświetla nazwę poniżej w sekcji join
        gameList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                //WAŻNE nazwa gry ma postać: naszanazwagry: [gracz_pierwszy vs gracz_drugi]

                if (newValue == null) {
                    return;
                }

                //sprawdzenie czy przy stole jest wolne miejsce
                int pending = newValue.indexOf("???");

                //sprawdzenie czy jesteśmy przy stole
                boolean yyy = newValue.contains("[" + UserSession.LOGIN + " ");
                boolean xxx = newValue.contains(" " + UserSession.LOGIN + "]");

                //sprawdzenie czy możemy dołączyć
                if (!(pending > 0 || xxx || yyy)) {
                    selectedGameName.setText("");
                    return;
                }
                //wyciągnięcie nazwy zaznaczonej gry
                int indexOf = newValue.indexOf(":");
                String substring = newValue.substring(0, indexOf);
                selectedGameName.setText(substring);
            }
        });

        refresh();
    }
}
