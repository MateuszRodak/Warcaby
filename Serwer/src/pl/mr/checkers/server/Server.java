package pl.mr.checkers.server;


import pl.mr.checkers.model.Game;
import pl.mr.checkers.model.GameInfo;
import pl.mr.checkers.model.GamePackage;
import pl.mr.checkers.model.PackageType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.ClassNotFoundException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Server
{


    private static ServerSocket SERVER_SOCKET;
    private static int PORT = 9876;
    private Map<String, Game> gameTables = new HashMap<>();

    public static void main(String args[]) throws IOException, ClassNotFoundException{

        Server server = new Server();

        SERVER_SOCKET = new ServerSocket(PORT);

        while(true)
        {
            System.out.println("WW Waiting for the client request");
            Socket socket = SERVER_SOCKET.accept();

            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            GamePackage message = (GamePackage) ois.readObject();

            System.out.println("Message Received: " + message.getType());

            GamePackage returnMessage = server.process(message);

            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(returnMessage);

            ois.close();
            oos.close();
            socket.close();

//            if(message.equalsIgnoreCase("exit")) break;
        }

     //   System.out.println("Shutting down Socket server!!");
      //  SERVER_SOCKET.close();
    }


    private GamePackage process(GamePackage gamePackage)
    {
        PackageType type = gamePackage.getType();
        Object content = gamePackage.getContent();
        Object response = null;

        Game game = new Game();
        game.setPlayers(new String[]{"xx"});

        gameTables.put("", game);


        switch (type) {

            case LOGIN:
                String request = (String) content;
                response = checkLogin(request);
                break;
            case GET_GAME_LIST:
                response = getGameList();
                break;
            case ACCESS_TO_GAME:
                break;
            case GET_GAME:
                break;
            case SEND_GAME:
                break;
        }


        return (GamePackage) response;
    }

    private GamePackage checkLogin(String login)
    {
        GamePackage ret = new GamePackage();

        for (Map.Entry<String, Game> gameEntry : gameTables.entrySet()) {
            Game game = gameEntry.getValue();

            if((game.getPlayers().length > 0 && login.equals(game.getPlayers()[0])) || (game.getPlayers().length > 1 && login.equals(game.getPlayers()[1])))
            {
                ret.setContent("error");
                return ret;
            }
        }

        ret.setContent("OK");
        return ret;
    }

    private GamePackage getGameList()
    {
        GamePackage ret = new GamePackage();
        List<GameInfo> gameInfos = new ArrayList<>();
        GameInfo gameInfo;



        for (Map.Entry<String, Game> gameEntry : gameTables.entrySet()) {
            String key = gameEntry.getKey();
            Game game = gameEntry.getValue();

            gameInfo = new GameInfo();
            gameInfo.setTableName(key);
            gameInfo.setPlayers(game.getPlayers());
            gameInfo.setPending(game.isPending());

            gameInfos.add(gameInfo);
        }

        ret.setContent(gameInfos);

        return ret;
    }

}
