package pl.mr.checkers.model;

import java.io.Serializable;
import java.util.List;

public class Game implements Serializable {

    private String name;
    private char[] board = new char[64];
    private String[] players = new String[2];
    private List<ChatMassage> chatMassages;
    private boolean pending;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
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
