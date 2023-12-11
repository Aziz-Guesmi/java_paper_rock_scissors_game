package models;

import java.util.ArrayList;
import java.util.List;

public class GameState {
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
            System.out.println("history setting winner ");
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
        Round lastRound = this.getHistory().get(this.getHistory().size() - 1);

        return lastRound.toString();
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
}