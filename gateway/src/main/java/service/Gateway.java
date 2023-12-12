package service;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Gateway {
    private static final ExecutorService pool = Executors.newFixedThreadPool(4);

    public static void main(String[] args) throws Exception {
        try {
            ServerSocket serverSocket = new ServerSocket(5019);
            System.out.println("Server is running.");
            while(true) {
                Socket socket = serverSocket.accept();

                System.out.println("New client  connected.");

                ClientHandler clientThread = new ClientHandler(socket);
                pool.execute(clientThread);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}