package pl.mr.checkers.model;

import java.io.Serializable;

public class ChatMassage implements Serializable {

    private String nick;
    private String message;

    public ChatMassage(String nick, String message) {
        this.nick = nick;
        this.message = message;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return nick + ":\n\t" + message;
    }
}
