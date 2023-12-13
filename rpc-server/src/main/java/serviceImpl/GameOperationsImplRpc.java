package serviceImpl;

import models.Choice;
import models.GameState;
import models.ServerResponse;
import models.SessionState;
import service.GameOperationsRpc;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static utils.Utils.getRandomChoice;

public class GameOperationsImplRpc extends UnicastRemoteObject implements GameOperationsRpc {

    public static Map<String, SessionState> state = new ConcurrentHashMap<>();

    public GameOperationsImplRpc() throws RemoteException {
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

        Choice randomChoice = getRandomChoice();

        game.playRoundInGame(Choice.valueOf(choice.toUpperCase()), randomChoice);
        ServerResponse response = new ServerResponse();
        response.setGame(game);
        ServerResponse tempResp = ServerResponse.parseFromString(response.toString());
        return response.toString();
    }


    public String getHistory(String sessionId) throws RemoteException

    {
        SessionState session = state.get(sessionId);
        if (session == null) {
            return "You didnt play yet";
        }
        ServerResponse response = new ServerResponse();
        response.setSession(session);
        String result = response.toString();

        ServerResponse testParsing = ServerResponse.parseFromString(result);
        System.out.println("test parsing "+testParsing);

        return result;

    }

}