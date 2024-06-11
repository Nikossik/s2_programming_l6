package ru.itmo.lab5.manager.commands;

import ru.itmo.lab5.data.models.Ticket;
import ru.itmo.lab5.manager.DatabaseHandler;
import ru.itmo.lab5.util.Task;

import java.util.List;

public class PrintDescendingCommand extends Command {

    public PrintDescendingCommand(DatabaseHandler dbHandler) {
        super(dbHandler);
        this.name = "print_descending";
        this.description = "Displays the elements of the collection in descending order";
    }

    @Override
    public Task execute(Task task) {
        List<Ticket> sortedTickets = dbHandler.getTicketsSortedByPriceDescending();

        if (sortedTickets.isEmpty()) {
            return new Task(new String[]{"The collection is empty."});
        } else {
            StringBuilder answer = new StringBuilder("The collection is displayed in descending order:\n");
            sortedTickets.forEach(ticket -> answer.append(ticket).append("\n"));
            return new Task(new String[]{answer.toString()});
        }
    }
}
