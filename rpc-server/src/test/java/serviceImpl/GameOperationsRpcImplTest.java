package serviceImpl;

import models.Choice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import utils.Utils;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.*;

class GameOperationsRpcImplTest {


    private GameOperationsImplRpc gameOperations;

    @BeforeEach
    void setUp() throws RemoteException {
        gameOperations = new GameOperationsImplRpc();
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
        GameOperationsImplRpc spyGameOperations = Mockito.spy(gameOperations);
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
    void testGetHistoryWhenEmpty() throws RemoteException {
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

    @Test
    void testStateOnMultipleClients() throws RemoteException {
        int numberOfSessions = 10;
        int numberOfGamesPerSession = 3;
        for (int i =0; i<numberOfSessions; i++){
            String sessionId = String.valueOf(UUID.randomUUID());
            for (int j = 0; j <numberOfGamesPerSession; j++){
                String gameId = String.valueOf(UUID.randomUUID());
                String roundResult = gameOperations.playRound("ROCK", sessionId,gameId);

            }
        }
        System.out.println("State" + GameOperationsImplRpc.state);
        assertEquals(GameOperationsImplRpc.state.keySet().size(), numberOfSessions);
    }

    @Test
    void testStateOnMultipleClientsConcurrently() throws Exception {
        int numberOfSessions = 10;
        int numberOfGamesPerSession = 3;

        ExecutorService executorService = Executors.newFixedThreadPool(numberOfSessions);

        List<Future<Void>> futures = new ArrayList<>();

        for (int i = 0; i < numberOfSessions; i++) {
            String sessionId = String.valueOf(UUID.randomUUID());

            Callable<Void> sessionTask = () -> {
                for (int j = 0; j < numberOfGamesPerSession; j++) {
                    String gameId = String.valueOf(UUID.randomUUID());
                    String roundResult = gameOperations.playRound("ROCK", sessionId, gameId);
                }
                return null;
            };

            futures.add(executorService.submit(sessionTask));
        }

        // Wait for all tasks to complete
        for (Future<Void> future : futures) {
            future.get(); // This will block until the task is completed
        }

        executorService.shutdown();

        System.out.println("State" + GameOperationsImplRpc.state);
        assertEquals(GameOperationsImplRpc.state.keySet().size(), numberOfSessions);
    }
}