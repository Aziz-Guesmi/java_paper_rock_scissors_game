package servers;

import service.GameOperations;
import serviceImpl.GameOperationsImpl;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class RmiServer {
    public static void main(String[] args) throws RemoteException, MalformedURLException {
        LocateRegistry.createRegistry(5005);
        GameOperations distanceObject = new GameOperationsImpl();

        System.out.println(distanceObject.toString());
        Naming.rebind("rmi://localhost:5005/gameOperations", distanceObject);
    }
}