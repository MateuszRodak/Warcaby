package pl.mr.checkers.client;

import pl.mr.checkers.model.GamePackage;

public abstract class AbstractController {

    public GamePackage sendToServer(GamePackage gamePackage) {
        SocketClient socketClient = new SocketClient();
        socketClient.openSocket();

        socketClient.sendPackage(gamePackage);

        GamePackage receivedPackage = socketClient.readPackage();

        socketClient.closeSocket();
        return receivedPackage;
    }
}