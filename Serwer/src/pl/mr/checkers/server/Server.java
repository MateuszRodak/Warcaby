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
import java.util.*;


public class Server
{

    private static ServerSocket SERVER_SOCKET;
    private static int PORT = 9876;
    private Map<String, Game> gameTables = new HashMap<>();
    private Map<String, Date> gameTimeTable = new HashMap<>();
    private Map<String, Date> userTables = new HashMap<>();

    public static void main(String args[]) throws IOException, ClassNotFoundException
    {

        Server server = new Server();

        SERVER_SOCKET = new ServerSocket(PORT);

        while (true)
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
        String user = gamePackage.getUser();
        Object response = null;

        Game game = new Game();
        game.setPlayers(new String[]{"xx"});

        gameTables.put("", game);


        switch (type)
        {
            case LOGIN:
                response = checkLogin(user);
                break;
            case GET_GAME_LIST:
                response = getGameList();
                break;
            case GET_USER_LIST:
                response = getUserList();
                break;
            case ACCESS_TO_GAME:
                ////
                break;
            case CREATE_NEW_GAME:
                response = createNewGame((String) content, user);
                break;
            case GET_GAME:
                ////g
                //
                break;
            case SEND_GAME:
                ////h
                break;
        }


        return (GamePackage) response;
    }

    private GamePackage checkLogin(String login)
    {
        GamePackage ret = new GamePackage();

        for (String userName : userTables.keySet())
        {
            if (userName.equals(login))
            {
                ret.setResult("ERROR1");
                return ret;
            }
        }
        userTables.put(login, new Date());
        ret.setResult("OK");
        return ret;
    }

    private GamePackage getGameList()
    {
        GamePackage ret = new GamePackage();
        List<GameInfo> gameInfos = new ArrayList<>();
        GameInfo gameInfo;


        for (Map.Entry<String, Game> gameEntry : gameTables.entrySet())
        {
            String key = gameEntry.getKey();
            Game game = gameEntry.getValue();

            gameInfo = new GameInfo();
            gameInfo.setTableName(key);
            gameInfo.setPlayers(game.getPlayers());
            gameInfo.setPending(game.isPending());

            gameInfos.add(gameInfo);
        }

        ret.setContent(gameInfos);
        ret.setResult("OK");
        return ret;
    }

    private GamePackage getUserList()
    {
        GamePackage ret = new GamePackage();
        Set<String> userList = new TreeSet<>();

        userList.addAll(userTables.keySet());
        ret.setContent(userList);
        ret.setResult("OK");
        return ret;
    }

    private GamePackage createNewGame(String gameName, String userName)
    {
        GamePackage ret = new GamePackage();
        //Sprawdźić czy gra istieje
        for (String nameOfTheGame : gameTables.keySet())
        {
            if (nameOfTheGame.equals(gameName))
            {
                ret.setResult("ERROR2");
                return ret;
            }
        }

        // jeżeli nie to dodać grę
        Game game = new Game();
        game.setPlayers(new String[]{userName, null});
        game.setBoard(Utils.generateBoard());
        game.setChatMassages(new ArrayList<>());
        game.setPending(true);

        gameTables.put(gameName, game);
        gameTimeTable.put(gameName, new Date());

        ret.setContent(game);
        ret.setResult("OK");
        return ret;
    }

    private GamePackage accessToGame(String gameName, String userName)
    {
        GamePackage ret = new GamePackage();

        //znaleźć grę
        Game game = gameTables.get(gameName);


        //sprawdzić czy pending jest true
        if (!game.isPending())
            {
                ret.setResult("ERROR3");
                return ret;
            }

        //dołącz do gry
        String[] players = game.getPlayers();
        players[1] = userName;

        gameTimeTable.put(gameName, new Date());
        ret.setContent(game);
        ret.setResult("OK");
        return ret;
    }





}
