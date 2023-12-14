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
                "history=" + history.toString().trim() +
                ",\n"+ ", score='" +
       //         score + '\'' + ",\n"+
     //           ", winner='" + winner + '\'' +
                '}';
    }

    public static SessionState parseFromString(String input) {
        SessionState sessionState = new SessionState();

        String historyKey = "history=[";
        int startIndex = input.indexOf(historyKey);
        if (startIndex != -1) {
            startIndex += historyKey.length();
            int endIndex = input.lastIndexOf(']');
            if (endIndex != -1) {
                String historyContent = input.substring(startIndex, endIndex);
                sessionState.setHistory(parseGameHistory(historyContent));
            }
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

        List<String> gameStateStrings = splitGameStateStrings(historyString);
        List<GameState> result = new ArrayList<>();

        // Print the parsed game states
        for (String gameStateString : gameStateStrings) {
            System.out.println(GameState.parseFromString(gameStateString));
            result.add(GameState.parseFromString(gameStateString));
        }

        return result;
    }

    private static List<String> splitGameStateStrings(String input) {
        List<String> gameStateStrings = new ArrayList<>();

        // Split the input string based on the word "GameState"
        String[] parts = input.split("GameState");

        // Add each substring to the list, excluding the empty ones
        for (String part : parts) {
            if (!part.trim().isEmpty()) {
                gameStateStrings.add("GameState" + part.trim());
            }
        }

        return gameStateStrings;
    }

}