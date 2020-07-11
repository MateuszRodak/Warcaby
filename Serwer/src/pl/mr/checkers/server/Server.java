package pl.mr.checkers.server;

import pl.mr.checkers.model.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.ClassNotFoundException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


public class Server extends TimerTask
{
    private static ServerSocket SERVER_SOCKET;
    private static int PORT = 9876;

    private Map<String, Game> gameTables = new HashMap<>();
    private Map<String, Date> gameTimeTable = new HashMap<>();
    private Map<String, Date> userTables = new HashMap<>();


    //TWORZENIE SERWERA ORAZ PROWADZENIE NASŁUCHU NA SOCKECIE
    public static void main(String args[]) throws IOException, ClassNotFoundException
    {
        if(args.length == 1){
        PORT = Integer.parseInt(args[0]);
        }

        Server server = new Server();
        SERVER_SOCKET = new ServerSocket(PORT);
        System.out.println("Waiting for the client request");

        //włączenie timera
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(server, 0, 10 * 1000);
        System.out.println("TimerTask started");

        int counter = 0; //licznik operacji na serwerze

        while (true)
        {
            counter++;
            //Czekanie na akceptacje
            Socket socket = SERVER_SOCKET.accept();

            //Odbieranie wiadomości
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            GamePackage message = (GamePackage) ois.readObject();

            //Wyświetlanie w konsoli serwera
            System.out.println("\n ID:" + counter + " Date: " + new Date() + " | ---> User: " + message.getUser());
            System.out.println("Message Received: " + message.getType());

            //Tworzenie odpowiedzi dla Klienta
            GamePackage returnMessage = server.process(message);

            //Wysyłanie odpowiedzi do Klienta
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(returnMessage);

            //Zamykanie socketa
            ois.close();
            oos.close();
            socket.close();
            //  SERVER_SOCKET.close();
        }
    }


    // !!! GŁÓWNY PROCESS ODCZYTANIA WIADOMOŚCI I PRZYGOTOWANIE ODPOWIEDZI
    private GamePackage process(GamePackage gamePackage)
    {
        PackageType type = gamePackage.getType();
        Object content = gamePackage.getContent();
        String user = gamePackage.getUser();
        Object response = null;

        //Tworzenie gier do testów
      /*  Game game = new Game();
        game.setPlayers(new String[]{"xx",""});
        gameTables.put("Gra #1", game);*/

        // Odczytanie rodzaju polecenia przez serwer
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
                response = accessToGame((String) content, user);
                break;
            case CREATE_NEW_GAME:
                response = createNewGame((String) content, user);
                break;
            case GET_GAME:
                response = getGame((String) content);
                break;
            case SEND_GAME:
                response = sendGame((Game) content); // wprowadza zmiany w planszy lub w czacie
                break;
        }

        checkLogin(gamePackage.getUser());

