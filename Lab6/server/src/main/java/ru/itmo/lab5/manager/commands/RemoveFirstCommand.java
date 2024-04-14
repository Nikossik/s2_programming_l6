package ru.itmo.lab5.manager.commands;

import ru.itmo.lab5.data.TicketCollection;
import ru.itmo.lab5.util.Task;

/**
 * Команда для удаления первого элемента из коллекции.
 */
public class RemoveFirstCommand extends Command {
    /**
     * Конструктор команды remove_first.
     *
     * @param ticketCollection Коллекция билетов, с которой работает команда.
     */
    public RemoveFirstCommand(TicketCollection ticketCollection) {
        super("remove_first", "Удаляет первый элемент из коллекции", ticketCollection);
    }

    @Override
    public Task execute(Task task) {
        if (ticketCollection.getTickets().isEmpty()) {
            return new Task(new String[]{"Коллекция уже пуста."});
        } else {
            ticketCollection.getTickets().remove(0);
            return new Task(new String[]{"Первый элемент коллекции успешно удален."});
        }
    }
}
