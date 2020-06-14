package pl.mr.checkers.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameTable
{

/*    private String gameTableName;
    private Object game;*/

    private Map<String, Game> gameTables;

    public void testhierarchi()
    {
        gameTables = new HashMap<>();

        Game game = gameTables.get("Nazwa Gry");
        List<ChatMassage> chatMassages = game.getChatMassages();

        for (ChatMassage chatMassage : chatMassages) {
            String nick = chatMassage.getNick();
            String message = chatMassage.getMessage();
        }

        //Dodanie wiadomości
        chatMassages.add(new ChatMassage("Nick gracza","Treść Nowej Wiadomość"));
    }

}