        return (GamePackage) response;
    }


    //SPRAWDZANIE CZY LOGIN JEST JUŻ NA LIŚCIE, JAK NIE TO DODAJEMY
    private GamePackage checkLogin(String login)
    {
        GamePackage ret = new GamePackage();

        //Wyszukiwanie nazwy na liście
        for (String userName : userTables.keySet())
        {
            if (userName.equals(login))
            {
                ret.setResult("ERROR1: login in use");
                return ret;
            }
        }

        //Dodaj nowego użytkownika
        userTables.put(login, new Date());
        ret.setResult("OK");
        return ret;
    }

    //TWORZENIE PACZKI OBECNYCH NAZW GIER I UŻYTKOWNIKÓW BEZ PLANSZY
    private GamePackage getGameList()
    {
        GamePackage ret = new GamePackage();
        List<GameInfo> gameInfos = new ArrayList<>();
        GameInfo gameInfo;

        //Wyciąganie z listy gier informacji do wyświetlenia i zamieszczanie w skrótowniku
        for (Map.Entry<String, Game> gameEntry : gameTables.entrySet())
        {
            String key = gameEntry.getKey();
            Game game = gameEntry.getValue();

            gameInfo = new GameInfo();
            gameInfo.setTableName(key); //nazwa stołu
            gameInfo.setPlayers(game.getPlayers()); //gracze przy stole
            gameInfo.setPending(game.isPending()); //czy czeka na drugiego gracza

            gameInfos.add(gameInfo);
        }

        //Wysyłanie skrótownika do klienta
        ret.setContent(gameInfos);
        ret.setResult("OK");
        return ret;
    }

    //POBIERANIE NAZW WSZYSTKICH AKTYWNYCH UŻYTKOWNIKÓW
    private GamePackage getUserList()
    {
        GamePackage ret = new GamePackage();
        Set<String> userList = new TreeSet<>();

        userList.addAll(userTables.keySet());
        ret.setContent(userList);
        ret.setResult("OK");
        return ret;
    }

    //TWORZENIE STOŁU
    private GamePackage createNewGame(String gameName, String userName)
    {
        GamePackage ret = new GamePackage();

        //Sprawdźić czy gra o tej nazwie istieje
        for (String nameOfTheGame : gameTables.keySet())
        {
            if (nameOfTheGame.equals(gameName))
            {
                ret.setResult("ERROR2: Table Name already in use");
                return ret;
            }
        }

        // jeżeli nie to dodać grę
        Game game = new Game();
        game.setPlayers(new String[]{userName, null});
        game.setBoard(Utils.generateBoard());
        game.setChatMassages(new ArrayList<>());
        game.setPending(true);
        game.setName(gameName);
        game.setHostTurn(true);

        gameTables.put(gameName, game);
        gameTimeTable.put(gameName, new Date());

        ret.setContent(game);
        ret.setResult("OK");
        return ret;
    }

    //UŻYTKOWNIK CHCE DOŁĄCZYĆ DO GRY
    private GamePackage accessToGame(String gameName, String userName)
    {
        GamePackage ret = new GamePackage();

        //Znaleźć grę

        Game game = gameTables.get(gameName);

        if (game == null)
        {
            ret.setResult("ERROR37: Game not exists");
            return ret;
        }

        //Sprawdzić czy gra jest w oczekiwaniu na gracza
//        if (!game.isPending())
        //     {
        //         ret.setResult("ERROR3: Game is full");
        //         return ret;
        //    }

        //Dołącz do gry
        String[] players = game.getPlayers();
        if (!(userName.equals(players[0]) || userName.equals(players[1])))
        {
            players[1] = userName;
        }
        gameTimeTable.put(gameName, new Date());
        ret.setContent(game);
        ret.setResult("OK");
        return ret;
    }

    //UŻYTKOWNIK CHCE ZAKTUALIZOWAĆ PLANSZĘ
    private GamePackage getGame(String gameName)
    {
        GamePackage ret = new GamePackage();

        if (gameName == null)
        {
            ret.setResult("ERROR4: Game not exists");
        } else
        {
            Game game = gameTables.get(gameName); //wyszukuje grę
            ret.setContent(game);
            ret.setResult("OK");
        }

        return ret;
    }

    //UŻYTKOWNIK WPROWADZA ZMIANY W GRZE LUB CZACIE
    private GamePackage sendGame(Game gameClient)
    {
        GamePackage ret = new GamePackage();

        if (gameClient.getName() == null)
        {
            ret.setResult("ERROR5: Game Name is empty");
        }

        Game gameServer = gameTables.get(gameClient.getName());

        //Sprawdza czy plansze sa takie same
        if(!Arrays.equals(gameClient.getBoard(), gameServer.getBoard()))
        {
            boolean hostTurn = gameServer.isHostTurn();
            gameClient.setHostTurn(!hostTurn);
        }

        //Podmienienie game
        gameTables.put(gameClient.getName(), gameClient);


        System.out.println("Host Turn? : " + gameServer.isHostTurn());
        ret.setResult("OK");

        return ret;
    }

    static int counterClener = 0;

    @Override
    public void run()
    {
        //System.out.println("Timer task started at:"+new Date());
        completeTask();
        counterClener++;
        //System.out.println("Timer task finished at:"+new Date());
        System.out.println("["+counterClener+"] Usunieto nieaktywnych użytkowników oraz nieaktywne gry"+ " Date: " + new Date());
        long timestamp = new Date().getTime();


        try {
            //Usuwanie nieaktualnych uzytkowników
            for (Map.Entry<String, Date> entry : userTables.entrySet())
                {
                    if (entry.getValue().getTime() < timestamp - 300000)
                        {
                            System.out.println(" ---->>> Usunieto Gracza: " + entry.getKey());
                            userTables.remove(entry.getKey());
                        }
                }

            //Usuwanie nieaktualnych gier
            for (Map.Entry<String, Date> entry : gameTimeTable.entrySet())
            {
                if (entry.getValue().getTime() < timestamp - 300000)
                {
                    System.out.println(" ---->>> Usunieto Gre: " + entry.getKey());
                    gameTables.remove(entry.getKey());
                    gameTimeTable.remove(entry.getKey());
                }
            }
        } catch (Exception e) {
//            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    private void completeTask()
    {
        try
        {
            //assuming it takes 20 secs to complete the task
            Thread.sleep(60000);
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
