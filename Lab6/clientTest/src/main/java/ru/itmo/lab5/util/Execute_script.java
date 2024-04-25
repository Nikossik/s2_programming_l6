package ru.itmo.lab5.util;

import ru.itmo.lab5.network.Client;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class Execute_script {
    private final ArrayList<String> listOfPaths= new ArrayList<>();

    public void readScript(String path, Client client, TicketBuilder ticketBuilder) throws IOException {
        FileReader fileReader= new FileReader(path);
        if (!fileReader.ready()){
            System.out.println("Что то неправильно с файлом");
        }
        else {
            Scanner scanner = new Scanner(fileReader);
            String[] commandComponents;

            try{
                while (scanner.hasNextLine()) {
                    String command = scanner.nextLine().trim();
                    commandComponents = (command + " ").split(" ", 2);
                    commandComponents[1] = commandComponents[1].trim();
                    if (!Objects.equals(commandComponents[0], "execute_script")) {
                        Task task = new Task(commandComponents);
                        if ("exit".equalsIgnoreCase(commandComponents[0])) {
                            System.exit(0);
                        }
                        if ("add".equalsIgnoreCase(commandComponents[0]) || "add_if_min".equalsIgnoreCase(commandComponents[0]) || "update_id".equalsIgnoreCase(commandComponents[0])) {
                            task.ticket = TicketBuilder.buildTicket();
                        }
                        client.sendTask(task);
                    } else {
                        if (!listOfPaths.contains(commandComponents[1])) {
                            listOfPaths.add(commandComponents[1]);
                            readScript(commandComponents[1], client, ticketBuilder);
                        }
                    }
                }
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
