package service;

import java.io.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.UUID;

import models.ServerResponse;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

public class ClientHandler extends Thread {

    private final Socket socket;

    public ClientHandler(Socket socket) throws Exception, NotBoundException {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            InputStreamReader in = new InputStreamReader(socket.getInputStream());
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
           BufferedReader bufferedReader = new BufferedReader(in);

            String sessionId = bufferedReader.readLine();

            int continuePlaying = Integer.parseInt(bufferedReader.readLine());
            int protocolChoice = Integer.parseInt(bufferedReader.readLine());

            while (protocolChoice != 3) {
                System.out.println(" playing ");
                ServerResponse serverResponse = new ServerResponse();

                if (protocolChoice == 1) {
                    String gameId = String.valueOf(UUID.randomUUID());
                    GameOperations operations = (GameOperations) Naming.lookup("rmi://localhost:5005/gameOperations");
                    if (continuePlaying == 2) {
                        serverResponse = operations.getHistory(sessionId);
                        objectOutputStream.writeObject(serverResponse);
                    }
                    else{
                        for (int i = 0; i < 3; i++) {
                            String clientChoice = bufferedReader.readLine();
                            serverResponse = operations.playRound(clientChoice, sessionId, gameId);
                            objectOutputStream.writeObject(serverResponse);
                            if (serverResponse.getGame().getWinner() != null)
                                break;
                        }
                    }
                } else if (protocolChoice == 2) {
                    String gameId = String.valueOf(UUID.randomUUID());
                    XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
                    config.setServerURL(new URL("http://localhost:5050/xmlrpc"));
                    XmlRpcClient client = new XmlRpcClient();
                    client.setConfig(config);
                    if (continuePlaying == 2) {
                       String responseString = (String) client.execute("Game.getHistory",
                                new Object[] { sessionId });
                       System.out.println(" response string " + responseString);

                        objectOutputStream.writeObject(responseString);

                    } else

                        for (int i = 0; i < 3; i++) {
                            String clientChoice = bufferedReader.readLine();
                            String response = (String) client.execute("Game.playRound",
                                        new Object[] { clientChoice, sessionId, gameId });
                            System.out.println(" response string " + response);
                            objectOutputStream.writeObject(serverResponse);
                            if (serverResponse.getGame().getWinner() != null)
                                break;
                        }
                }
                else {
                    System.out.println("execption ");

                    throw new RuntimeException("Unknown protocol: " + protocolChoice);

                }

                continuePlaying = Integer.parseInt(bufferedReader.readLine());
                if (continuePlaying == 1)
                    protocolChoice = Integer.parseInt(bufferedReader.readLine());

            }

        } catch (Exception e) {
            System.out.println(e);

        }
    }
}