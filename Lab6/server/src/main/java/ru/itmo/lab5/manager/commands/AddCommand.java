package ru.itmo.lab5.manager.commands;

import ru.itmo.lab5.manager.CollectionManager;
import ru.itmo.lab5.data.models.Ticket;
import ru.itmo.lab5.util.Task;

/**
 * Command for adding a new ticket to the collection.
 */
public class AddCommand extends Command {

    private final CollectionManager collectionManager;

    public AddCommand(CollectionManager collectionManager) {
        super("add", "Adds a new ticket to the collection", collectionManager);
        this.collectionManager = collectionManager;
    }

    @Override
    public Task execute(Task task) {
        try {
            Ticket ticket = task.getTicket();
            if (ticket != null && ticket.validate()) {
                collectionManager.add(ticket);
                return new Task(new String[]{"Ticket added to collection."});
            } else {
                return new Task(new String[]{"Invalid ticket data."});
            }
        } catch (Exception e) {
            return new Task(new String[]{"Error adding ticket: " + e.getMessage()});
        }
    }
}
