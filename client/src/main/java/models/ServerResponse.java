package models;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public static ServerResponse parseFromString(String input) {
        ServerResponse serverResponse = new ServerResponse();

        // Split the input string into parts for game, session, and error
        List<String> parts = Arrays.asList(input.split("(?<=\\}),\\s*(?=[a-zA-Z])"));

        // Parse Game State
        Pattern gameStatePattern = Pattern.compile("GameState\\{(.+?)\\}");
        Matcher gameStateMatcher = gameStatePattern.matcher(parts.get(0));
        if (gameStateMatcher.find()) {
            serverResponse.game = GameState.parseFromString(gameStateMatcher.group(1));
         //   serverResponse.game = parseGameState(gameStateMatcher.group(1));
        }

        // Parse Session State
        Pattern sessionStatePattern = Pattern.compile("SessionState\\{(.+?)\\}");
        Matcher sessionStateMatcher = sessionStatePattern.matcher(parts.get(1));
        if (sessionStateMatcher.find()) {
            serverResponse.game = GameState.parseFromString(gameStateMatcher.group(1));

            serverResponse.session = SessionState.parseFromString(sessionStateMatcher.group(1));
        }

        // Parse Error
        serverResponse.error = parts.get(2);

        return serverResponse;
    }


}
