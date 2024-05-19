package ru.itmo.lab5.util;

import java.util.Scanner;

public class InputHelper {
    private static final Scanner scanner = new Scanner(System.in);


    public static String requestString(String prompt, boolean allowNull) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!allowNull && input.isEmpty()) {
                System.out.println("This field cannot be empty. Please try again.");
            } else {
                return input.isEmpty() ? null : input;
            }
        }
    }

    public static int requestInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("No number has been entered. Please try again.");
            }
        }
    }

    public static double requestDouble(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("No number has been entered. Please try again.");
            }
        }
    }
}
