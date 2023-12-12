package utils;

import service.Choice;

public class Utils {
    public static void printMenu(String... args) {
        System.out.println("---------Menu--------");
        for (int i = 1; i < args.length; i++) {
            System.out.println(i + "/- " + args[i]);
        }
    }

    public static boolean containsChoice(String test) {
        for (Choice choice : Choice.values()) {
            if (choice.name().equals(test.toUpperCase())) {
                return true;
            }
        }
        return false;
    }
}
