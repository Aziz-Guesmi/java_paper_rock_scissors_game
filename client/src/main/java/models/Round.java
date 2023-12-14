package models;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Round implements Serializable {
    Choice client;
    Choice server;
    Winner winner;

    public void determineWinner(Choice clientChoice, Choice serverChoice) {
        setClient(clientChoice);
        setServer(serverChoice);
        Winner winner;
        if (clientChoice.equals(serverChoice)) {

            winner = Winner.DRAW;
        } else if ((clientChoice.equals(Choice.ROCK) && serverChoice.equals(Choice.SCISSORS)) ||
                (clientChoice.equals(Choice.PAPER) && serverChoice.equals(Choice.ROCK)) ||
                (clientChoice.equals(Choice.SCISSORS) && serverChoice.equals(Choice.PAPER))) {
            winner = Winner.CLIENT;
        } else {
            winner = Winner.SERVER;
        }
        setWinner(winner);
    }

    @Override
    public String toString() {
        return "\n  Round{" +
                "client=" + client +
                ", server=" + server +
                ", winner=" + winner +
                "}";
    }

    public Choice getClient() {
        return client;
    }

    public void setClient(Choice client) {
        this.client = client;
    }

    public Choice getServer() {
        return server;
    }

    public void setServer(Choice server) {
        this.server = server;
    }

    public Winner getWinner() {
        return winner;
    }

    public void setWinner(Winner winner) {
        this.winner = winner;
    }

    // Parser method for Round
    public static Round parseFromString(String input) {
        Round round = new Round();

        // Extract client
        Matcher clientMatcher = Pattern.compile("client=([A-Za-z0-9_]+),?").matcher(input);
        if (clientMatcher.find()) {
            round.client = Choice.valueOf(clientMatcher.group(1));
        }

        // Extract server
        Matcher serverMatcher = Pattern.compile("server=([A-Za-z0-9_]+),?").matcher(input);
        if (serverMatcher.find()) {
            round.server = Choice.valueOf(serverMatcher.group(1));
        }
        // Extract winner
        Matcher winnerMatcher = Pattern.compile("winner=([A-Za-z0-9_]+)").matcher(input);
        if (winnerMatcher.find()) {
            String winnerValue = winnerMatcher.group(1);
            if ("DRAW".equals(winnerValue)) {
                round.winner = Winner.DRAW;
            } else {
                round.winner = Winner.valueOf(winnerValue);
            }
        }
        return round;
    }
}