package serviceImpl;

import models.Choice;
import models.Winner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import utils.Utils;

import java.rmi.RemoteException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class GameOperationsImplTest {


    private GameOperationsImpl gameOperations;

    @BeforeEach
    void setUp() throws RemoteException {
        gameOperations = new GameOperationsImpl();
    }

    @Test
    void testPlayRound() throws RemoteException {
        // Mock data for testing
        String choice = "Rock";
        String sessionId = String.valueOf(UUID.randomUUID());
        String gameId = String.valueOf(UUID.randomUUID());

        // Perform the method call
        String result = gameOperations.playRound(choice, sessionId, gameId);
        System.out.println(result);
        // Add your assertions based on the expected behavior of your method
        assertNotNull(result);
        assertTrue(result.contains("Round{client="+choice.toUpperCase()), "Result should contain 'Round{client='");

        // Assert that the 'winner' field is not null
        assertTrue(result.contains("winner="), "Result should contain 'winner='");
        int winnerIndex = result.indexOf("winner=") + "winner=".length();
        String winnerValue = result.substring(winnerIndex, result.indexOf("}", winnerIndex));
        System.out.println("winner" + winnerValue);
        assertNotNull(winnerValue, "Winner value should not be null");
    }       // Assert that the winner value is one of the enum values

    @Test
    void testPlayGameOfTwoRounds() throws RemoteException {
        // Mock data for testing
        String choice = "Rock";
        String sessionId = String.valueOf(UUID.randomUUID());
        String gameId = String.valueOf(UUID.randomUUID());

        // Mock the getRandomChoice() method
        GameOperationsImpl spyGameOperations = Mockito.spy(gameOperations);
        MockedStatic<Utils> utils = Mockito.mockStatic(Utils.class);
        utils.when(Utils::getRandomChoice).thenReturn(Choice.SCISSORS);

        // Perform the method call
        String result = "";
        spyGameOperations.playRound(choice, sessionId, gameId);

        result = spyGameOperations.playRound(choice, sessionId, gameId);
        // Add your assertions based on the expected behavior of your method
        assertTrue(result.contains("GameState"), "Result should contain 'GameState'");

        System.out.println(result);
        assertThrows(RemoteException.class,() -> spyGameOperations.playRound(choice, sessionId, gameId));

     }


    @Test
    void testGetHistory() throws RemoteException {
        // Mock data for testing
        String sessionId = String.valueOf(UUID.randomUUID());

        // Perform the method call
        String history = gameOperations.getHistory(sessionId);
        System.out.println(history);
        // Add your assertions based on the expected behavior of your method
        assertNotNull(history);
        assertTrue(history.contains("You didnt play yet"), "History should contain 'You didnt play yet'");
        System.out.println("History: " + history);
    }
}