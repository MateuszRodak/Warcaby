package pl.mr.checkers.client.gui;

import javafx.fxml.Initializable;
import pl.mr.checkers.client.SocketClient;
import pl.mr.checkers.model.GamePackage;

import java.util.Timer;
import java.util.TimerTask;

public abstract class AbstractController extends TimerTask implements Initializable {

    public AbstractController() {
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(this, 0, 3*1000);
    }

    public GamePackage sendToServer(GamePackage gamePackage) {
        SocketClient socketClient = new SocketClient();
        socketClient.openSocket();

        socketClient.sendPackage(gamePackage);

        GamePackage receivedPackage = socketClient.readPackage();

        socketClient.closeSocket();
        return receivedPackage;
    }

    @Override
    public void run() {
        refresh();
    }

    protected abstract void refresh();

}