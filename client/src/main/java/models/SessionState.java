package models;

import java.util.ArrayList;
import java.util.List;

public class SessionState {

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
}