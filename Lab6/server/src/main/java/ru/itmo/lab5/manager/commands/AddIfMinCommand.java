package ru.itmo.lab5.manager.commands;

import ru.itmo.lab5.data.models.Ticket;
import ru.itmo.lab5.manager.CollectionManager;
import ru.itmo.lab5.manager.DatabaseHandler;
import ru.itmo.lab5.util.Task;

import java.util.Comparator;

public class AddIfMinCommand extends Command {
    public AddIfMinCommand(CollectionManager collectionManager, DatabaseHandler dbHandler) {
        super(collectionManager, dbHandler);
        this.name = "add_if_min";
        this.description = "Add new ticket to the collection if it is less than min";
    }

    @Override
    public Task execute(Task task) {
        Ticket newTicket = task.getTicket();
        if (newTicket == null) {
            return new Task(new String[]{"Unable to create new ticket."});
        }

        Ticket minTicket = collectionManager.getTickets().stream()
                .min(Comparator.comparing(Ticket::getPrice))
                .orElse(null);

        if (minTicket == null || newTicket.getPrice() < minTicket.getPrice()) {
            boolean added = collectionManager.add(newTicket);
            if (added) {
                return new Task(new String[]{"New ticket added."});
            } else {
                return new Task(new String[]{"Error adding new ticket."});
            }
        } else {
            return new Task(new String[]{"New ticket is not less than min."});
        }
    }
}
