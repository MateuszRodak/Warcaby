package pl.mr.checkers.client.gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import pl.mr.checkers.client.UserSession;
import pl.mr.checkers.client.gui.utils.MenuMethods;
import pl.mr.checkers.client.SceneNames;

import java.io.IOException;
import java.util.*;

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
    @FXML
    private TextField newTableName;
    @FXML
    private Label selectedGameName;

    private final MenuMethods menuMethods;

    public FXMLMenuController() {
        menuMethods = new MenuMethods();
    }

    /**
     * wypełnianinie menu danymi z serwera
     */
    @FXML
    public void init(MouseEvent event) {
        //sprawdzanie czy już wypełnione
        if (initialized) {
            return;
        }
        errorMessage.setText("");

        //pobranie z lokalnego pliku nazwy użytkownika podanego przy logowaniu
        userName.setText(UserSession.LOGIN);

        //pobranie i wyświtlenie listy graczy
        List<String> userList = menuMethods.getUserList(this, errorMessage);
        ObservableList<String> observableUserList = FXCollections.observableList(userList);
        playerList.setItems(observableUserList);

        //pobranie i wyświetlenie listy gier
        List<String> gameListStrings = menuMethods.getGameList(this, errorMessage);
        ObservableList<String> observableGameList = FXCollections.observableList(gameListStrings);
        gameList.setItems(observableGameList);

        //po kliknieciu na rozgrywaną gre wyświetla nazwę poniżej w sekcji join
        gameList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                //WAŻNE nazwa gry ma postać: naszanazwagry: [gracz_pierwszy vs gracz_drugi]

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
        initialized = true;
    }

    /**
     * dodanie nowego stołu do listy stołów w menu
     */
    @FXML
    public void createGame(ActionEvent event) throws IOException {
        //potrzebna nazwa stołu
        if (newTableName.getText().equals("")) {
            errorMessage.setText("Podaj nazwe");
            return;
        }

        String tableName = newTableName.getText().toUpperCase();
        menuMethods.createNewGame(this,errorMessage,tableName);

        //dołącz do utworzonej swojej gry
        menuMethods.goToScene(SceneNames.GAME_SCENE, null, event);
    }

    /**
     * dołączenie do stołu
     */
    @FXML
    public void join(ActionEvent event) throws IOException {
        String tableName = selectedGameName.getText();

        //dołączanie do stołu
        menuMethods.joinToGame(this,errorMessage,tableName);

        //przełączenie okna na okno z grą
        menuMethods.goToScene(SceneNames.GAME_SCENE, null, event);
    }

    //metoda timera wykonywana cały czas
    @Override
    protected void completeTask() {
        System.out.println("tu cos robie menu");
    }
}
