package service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.util.Scanner;
import java.util.UUID;

import static utils.Utils.containsChoice;
import static utils.Utils.printMenu;

public class Client {

    public static void main(String[] args) throws IOException {

        Scanner scanner = new Scanner(System.in);
        Socket socket = new Socket("localhost", 5008);

        PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
        InputStreamReader in = new InputStreamReader(socket.getInputStream());
        BufferedReader bufferedReader = new BufferedReader(in);

        printMenu("Menu", "RMI", "RPC", "Quit");

        // choice de protocole
        System.out.println("Please choose a protocol : ");
        int choice = scanner.nextInt();
        printWriter.println(choice);
        String clientId = String.valueOf(UUID.randomUUID());
        printWriter.println(clientId);

        int continuePlaying = choice;
        while (continuePlaying != 3) {
            printWriter.println(continuePlaying);
            if (continuePlaying == 1) {
                for (int i = 1; i < 4; i++) {
                    System.out.println("Round " + i + " : your turn ");
                    String clientChoice = scanner.nextLine();
                    while (!containsChoice(clientChoice)) {
                        System.out.println("Please Enter your choice again");
                        clientChoice = scanner.nextLine();
                    }
                    printWriter.println(clientChoice);

                    String serverResponse = bufferedReader.readLine();
                    if (serverResponse.equals("Your game is already over")) {
                        break;
                    } else
                        System.out.println(serverResponse);
                }
            } else if (continuePlaying == 2)
                System.out.println(bufferedReader.readLine());

            printMenu("Menu", "Continue playing", "Show History", "Quit");

            // TODO : control saisie
            continuePlaying = scanner.nextInt();

        }

        // close
        scanner.close();
        socket.close();
    }


}