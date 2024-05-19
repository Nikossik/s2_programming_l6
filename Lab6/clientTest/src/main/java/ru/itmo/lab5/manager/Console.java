package ru.itmo.lab5.manager;

import ru.itmo.lab5.network.Client;
import ru.itmo.lab5.util.Execute_script;
import ru.itmo.lab5.util.Task;
import ru.itmo.lab5.util.TicketBuilder;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Console {
    private final Scanner scanner;
    private final Client client;
    private static final Logger logger = Logger.getLogger(Console.class.getName());
    private String username;
    private String password;

    public Console(Client client) throws IOException {
        this.scanner = new Scanner(System.in);
        this.client = new Client();
    }

    public void start() {
        try {
            System.out.println("Welcome to the ticket management console. Please log in or register.");
            while (!authenticate()) {
                System.out.println("Failed to log in or register. Please try again.");
            }

            System.out.println("Enter collection type (ArrayList/HashMap/ArrayDeque)");
            System.out.print("Type (1/2/3): ");
            String collectionType = scanner.nextLine().trim();

            System.out.println("Enter 'help' for a list of commands.");
            String[] userCommand;
            while (true) {
                Task task = new Task();
                task.setUsername(this.username);
                task.setPassword(this.password);
                task.setCollectionType(collectionType);
                System.out.print("> ");
                if (!scanner.hasNextLine()) {
                    break;
                }
                String input = scanner.nextLine().trim();
                userCommand = (input + " ").split(" ", 2);
                userCommand[1] = userCommand[1].trim();
                if ("exit".equalsIgnoreCase(userCommand[0])) {
                    System.exit(0);
                }
                if ("save".equalsIgnoreCase(userCommand[0])) {
                    System.out.println("Error: 'save' command is not allowed for users.");
                    continue;
                }
                task.setDescribe(userCommand);
                if ("add".equalsIgnoreCase(userCommand[0]) || "add_if_min".equalsIgnoreCase(userCommand[0]) || "update_id".equalsIgnoreCase(userCommand[0])) {
                    task.setTicket(TicketBuilder.buildTicket(username));
                }
                try {
                    if ("execute_script".equalsIgnoreCase(userCommand[0])) {
                        Execute_script.readScript(userCommand[1], client, username);
                    } else {
                        client.sendTask(task);
                    }
                } catch (NoSuchElementException e) {
                    System.out.println("Command '" + input + "' not found. Enter 'help' for assistance.");
                } catch (IOException | ClassNotFoundException e) {
                    logger.log(Level.SEVERE, "Error executing command: " + e.getMessage(), e);
                }
            }
        } catch (NoSuchElementException | IllegalStateException e) {
            logger.log(Level.SEVERE, "Input error. Exiting program.", e);
        }
    }

    private boolean authenticate() {
        while (true) {
            System.out.print("Enter command (login/register): ");
            String command = scanner.nextLine().trim();

            if ("login".equalsIgnoreCase(command)) {
                if (login()) {
                    return true;
                } else {
                    System.out.println("Login failed.");
                }
            } else if ("register".equalsIgnoreCase(command)) {
                if (register()) {
                    System.out.println("Registration successful.");
                    return true;
                } else {
                    System.out.println("Registration failed.");
                }
            } else {
                System.out.println("Unknown command. Please enter 'login' or 'register'.");
            }
        }
    }

    private boolean login() {
        System.out.print("Enter username: ");
        this.username = scanner.nextLine().trim();
        System.out.print("Enter password: ");
        this.password = scanner.nextLine().trim();

        Task task = new Task(new String[]{"login"}, null, this.username, this.password);
        try {
            return client.sendTask(task);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error during login: " + e.getMessage());
            return false;
        }
    }

    private boolean register() {
        System.out.print("Enter username: ");
        this.username = scanner.nextLine().trim();
        System.out.print("Enter password: ");
        this.password = scanner.nextLine().trim();

        Task task = new Task(new String[]{"register"}, null, this.username, this.password);
        try {
            return client.sendTask(task);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error during registration: " + e.getMessage());
            return false;
        }
    }
}
