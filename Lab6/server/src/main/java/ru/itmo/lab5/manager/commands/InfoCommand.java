package ru.itmo.lab5.manager.commands;

import ru.itmo.lab5.manager.CollectionManager;
import ru.itmo.lab5.manager.DatabaseHandler;
import ru.itmo.lab5.util.Task;

import java.time.format.DateTimeFormatter;

public class InfoCommand extends Command {

    public InfoCommand(CollectionManager collectionManager, DatabaseHandler dbHandler) {
        super(collectionManager, dbHandler);
        this.name = "info";
        this.description = "Displays information about the collection";
    }

    @Override
    public Task execute(Task task) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

        String answer = "";
        answer = answer + "information about the collection:\n";

        int ticketCount = collectionManager.getTickets().size();
        answer = answer + "Number of elements: " + ticketCount + "\n";

        String initializationDate = collectionManager.getInitializationDate().format(formatter);
        answer = answer + "Date of collection initialization: " + initializationDate + "\n";

        String lastSaveTime = collectionManager.getLastSaveTime() != null
                ? collectionManager.getLastSaveTime().format(formatter)
                : "The collection has not been saved yet.";
        answer = answer + "Date of last save: " + lastSaveTime + "\n";

        return new Task(new String[]{answer});
    }
}
