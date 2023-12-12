package serviceImpl;

import models.Choice;
import models.GameState;
import models.SessionState;
import service.GameOperations;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

import static utils.Utils.getRandomChoice;

public class GameOperationsImpl extends UnicastRemoteObject implements GameOperations {

    static Map<String, SessionState> state = new HashMap<>();

    public GameOperationsImpl() throws RemoteException {
        super();
    }

    @Override
    public String playRound(String choice, String sessionId, String gameId) throws RemoteException {

        SessionState session = state.computeIfAbsent(sessionId, k -> new SessionState());

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

        Choice randomChoice = getRandomChoice();

        game.playRoundInGame(Choice.valueOf(choice.toUpperCase()), randomChoice);

        String result = game.printPreviousRound();
        System.out.println("Previous round printed: " + result);

        if (game.getWinner() != null) {
            result += game.toString();
            System.out.println("Game winner found: " + game.getWinner());
        }

        System.out.println("Exiting playRound method...");
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


}