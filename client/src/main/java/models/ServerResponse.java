package models;

import java.io.Serializable;
import java.util.ArrayList;
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
        List<String> parts = new ArrayList<>();

// Define the regex pattern to capture "game," "session," and "error"
        Pattern serverResponsePattern = Pattern.compile("game=(.+?),\\s*session=(.+?),\\s*error='(.+)'");
        Matcher serverResponseMatcher = serverResponsePattern.matcher(input);

// Check if the pattern is found
        if (serverResponseMatcher.find()) {
            // Add "game" to parts
            parts.add(serverResponseMatcher.group(1).trim());

            // Add "session" to parts
            parts.add(serverResponseMatcher.group(2).trim());

            // Add "error" to parts
            parts.add(serverResponseMatcher.group(3).trim());
        }
        System.out.println("parts" + parts);

        // Parse Game State
        Pattern gameStatePattern = Pattern.compile("GameState\\{id='([^']+)', history=\\[([^\\]]+)\\], score='([^']+)', winner=([^}]+)\\}");
        Matcher gameStateMatcher = gameStatePattern.matcher(parts.get(0));

        Pattern gameStatePatterntwo = Pattern.compile("GameState\\{id='([^']+)', history=\\[([^\\]]+)\\], score='([^']+)', winner=([^}]+)\\}");
        Matcher gameStateMatchertwo = gameStatePattern.matcher(input);
        if (gameStateMatcher.find()) {
            String id = gameStateMatcher.group(1);
            String history = gameStateMatcher.group(2);
            String score = gameStateMatcher.group(3);
            String winner = gameStateMatcher.group(4);

            System.out.println("ID: " + id);
            System.out.println("History: " + history);
            System.out.println("Score: " + score);
            System.out.println("Winner: " + winner);
            serverResponse.game = GameState.parseFromString(parts.get(0));
         //   serverResponse.game = parseGameState(gameStateMatcher.group(1));
        }

      /*  // Parse Session State
        Pattern sessionStatePattern = Pattern.compile("SessionState\\{(.+?)\\}");
        Matcher sessionStateMatcher = sessionStatePattern.matcher(parts.get(1));
        if (sessionStateMatcher.find()) {
            serverResponse.game = GameState.parseFromString(gameStateMatcher.group(1));

            serverResponse.session = SessionState.parseFromString(sessionStateMatcher.group(1));
        }*/

        // Parse Error
    //    serverResponse.error = parts.get(2);

        return serverResponse;
    }

    @Override
    public String toString() {
        return "ServerResponse{" +
                "game=" + game +
                ", session=" + session +
                ", error='" + error + '\'' +
                '}';
    }

}
