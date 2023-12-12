package serviceImpl;

import models.Choice;
import models.GameState;
import models.ServerResponse;
import models.SessionState;
import service.GameOperations;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static utils.Utils.getRandomChoice;

public class GameOperationsImpl extends UnicastRemoteObject implements GameOperations {

    public static Map<String, SessionState> state = new ConcurrentHashMap<>();

    public GameOperationsImpl() throws RemoteException {
        super();
    }

    @Override
    public ServerResponse playRound(String choice, String sessionId, String gameId) throws RemoteException {

        SessionState session = state.computeIfAbsent(sessionId, k -> new SessionState());

        GameState game = session.getHistory().stream().filter(gameState -> (gameState.getId().equals(gameId)))
                .findAny().orElse(null);

        if (game == null) {
            game = new GameState();
            game.setId(gameId);
            session.getHistory().add(game);
        }

        Choice randomChoice = getRandomChoice();

        game.playRoundInGame(Choice.valueOf(choice.toUpperCase()), randomChoice);
        ServerResponse response = new ServerResponse();
        response.setGame(game);

        String responseString = response.toString();
        ServerResponse parsedResponse = ServerResponse.parseFromString(responseString);
        System.out.println("here");
        System.out.println("Final test" + parsedResponse.toString());
        return response;
    }


    public ServerResponse getHistory(String sessionId) throws RemoteException

    {
        ServerResponse response = new ServerResponse();
        SessionState session = state.get(sessionId);
        response.setSession(state.get(sessionId));

        if (session == null) {
            response.setError("You have not played yet!");
        }
        return response;

    }


}