package ru.itmo.lab5.manager.commands;

import ru.itmo.lab5.data.models.Ticket;
import ru.itmo.lab5.manager.CollectionManager;
import ru.itmo.lab5.util.Task;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Команда для вывода информации о местах проведения (Venue) всех билетов в порядке убывания их вместимости.
 * Эта команда выводит значения поля venue всех элементов коллекции, упорядоченные по убыванию вместимости.
 */
public class PrintFieldDescendingVenueCommand extends Command {
    private final CollectionManager collectionManager;

    /**
     * Конструктор для создания команды print_field_descending_venue.
     *
     * @param collectionManager Менеджер коллекции для взаимодействия с коллекцией.
     */
    public PrintFieldDescendingVenueCommand(CollectionManager collectionManager) {
        super("print_field_descending_venue", "Выводит значения поля venue всех элементов в порядке убывания", collectionManager);
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет операцию вывода информации о местах проведения всех билетов в порядке убывания вместимости.
     */
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
