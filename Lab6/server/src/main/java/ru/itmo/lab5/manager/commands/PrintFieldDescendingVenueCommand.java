package ru.itmo.lab5.manager.commands;

import ru.itmo.lab5.data.models.Ticket;
import ru.itmo.lab5.manager.CollectionManager;
import ru.itmo.lab5.manager.DatabaseHandler;
import ru.itmo.lab5.util.Task;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class PrintFieldDescendingVenueCommand extends Command {


    public PrintFieldDescendingVenueCommand(CollectionManager collectionManager, DatabaseHandler dbHandler) {
        super(collectionManager, dbHandler);
        this.name = "print_field_descending_venue";
        this.description = "Выводит значения поля venue всех элементов в порядке убывания";
    }

    @Override
    public Task execute(Task task) {
        List<Ticket> tickets = collectionManager.getTickets().stream()
                .filter(ticket -> ticket.getVenue() != null)
                .sorted(Comparator.comparingInt(ticket -> -ticket.getVenue().getCapacity()))
                .collect(Collectors.toList());

        if (tickets.isEmpty()) {
            return new Task(new String[]{"В коллекции нет элементов с заданным полем venue или коллекция пуста."});
        } else {
            StringBuilder answer = new StringBuilder("Значения поля venue всех элементов в порядке убывания:\n");
            tickets.forEach(ticket -> answer.append(ticket.getVenue()).append("\n"));
            return new Task(new String[]{answer.toString()});
        }
    }
}
