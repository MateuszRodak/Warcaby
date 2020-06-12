package pl.mr.checkers.server.model;

import java.util.List;

public class Game {

    private char[] board = new char[64];
    private String[] players = new String[2];

    private List<ChatMassage> chatMassages;

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
}
