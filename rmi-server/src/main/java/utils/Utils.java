package utils;

import models.Choice;

import java.util.Random;

public class Utils {
    public static  boolean contains(String test) {

        for (Choice c : Choice.values()) {
            if (c.name().toUpperCase().equals(test)) {
                return true;
            }
        }

        return false;
    }

    public static Choice getRandomChoice() {
        Random random = new Random();
        Choice[] choices = Choice.values();
        return choices[random.nextInt(choices.length)];
    }
}
