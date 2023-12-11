package serviceImpl;

import models.Choice;
import models.GameState;
import models.SessionState;
import service.GameOperations;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class GameOperationsImpl extends UnicastRemoteObject implements GameOperations {

    static Map<String, SessionState> state = new HashMap<>();
    private static final Random PRNG = new Random();

    public GameOperationsImpl() throws RemoteException {
        super();
    }

    @Override
    public String playRound(String choice, String sessionId, String gameId) throws RemoteException {
        SessionState session = state.get(sessionId);
        if (session == null) {
            session = new SessionState();
            state.put(sessionId, session);
        }
        GameState game = session.getHistory().stream().filter(gameState -> (gameState.getId().equals(gameId)))
                .findAny().orElse(null);
        if (game == null) {
            game = new GameState();
            game.setId(gameId);
            session.getHistory().add(game);
        }
        if (game.getWinner() != null) {
            throw new RemoteException("Cannot play when there is already a winner");
        }
        /*
         * if (!contains(choice)) {
         * throw new RuntimeException("Choice not found");
         * }
         */
        Choice randomChoice = getRandomChoice();

        game.playRoundInGame(Choice.valueOf(choice), randomChoice);

        String result = game.printPreviousRound();

        if (game.getWinner() != null) {
            result += game.toString();
        }

        return result;
    }

    public String getHistory(String sessionId) throws RemoteException

    {
        SessionState session = state.get(sessionId);
        if (session == null) {
            return "You didnt play yet";
        }
        return session.getHistory().toString();

    }

    private static boolean contains(String test) {

        for (Choice c : Choice.values()) {
            if (c.name().toUpperCase().equals(test)) {
                return true;
            }
        }

        return false;
    }

    // TODO: goes in utils folder
    private Choice getRandomChoice() {
        Random random = new Random();
        Choice[] choices = Choice.values();
        return choices[random.nextInt(choices.length)];
    }
}