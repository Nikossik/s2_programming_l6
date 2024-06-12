package ru.itmo.lab5.manager.commands;

import ru.itmo.lab5.data.models.Ticket;
import ru.itmo.lab5.manager.DatabaseHandler;
import ru.itmo.lab5.util.Task;

public class AddIfMinCommand extends Command {
    public AddIfMinCommand(DatabaseHandler dbHandler) {
        super(dbHandler);
        this.name = "add_if_min";
    }

    @Override
    public Task execute(Task task) {
        try {
            Ticket newTicket = task.getTicket();
            if (newTicket == null) {
                return new Task(new String[]{"false"});
            }

            Ticket minTicket = dbHandler.getMinTicketByPrice();

            if (minTicket == null || newTicket.getPrice() < minTicket.getPrice()) {
                dbHandler.addTicket(newTicket);
                return new Task(new String[]{"true"});
            } else {
                return new Task(new String[]{"false"});
            }
        } catch (Exception e) {
            return new Task(new String[]{"Error:" + e.getMessage()});
        }
    }
}
