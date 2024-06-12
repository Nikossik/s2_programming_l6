package ru.itmo.lab5.manager.commands;

import ru.itmo.lab5.data.models.Ticket;
import ru.itmo.lab5.manager.DatabaseHandler;
import ru.itmo.lab5.util.Task;

import java.util.List;

public class PrintFieldDescendingVenueCommand extends Command {

    public PrintFieldDescendingVenueCommand(DatabaseHandler dbHandler) {
        super(dbHandler);
        this.name = "print_field_descending_venue";
    }

    @Override
    public Task execute(Task task) {
        List<Ticket> tickets = dbHandler.getTicketsSortedByVenueCapacityDescending();

        if (tickets.isEmpty()) {
            return new Task(new String[]{"There are no items in the collection with the specified venue field or the collection is empty."});
        } else {
            StringBuilder answer = new StringBuilder("The values of the venue field of all elements are in descending order:\n");
            tickets.forEach(ticket -> answer.append(ticket.getVenue()).append("\n"));
            return new Task(new String[]{answer.toString()});
        }
    }
}
