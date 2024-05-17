package ru.itmo.lab5.manager.commands;

import ru.itmo.lab5.manager.DatabaseHandler;
import ru.itmo.lab5.data.models.Ticket;
import ru.itmo.lab5.util.Task;

/**
 * Команда для добавления нового билета в базу данных.
 * спользует данные из задачи для создания и добавления билета.
 */
public class AddCommand extends Command {

    /**
     * Конструктор для команды добавления билета.
     *
     * @param dbHandler Обработчик базы данных для взаимодействия с БД.
     */
    public AddCommand(DatabaseHandler dbHandler) {
        super("add", "Добавляет новый билет в коллекцию", dbHandler);
    }

    @Override
    public Task execute(Task task, DatabaseHandler dbHandler) {
        try {
            Ticket ticket = task.ticket;
            if (ticket.validate()) {
                Ticket addedTicket = DatabaseHandler.createTicket(ticket, task.username);
                if (addedTicket != null) {
                    return new Task(new String[]{"Билет успешно добавлен."});
                } else {
                    return new Task(new String[]{"Ошибка при добавлении билета в базу данных."});
                }
            } else {
                return new Task(new String[]{"Невозможно добавить билет: некорректные данные."});
            }
        } catch (Exception e) {
            return new Task(new String[]{"Ошибка при добавлении билета: " + e.getMessage()});
        }
    }
}
