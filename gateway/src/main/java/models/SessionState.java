package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SessionState implements Serializable {

    private List<GameState> history;
    private String score;
    private String winner;

    public SessionState() {
        this.history = new ArrayList<>();
    }

    public List<GameState> getHistory() {
        return history;
    }

    public void setHistory(List<GameState> history) {
        this.history = history;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    @Override
    public String toString() {
        return "SessionState{" +
                "history=" + history +
                ", score='" + score + '\'' +
                ", winner='" + winner + '\'' +
                '}';
    }

    public static SessionState parseFromString(String input) {
        SessionState sessionState = new SessionState();

        // Parse history
        Pattern historyPattern = Pattern.compile("history=(.+?)");
        Matcher historyMatcher = historyPattern.matcher(input);
        if (historyMatcher.find()) {
         //   sessionState.setHistory();
            sessionState.setHistory(parseGameHistory(historyMatcher.group(1)));

        }

        // Parse score
        Pattern scorePattern = Pattern.compile("score='(.+?)'");
        Matcher scoreMatcher = scorePattern.matcher(input);
        if (scoreMatcher.find()) {
            sessionState.setScore(scoreMatcher.group(1));
        }

        // Parse winner
        Pattern winnerPattern = Pattern.compile("winner='(.+?)'");
        Matcher winnerMatcher = winnerPattern.matcher(input);
        if (winnerMatcher.find()) {
            sessionState.setWinner(winnerMatcher.group(1));
        }

        return sessionState;
    }

    private static List<GameState> parseGameHistory(String historyString) {
        List<GameState> history = new ArrayList<>();

        // Assume game states are separated by commas
        String[] gameStateStrings = historyString.split(", ");

        // Parse each game state string and add it to the history list
        for (String gameStateString : gameStateStrings) {
            GameState gameState = GameState.parseFromString(gameStateString);
            history.add(gameState);
        }

        return history;
    }


}