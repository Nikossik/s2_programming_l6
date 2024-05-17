package ru.itmo.lab5.manager.commands;

import ru.itmo.lab5.data.models.Ticket;
import ru.itmo.lab5.manager.DatabaseHandler;
import ru.itmo.lab5.util.Task;

import java.util.Comparator;
import java.util.List;

/**
 * Команда для добавления нового билета в базу данных, если его цена меньше минимальной в базе данных.
 * Запрашивает у пользователя данные для создания билета и добавляет его в базу данных при условии.
 */
public class AddIfMinCommand extends Command {
    private final DatabaseHandler dbHandler;

    /**
     * Конструктор для команды добавления билета, если его цена меньше минимальной.
     *
     * @param dbHandler Обработчик базы данных для взаимодействия с БД.
     */
    public AddIfMinCommand(DatabaseHandler dbHandler) {
        super("add_if_min", "Добавляет новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции", dbHandler);
        this.dbHandler = dbHandler;
    }

    @Override
    public Task execute(Task task, DatabaseHandler dbHandler) {
        Ticket newTicket = task.ticket;
        if (newTicket == null) {
            return new Task(new String[]{"Не удалось создать билет. Отмена операции."});
        }

        try {
            List<Ticket> allTickets = this.dbHandler.loadAllTickets();
            Ticket minTicket = allTickets.stream()
                    .min(Comparator.comparing(Ticket::getPrice))
                    .orElse(null);

            if (minTicket == null || newTicket.getPrice() < minTicket.getPrice()) {
                Ticket addedTicket = this.dbHandler.createTicket(newTicket, task.username);
                if (addedTicket != null) {
                    return new Task(new String[]{"Новый билет успешно добавлен в базу данных."});
                } else {
                    return new Task(new String[]{"Ошибка при добавлении билета в базу данных."});
                }
            } else {
                return new Task(new String[]{"Новый билет не является минимальным. Операция добавления не выполнена."});
            }
        } catch (Exception e) {
            return new Task(new String[]{"Ошибка при добавлении билета: " + e.getMessage()});
        }
    }
}
