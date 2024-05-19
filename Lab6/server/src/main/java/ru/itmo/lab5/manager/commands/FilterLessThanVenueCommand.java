package ru.itmo.lab5.manager.commands;

import ru.itmo.lab5.data.models.Ticket;
import ru.itmo.lab5.manager.CollectionManager;
import ru.itmo.lab5.manager.DatabaseHandler;
import ru.itmo.lab5.util.Task;

import java.util.Comparator;
import java.util.List;

public class FilterLessThanVenueCommand extends Command {

    public FilterLessThanVenueCommand(CollectionManager collectionManager, DatabaseHandler dbHandler) {
        super(collectionManager, dbHandler);
        this.name = "filter_less_than_venue <venue>";
        this.description = "Outputs elements whose venue field value is less than the specified value.";
    }

    @Override
    public Task execute(Task task) {
        if (task.getDescribe()[0].length() < 2 || task.getDescribe()[1].isEmpty()) {
            return new Task(new String[]{"You must specify a value for comparison. usage: '" + task.getTicket().getName() + "'"});
        }

        int venueCapacity;
        try {
            venueCapacity = Integer.parseInt(task.getDescribe()[1]);
        } catch (NumberFormatException e) {
            return new Task(new String[]{"The value must be an integer. Invalid value passed: " + task.getDescribe()[1]});
        }

        List<Ticket> filteredTickets = collectionManager.getTickets().stream()
                .filter(ticket -> ticket.getVenue().getCapacity() < venueCapacity)
                .sorted(Comparator.comparingInt(ticket -> ticket.getVenue().getCapacity()))
                .toList();

        if (filteredTickets.isEmpty()) {
            return new Task(new String[]{"Нет билетов с вместимостью места проведения меньше " + venueCapacity});
        } else {
            StringBuilder answer = new StringBuilder("Sorted collection items:\n");
            filteredTickets.forEach(ticket -> answer.append(ticket).append("\n"));
            return new Task(new String[]{answer.toString()});
        }
    }
}
