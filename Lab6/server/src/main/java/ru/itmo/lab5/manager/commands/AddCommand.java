package ru.itmo.lab5.manager.commands;

import ru.itmo.lab5.data.models.Ticket;
import ru.itmo.lab5.manager.CollectionManager;
import ru.itmo.lab5.manager.DatabaseHandler;
import ru.itmo.lab5.util.Task;

public class AddCommand extends Command {
    public AddCommand(CollectionManager collectionManager, DatabaseHandler dbHandler) {
        super(collectionManager, dbHandler);
        this.name = "add";
        this.description = "Adds an element to the collection";
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
