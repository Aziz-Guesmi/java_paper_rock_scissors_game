package models;

import java.io.Serializable;

public class ServerResponse implements Serializable {
    private GameState game;
    private SessionState session;

    private String error;


    public GameState getGame() {
        return game;
    }

    public void setGame(GameState game) {
        this.game = game;
    }

    public SessionState getSession() {
        return session;
    }

    public void setSession(SessionState session) {
        this.session = session;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
