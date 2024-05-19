package ru.itmo.lab5.util;

import ru.itmo.lab5.network.Client;
import ru.itmo.lab5.data.models.*;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Objects;

public class Execute_script {
    private final ArrayList<String> listOfPaths = new ArrayList<>();

    public void readScript(String path, Client client, String username) throws IOException, ClassNotFoundException {
        try (FileReader fileReader = new FileReader(path)) {
            Scanner scanner = new Scanner(fileReader);
            String[] commandComponents;

            while (scanner.hasNextLine()) {
                String command = scanner.nextLine().trim();
                commandComponents = (command + " ").split(" ", 2);
                commandComponents[1] = commandComponents[1].trim();

                Task task = new Task(commandComponents);
                task.setUsername(username);

                if (!Objects.equals(commandComponents[0], "execute_script")) {
                    if ("exit".equalsIgnoreCase(commandComponents[0])) {
                        System.exit(0);
                    }

                    if ("add".equalsIgnoreCase(commandComponents[0]) || "add_if_min".equalsIgnoreCase(commandComponents[0]) || "update_id".equalsIgnoreCase(commandComponents[0])) {
                        String name = scanner.nextLine().trim();
                        int x = Integer.parseInt(scanner.nextLine().trim());
                        double y = Double.parseDouble(scanner.nextLine().trim());
                        long price = Long.parseLong(scanner.nextLine().trim());
                        TicketType type = TicketType.valueOf(scanner.nextLine().trim().toUpperCase());
                        String venueName = scanner.nextLine().trim();
                        int capacity = Integer.parseInt(scanner.nextLine().trim());
                        VenueType venueType = VenueType.valueOf(scanner.nextLine().trim().toUpperCase());
                        String street = scanner.nextLine().trim();
                        String zipCode = scanner.nextLine().trim();
                        int townX = Integer.parseInt(scanner.nextLine().trim());
                        double townY = Double.parseDouble(scanner.nextLine().trim());
                        String townName = scanner.nextLine().trim();

                        Coordinates coordinates = new Coordinates(x, y);
                        Location town = new Location(townX, townY, townName);
                        Address address = new Address(street, zipCode, town);
                        Venue venue = new Venue(venueName, capacity, venueType, address);

                        Ticket ticket = new Ticket(name, coordinates, price, type, venue, username);
                        task.setTicket(ticket);
                    }

                    client.sendTask(task);
                } else {
                    if (!listOfPaths.contains(commandComponents[1])) {
                        listOfPaths.add(commandComponents[1]);
                        readScript(commandComponents[1], client, username);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error: File " + path + " not found.");
        } catch (Exception e) {
            System.err.println("Error processing script: " + e.getMessage());
        }
    }
}
