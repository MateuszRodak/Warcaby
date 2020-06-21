package pl.mr.checkers.model;

import java.io.Serializable;
import java.util.List;

public class Game implements Serializable {

    private String name;
    private char[] board = new char[64];
    private String[] players = new String[2];
    private List<ChatMassage> chatMassages;
    private boolean pending;
    private boolean hostTurn;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public boolean isHostTurn()
    {
        return hostTurn;
    }

    public void setHostTurn(boolean hostTurn)
    {
        this.hostTurn = hostTurn;
    }

    public char[] getBoard() {
        return board;
    }

    public void setBoard(char[] board) {
        this.board = board;
    }

    public String[] getPlayers() {
        return players;
    }

    public void setPlayers(String[] players) {
        this.players = players;
    }

    public List<ChatMassage> getChatMassages() {
        return chatMassages;
    }

    public void setChatMassages(List<ChatMassage> chatMassages) {
        this.chatMassages = chatMassages;
    }

    public boolean isPending() {
        return pending;
    }

    public void setPending(boolean pending) {
        this.pending = pending;
    }
}
