package service;

import models.ServerResponse;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GameOperations extends Remote {
    public ServerResponse playRound(String choice, String sessionId, String gameId) throws RemoteException;

    public ServerResponse getHistory(String sessionId) throws RemoteException;



}