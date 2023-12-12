package models;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class GameStateTest {

    @ParameterizedTest
    @CsvSource({
            "ROCK, SCISSORS, CLIENT, Server: 0 - Client : 1 - Draws 0",
            "PAPER, ROCK, CLIENT, Server: 0 - Client : 1 - Draws 0",
            "SCISSORS, PAPER, CLIENT, Server: 0 - Client : 1 - Draws 0",
            "ROCK, PAPER, SERVER, Server: 1 - Client : 0 - Draws 0",
            "PAPER, SCISSORS, SERVER, Server: 1 - Client : 0 - Draws 0",
            "SCISSORS, ROCK, SERVER, Server: 1 - Client : 0 - Draws 0",
            "ROCK, ROCK, DRAW, Server: 0 - Client : 0 - Draws 1",
            "PAPER, PAPER, DRAW, Server: 0 - Client : 0 - Draws 1",
            "SCISSORS, SCISSORS, DRAW, Server: 0 - Client : 0 - Draws 1",
    })
    void testPlayRoundInGame(Choice clientChoice, Choice randomChoice, Winner expectedWinner, String expectedScore) {
        // Create an instance of GameState
        GameState game = new GameState();

        // Perform the method call
        game.playRoundInGame(clientChoice, randomChoice);

        // Check the expected behavior

        // Verify that a round has been added to the history
        assertEquals(1, game.getHistory().size(), "Unexpected number of rounds in history");

        Round lastRound = game.getHistory().getLast();
        // Verify that the winner is set correctly based on the choices
        assertEquals(expectedWinner, lastRound.getWinner(), "Unexpected winner");

        // Verify that the score is set correctly
        assertEquals(expectedScore, game.getScore(), "Unexpected score");
    }
}