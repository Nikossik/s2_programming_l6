package ru.itmo.lab5.manager.commands;

import ru.itmo.lab5.manager.DatabaseHandler;
import ru.itmo.lab5.data.models.Ticket;
import ru.itmo.lab5.util.Task;

import java.util.List;

/**
 * Команда для вывода всех элементов коллекции.
 */
public class ShowCommand extends Command {
    private final DatabaseHandler dbHandler;

    /**
     * Конструктор команды show.
     *
     * @param dbHandler Обработчик базы данных для взаимодействия с БД.
     */
    public ShowCommand(DatabaseHandler dbHandler) {
        super("show", "Выводит все элементы коллекции", dbHandler);
        this.dbHandler = dbHandler;
    }

    @Override
    public Task execute(Task task, DatabaseHandler dbHandler) {
        try {
            List<Ticket> tickets = this.dbHandler.loadAllTickets();
            if (tickets.isEmpty()) {
                return new Task(new String[]{"Коллекция пуста."});
            } else {
                StringBuilder answer = new StringBuilder();
                answer.append("Элементы коллекции:\n");
                for (Ticket ticket : tickets) {
                    answer.append(ticket).append("\n");
                }
                return new Task(new String[]{answer.toString()});
            }
        } catch (Exception e) {
            return new Task(new String[]{"Ошибка при получении билетов: " + e.getMessage()});
        }
    }
}
