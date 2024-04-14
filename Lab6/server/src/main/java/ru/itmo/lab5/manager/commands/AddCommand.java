package ru.itmo.lab5.manager.commands;

import ru.itmo.lab5.data.TicketCollection;
import ru.itmo.lab5.data.models.Ticket;
import ru.itmo.lab5.util.Task;

/**
 * Команда для добавления нового билета в коллекцию.
 * Запрашивает у пользователя данные для создания билета и добавляет его в коллекцию.
 */
public class AddCommand extends Command {
    /**
     * Конструктор для команды добавления билета.
     *
     * @param ticketCollection Коллекция, в которую будет добавлен билет.
     */
    public AddCommand(TicketCollection ticketCollection) {
        super("add", "Добавляет новый билет в коллекцию", ticketCollection);
    }

    @Override
    public Task execute(Task task) {
        try {
            Ticket ticket = task.ticket;
            if (ticket.validate()) {
                ticketCollection.add(ticket);
                return new Task(new String[]{"Билет успешно добавлен."});
            } else {
                new Task(new String[]{"Невозможно добавить билет: некорректные данные."});
            }
        } catch (Exception e) {
            new Task(new String[]{"Ошибка при добавлении билета: " + e.getMessage()});
        }
        return new Task(new String[]{"Ошибка"});
    }
}
