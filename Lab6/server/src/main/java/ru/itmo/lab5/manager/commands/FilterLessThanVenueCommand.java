package ru.itmo.lab5.manager.commands;

import ru.itmo.lab5.data.models.Ticket;
import ru.itmo.lab5.manager.CollectionManager;
import ru.itmo.lab5.util.Task;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Команда для вывода элементов коллекции, чьи места проведения имеют вместимость меньше заданной.
 */
public class FilterLessThanVenueCommand extends Command {
    private final CollectionManager collectionManager;

    /**
     * Конструктор команды filter_less_than_venue.
     *
     * @param collectionManager Менеджер коллекции для взаимодействия с коллекцией.
     */
    public FilterLessThanVenueCommand(CollectionManager collectionManager) {
        super("filter_less_than_venue <venue_capacity>", "Выводит элементы, значение поля venue которых меньше заданного", collectionManager);
        this.collectionManager = collectionManager;
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
