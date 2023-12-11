package service;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GameOperations extends Remote {
    public String playRound(String choice, String sessionId, String gameId) throws RemoteException;

    public String getHistory(String sessionId) throws RemoteException;

}