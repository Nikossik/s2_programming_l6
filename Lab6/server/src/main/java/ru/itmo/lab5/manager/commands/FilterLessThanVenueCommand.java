package ru.itmo.lab5.manager.commands;

import ru.itmo.lab5.data.TicketCollection;
import ru.itmo.lab5.util.Task;

/**
 * Команда для вывода элементов коллекции, чьи места проведения имеют вместимость меньше заданной.
 */
public class FilterLessThanVenueCommand extends Command {
    /**
     * Конструктор команды filter_less_than_venue.
     *
     * @param ticketCollection Коллекция билетов, с которой работает команда.
     */
    public FilterLessThanVenueCommand(TicketCollection ticketCollection) {
        super("filter_less_than_venue <venue_capacity>", "Выводит элементы, значение поля venue которых меньше заданного", ticketCollection);
    }

    @Override
    public Task execute(Task task) {
        if (task.describe.length < 2 || task.describe[1].isEmpty()) {
            new Task(new String[]{"Необходимо указать значение для сравнения. спользование: '" + getName() + "'"});
        }

        int venueCapacity;
        try {
            venueCapacity = Integer.parseInt(task.describe[1]);
        } catch (NumberFormatException e) {
            return new Task(new String[]{"Значение должно быть целым числом. Передано неверное значение: " + task.describe[1]});
        }

        ticketCollection.getTickets().stream()
                .filter(ticket -> ticket.getVenue() != null && ticket.getVenue().getCapacity() < venueCapacity)
                .forEach(ticket -> System.out.println(ticket));
        return new Task(new String[]{"Ошибка"});
    }
}
