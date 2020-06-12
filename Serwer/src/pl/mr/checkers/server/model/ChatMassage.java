package pl.mr.checkers.server.model;

public class ChatMassage {

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
}
