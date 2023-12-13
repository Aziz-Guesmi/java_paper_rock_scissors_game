package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GameState implements Serializable {
    private String id;
    private List<Round> history;
    private String score;
    private Winner winner;

    public GameState() {
        this.history = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Round> getHistory() {
        return history;
    }

    public void setHistory(List<Round> history) {
        this.history = history;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public Winner getWinner() {
        return winner;
    }

    public void setWinner(Winner winner) {
        this.winner = winner;
    }

    public void playRoundInGame(Choice choice, Choice randomChoice) {
        Round round = new Round();
        round.determineWinner(choice, randomChoice);
        List<Round> gameRounds = this.getHistory();
        // TODO: check this in future
        gameRounds.add(round);

        int clientWins = 0;
        int serverWins = 0;
        int draws = 0;

        for (Round tempRound : gameRounds) {

            switch (tempRound.getWinner()) {
                case CLIENT:
                    clientWins++;
                    break;
                case SERVER:
                    serverWins++;
                    break;
                case DRAW:
                    draws++;
                    break;
                default:
                    // Handle unexpected cases or ignore them
                    break;
            }
        }

        if (this.getHistory().size() == 2) {
            if (clientWins > serverWins && clientWins == 2) {
                setWinner(Winner.CLIENT);
            } else if (serverWins > clientWins && serverWins == 2)
                setWinner(Winner.SERVER);
        } else if (this.getHistory().size() == 3) {
            if (clientWins > serverWins) {
                setWinner(Winner.CLIENT);
            } else if (serverWins > clientWins)
                setWinner(Winner.SERVER);
            else
                setWinner(Winner.DRAW);
        }

        setScore(String.format("Server: %s - Client : %s - Draws %s", serverWins, clientWins, draws));

    }

    public String printPreviousRound() {
        Round lastRound = this.getHistory().getLast();

        return lastRound.toString();
    }
    public Round getPreviousRound() {
        Round lastRound = this.getHistory().getLast();

        return lastRound;
    }


    @Override
    public String toString() {
        return "GameState{" +
                "id='" + id + '\'' +
                ", history=" + history +
                ", score='" + score + '\'' +
                ", winner=" + winner +
                '}';
    }

    // Parser method for GameState
    public static GameState parseFromString(String input) {
        GameState gameState = new GameState();

        // Parse id
        Pattern idPattern = Pattern.compile("id='(.+?)'");
        Matcher idMatcher = idPattern.matcher(input);
        if (idMatcher.find()) {
            gameState.id = idMatcher.group(1);
        }

        // Parse history
        Pattern historyPattern = Pattern.compile("history=\\[([^\\]]+)\\]");
        Matcher historyMatcher = historyPattern.matcher(input);
        if (historyMatcher.find()) {
            gameState.history = parseHistory(historyMatcher.group(1));
        }

        // Parse score
        Pattern scorePattern = Pattern.compile("score='([^']+)'");
        Matcher scoreMatcher = scorePattern.matcher(input);
        if (scoreMatcher.find()) {
            gameState.score = scoreMatcher.group(1);
        }

        // Parse winner
        Pattern winnerPattern = Pattern.compile("winner=([^,}]+)\\}");
        Matcher winnerMatcher = winnerPattern.matcher(input);
        if (winnerMatcher.find()) {
            String match = winnerMatcher.group(1);
            if (match == null){
                gameState.winner = null;
            }else{
                gameState.winner = Winner.valueOf(match);
            }
        }



        return gameState;
    }

    public static List<Round> parseHistory(String historyString) {
        List<Round> history = new ArrayList<>();

        // Assume rounds are separated by commas
        String[] roundStrings = historyString.split(",(?![^\\{]*\\})");

        // Parse each round string and add it to the history list
        for (String roundString : roundStrings) {
            Round round = Round.parseFromString(roundString);
            history.add(round);
        }

        return history;
    }

}