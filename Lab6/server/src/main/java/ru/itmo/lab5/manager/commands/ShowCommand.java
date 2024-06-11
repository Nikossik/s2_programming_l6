package ru.itmo.lab5.manager.commands;

import ru.itmo.lab5.data.models.Ticket;
import ru.itmo.lab5.manager.DatabaseHandler;
import ru.itmo.lab5.util.Task;

import java.util.List;

public class ShowCommand extends Command {

    public ShowCommand(DatabaseHandler dbHandler) {
        super(dbHandler);
        this.name = "show";
        this.description = "List all tickets";
    }

    @Override
    public Task execute(Task task) {
        try {
            List<Ticket> tickets = dbHandler.getTickets();
            if (tickets.isEmpty()) {
                return new Task(new String[]{"Collection is empty."});
            } else {
                return new Task(tickets);
            }
        } catch (Exception e) {
            return new Task(new String[]{"Error retrieving tickets: " + e.getMessage()});
        }
    }
}
