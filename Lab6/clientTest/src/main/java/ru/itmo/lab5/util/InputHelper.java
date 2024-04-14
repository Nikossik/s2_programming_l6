package ru.itmo.lab5.util;

import java.util.Scanner;

public class InputHelper {
    private static final Scanner scanner = new Scanner(System.in);


    /**
     * Запрашивает у пользователя строку с заданным приглашением к вводу.
     *
     * @param prompt Приглашение к вводу.
     * @param allowNull Разрешить ли ввод пустой строки.
     * @return Введенная пользователем строка или null, если ввод пустой и allowNull истинно.
     */
    public static String requestString(String prompt, boolean allowNull) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!allowNull && input.isEmpty()) {
                System.out.println("Это поле не может быть пустым. Пожалуйста, попробуйте снова.");
            } else {
                return input.isEmpty() ? null : input;
            }
        }
    }

    /**
     * Запрашивает у пользователя целое число с заданным приглашением к вводу.
     *
     * @param prompt Приглашение к вводу.
     * @return Введенное пользователем целое число.
     */
    public static int requestInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Введено не число. Пожалуйста, попробуйте снова.");
            }
        }
    }

    /**
     * Запрашивает у пользователя число с плавающей точкой с заданным приглашением к вводу.
     *
     * @param prompt Приглашение к вводу.
     * @return Введенное пользователем число с плавающей точкой.
     */
    public static double requestDouble(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Введено не число. Пожалуйста, попробуйте снова.");
            }
        }
    }

    /**
     * Запрашивает у пользователя длинное целое число с заданным приглашением к вводу.
     *
     * @param prompt Приглашение к вводу.
     * @return Введенное пользователем длинное целое число.
     */
    public static long requestLong(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Long.parseLong(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Введено не число. Пожалуйста, попробуйте снова.");
            }
        }
    }
}
