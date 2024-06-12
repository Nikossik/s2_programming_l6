package ru.itmo.lab5.manager.commands;

import ru.itmo.lab5.data.models.Ticket;
import ru.itmo.lab5.manager.DatabaseHandler;
import ru.itmo.lab5.util.Task;

import java.util.List;

public class FilterLessThanVenueCommand extends Command {

    public FilterLessThanVenueCommand(DatabaseHandler dbHandler) {
        super(dbHandler);
        this.name = "filter_less_than_venue <venue>";
    }

    @Override
    public Task execute(Task task) {
        if (task.getDescribe().length < 2 || task.getDescribe()[1].isEmpty()) {
            return new Task(new String[]{"false"});
        }

        int venueCapacity;
        try {
            venueCapacity = Integer.parseInt(task.getDescribe()[1]);
        } catch (NumberFormatException e) {
            return new Task(new String[]{"false"});
        }

        List<Ticket> filteredTickets = dbHandler.getTicketsWithVenueLessThan(venueCapacity);

        if (filteredTickets.isEmpty()) {
            return new Task(new String[]{"false " + venueCapacity});
        } else {
            StringBuilder answer = new StringBuilder("Sorted collection items:\n");
            filteredTickets.forEach(ticket -> answer.append(ticket).append("\n"));
            return new Task(new String[]{answer.toString()});
        }
    }
}
