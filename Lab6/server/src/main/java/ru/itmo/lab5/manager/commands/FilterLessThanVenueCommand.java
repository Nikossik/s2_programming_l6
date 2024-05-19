package ru.itmo.lab5.manager.commands;

import ru.itmo.lab5.data.models.Ticket;
import ru.itmo.lab5.manager.CollectionManager;
import ru.itmo.lab5.manager.DatabaseHandler;
import ru.itmo.lab5.util.Task;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Команда для вывода элементов коллекции, чьи места проведения имеют вместимость меньше заданной.
 */
public class FilterLessThanVenueCommand extends Command {

    public FilterLessThanVenueCommand(CollectionManager collectionManager, DatabaseHandler dbHandler) {
        super(collectionManager, dbHandler);
        this.name = "filter_less_than_venue <venue>";
        this.description = "Выводит элементы, значение поля venue которых меньше заданного";
    }

    @Override
    public Task execute(Task task) {
        if (task.describe.length < 2 || task.describe[1].isEmpty()) {
            return new Task(new String[]{"Необходимо указать значение для сравнения. использование: '" + task.getTicket().getName() + "'"});
        }

        int venueCapacity;
        try {
            venueCapacity = Integer.parseInt(task.describe[1]);
        } catch (NumberFormatException e) {
            return new Task(new String[]{"Значение должно быть целым числом. Передано неверное значение: " + task.describe[1]});
        }

        List<Ticket> filteredTickets = collectionManager.getTickets().stream()
                .filter(ticket -> ticket.getVenue().getCapacity() < venueCapacity)
                .sorted(Comparator.comparingInt(ticket -> ticket.getVenue().getCapacity()))
                .collect(Collectors.toList());

        if (filteredTickets.isEmpty()) {
            return new Task(new String[]{"Нет билетов с вместимостью места проведения меньше " + venueCapacity});
        } else {
            StringBuilder answer = new StringBuilder("Отсортированные элементы коллекции:\n");
            filteredTickets.forEach(ticket -> answer.append(ticket).append("\n"));
            return new Task(new String[]{answer.toString()});
        }
    }
}
