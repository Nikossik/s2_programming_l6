package ru.itmo.lab5.manager.commands;

import ru.itmo.lab5.manager.DatabaseHandler;
import ru.itmo.lab5.data.models.Ticket;
import ru.itmo.lab5.util.Task;

import java.util.List;

/**
 * Команда для вывода элементов коллекции, чьи места проведения имеют вместимость меньше заданной.
 */
public class FilterLessThanVenueCommand extends Command {
    private final DatabaseHandler dbHandler;

    /**
     * Конструктор команды filter_less_than_venue.
     *
     * @param dbHandler Обработчик базы данных для взаимодействия с БД.
     */
    public FilterLessThanVenueCommand(DatabaseHandler dbHandler) {
        super("filter_less_than_venue <venue_capacity>", "Выводит элементы, значение поля venue которых меньше заданного", dbHandler);
        this.dbHandler = dbHandler;
    }

    @Override
    public Task execute(Task task, DatabaseHandler dbHandler) {
        if (task.describe.length < 2 || task.describe[1].isEmpty()) {
            return new Task(new String[]{"Необходимо указать значение для сравнения. использование: '" + getName() + "'"});
        }

        int venueCapacity;
        try {
            venueCapacity = Integer.parseInt(task.describe[1]);
        } catch (NumberFormatException e) {
            return new Task(new String[]{"Значение должно быть целым числом. Передано неверное значение: " + task.describe[1]});
        }

        try {
            List<Ticket> tickets = this.dbHandler.filterTicketsByVenueCapacity(venueCapacity);
            StringBuilder answer = new StringBuilder("Отсортированные элементы коллекции:\n");
            tickets.forEach(ticket -> answer.append(ticket).append("\n"));
            return new Task(new String[]{answer.toString()});
        } catch (Exception e) {
            return new Task(new String[]{"Ошибка при фильтрации билетов: " + e.getMessage()});
        }
    }
}
