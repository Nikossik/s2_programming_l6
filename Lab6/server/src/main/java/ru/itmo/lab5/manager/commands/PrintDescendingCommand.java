package ru.itmo.lab5.manager.commands;

import ru.itmo.lab5.manager.DatabaseHandler;
import ru.itmo.lab5.data.models.Ticket;
import ru.itmo.lab5.util.Task;

import java.util.List;

/**
 * Команда для вывода элементов коллекции в порядке убывания цены.
 */
public class PrintDescendingCommand extends Command {
    private final DatabaseHandler dbHandler;

    /**
     * Конструктор команды print_descending.
     *
     * @param dbHandler Обработчик базы данных для взаимодействия с БД.
     */
    public PrintDescendingCommand(DatabaseHandler dbHandler) {
        super("print_descending", "Выводит элементы коллекции в порядке убывания", dbHandler);
        this.dbHandler = dbHandler;
    }

    @Override
    public Task execute(Task task, DatabaseHandler dbHandler) {
        try {
            List<Ticket> tickets = this.dbHandler.getTicketsInDescendingOrder();
            if (tickets.isEmpty()) {
                return new Task(new String[]{"Коллекция пуста."});
            } else {
                StringBuilder answer = new StringBuilder();
                answer.append("Коллекция выведена в порядке убывания:\n");
                tickets.forEach(ticket -> answer.append(ticket).append("\n"));
                return new Task(new String[]{answer.toString()});
            }
        } catch (Exception e) {
            return new Task(new String[]{"Ошибка при получении билетов: " + e.getMessage()});
        }
    }
}
