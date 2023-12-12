package service;

import models.GameState;
import models.ServerResponse;

import java.io.*;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.util.Scanner;
import java.util.UUID;

import static utils.Utils.containsChoice;
import static utils.Utils.printMenu;

public class Client {

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        Scanner scanner = new Scanner(System.in);
        Socket socket = new Socket("localhost", 5019);

        PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
      //  InputStreamReader in = new InputStreamReader(socket.getInputStream());
        ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
      //  BufferedReader bufferedReader = new BufferedReader(in);

        printMenu("Menu", "RMI", "RPC", "Quit");

        // choice de protocole
        System.out.println("Please choose a protocol : ");
        int protocolChoice = scanner.nextInt();
        String clientId = String.valueOf(UUID.randomUUID());
        printWriter.println(clientId);

        int continuePlaying = 1;
        while ((protocolChoice != 3) && (continuePlaying != 3)) {
            printWriter.println(continuePlaying);

            if (continuePlaying == 1) {
                printWriter.println(protocolChoice);

                for (int i = 1; i < 4; i++) {
                    System.out.println("Round " + i + " : your turn ");
                    String clientChoice = scanner.nextLine();
                    while (!containsChoice(clientChoice)) {
                        System.out.println("Please Enter your choice ");
                        clientChoice = scanner.nextLine();
                    }
                    printWriter.println(clientChoice);

                    ServerResponse serverResponse = (ServerResponse) objectInputStream.readObject();
                    System.out.println(serverResponse.getGame().getPreviousRound());
                    GameState game = serverResponse.getGame();
                   if (game.getWinner() != null){
                       System.out.println("Game ended");
                       System.out.println(game);
                       break;
                   }
                }
            } else if (continuePlaying == 2){
                ServerResponse serverResponse = (ServerResponse) objectInputStream.readObject();

                System.out.println("Printing session history");
                System.out.println(serverResponse.getSession());

            }

            printMenu("Menu", "Continue playing", "Show History", "Quit");

            // TODO : control saisie
            continuePlaying = scanner.nextInt();
            if(continuePlaying == 1 )
            {    printMenu("Menu", "RMI", "RPC", "Quit");
                protocolChoice = scanner.nextInt();


            }
        }

        // close
        scanner.close();
        socket.close();
    }


}