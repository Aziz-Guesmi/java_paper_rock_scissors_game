package service;

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

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

public class ClientHandler extends Thread {

    private Socket socket;
    private String protocol;

    public ClientHandler(Socket socket) throws Exception, NotBoundException {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {

            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
            InputStreamReader in = new InputStreamReader(socket.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(in);

            // le choice
            int choice = Integer.parseInt(bufferedReader.readLine());
            String sessionId = bufferedReader.readLine();

            int continuePlaying = Integer.parseInt(bufferedReader.readLine());
            while (continuePlaying != 3) {
                System.out.println(" playing ");
                String result = "";
                if (choice == 1) {
                    String gameId = String.valueOf(UUID.randomUUID());
                    GameOperations operations = (GameOperations) Naming.lookup("rmi://localhost:5015/gameOperations");
                    if (continuePlaying == 2) {
                        result = operations.getHistory(sessionId);
                        printWriter.println(result);
                    }

                    else
                        for (int i = 0; i < 3; i++) {
                            String clientChoice = bufferedReader.readLine();
                            try {

                                result = operations.playRound(clientChoice, sessionId, gameId);

                            } catch (RemoteException e) {
                                result = "Your game already over";
                            }
                            printWriter.println(result);

                        }

                } else if (choice == 2) {

                    String gameId = String.valueOf(UUID.randomUUID());
                    XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
                    config.setServerURL(new URL("http://localhost:5007/xmlrpc"));
                    XmlRpcClient client = new XmlRpcClient();
                    client.setConfig(config);
                    if (continuePlaying == 2) {
                        result = (String) client.execute("Game.getHistory",
                                new Object[] { sessionId });
                        printWriter.println(result);
                    } else
                        for (int i = 0; i < 3; i++) {
                            try {
                                result = (String) client.execute("Game.playRound",
                                        new Object[] { bufferedReader.readLine(), sessionId, gameId });
                            } catch (RemoteException e) {
                                result = "Your game already over";
                            }
                            printWriter.println(result);

                        }

                }

                else {
                    throw new RuntimeException("Unknown protocol: " + protocol);
                }

                continuePlaying = Integer.parseInt(bufferedReader.readLine());

            }

        } catch (Exception e) {
            System.out.println(e);

        }
    }
}