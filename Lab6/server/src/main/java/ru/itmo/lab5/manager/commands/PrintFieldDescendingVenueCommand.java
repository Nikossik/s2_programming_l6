package ru.itmo.lab5.manager.commands;

import ru.itmo.lab5.manager.DatabaseHandler;
import ru.itmo.lab5.data.models.Venue;
import ru.itmo.lab5.util.Task;

import java.util.List;

/**
 * Команда для вывода информации о местах проведения (Venue) всех билетов в порядке убывания их вместимости.
 * Эта команда выводит значения поля venue всех элементов коллекции, упорядоченные по убыванию вместимости.
 */
public class PrintFieldDescendingVenueCommand extends Command {
    private final DatabaseHandler dbHandler;

    /**
     * Конструктор для создания команды print_field_descending_venue.
     *
     * @param dbHandler Обработчик базы данных для взаимодействия с БД.
     */
    public PrintFieldDescendingVenueCommand(DatabaseHandler dbHandler) {
        super("print_field_descending_venue", "Выводит значения поля venue всех элементов в порядке убывания", dbHandler);
        this.dbHandler = dbHandler;
    }

    /**
     * Выполняет операцию вывода информации о местах проведения всех билетов в порядке убывания вместимости.
     * спользует метод DatabaseHandler для получения данных из базы и выводит их.
     */
    @Override
    public Task execute(Task task, DatabaseHandler dbHandler) {
        try {
            List<Venue> venues = this.dbHandler.getVenuesInDescendingOrderByCapacity();
            if (venues.isEmpty()) {
                return new Task(new String[]{"В коллекции нет элементов с заданным полем venue или коллекция пуста."});
            } else {
                StringBuilder answer = new StringBuilder("Значения поля venue всех элементов в порядке убывания:\n");
                venues.forEach(venue -> answer.append(venue).append("\n"));
                return new Task(new String[]{answer.toString()});
            }
        } catch (Exception e) {
            return new Task(new String[]{"Ошибка при получении данных о местах проведения: " + e.getMessage()});
        }
    }
}
