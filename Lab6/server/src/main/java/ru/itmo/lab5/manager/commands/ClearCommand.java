package ru.itmo.lab5.manager.commands;

import ru.itmo.lab5.data.TicketCollection;
import ru.itmo.lab5.util.Task;

/**
 * Команда для очистки коллекции билетов.
 * Удаляет все элементы из коллекции.
 */
public class ClearCommand extends Command {
    /**
     * Конструктор для команды очистки коллекции.
     *
     * @param ticketCollection Коллекция, которая будет очищена.
     */
    public ClearCommand(TicketCollection ticketCollection) {
        super("clear", "Очищает коллекцию", ticketCollection);
    }
    @Override
    public Task execute(Task task) {
        if (ticketCollection != null && !ticketCollection.getTickets().isEmpty()) {
            ticketCollection.getTickets().clear();
            new Task(new String[]{"Коллекция успешно очищена."});
        } else {
            return new Task(new String[]{"Коллекция уже пуста или не инициализирована."});
        }
        return new Task(new String[]{"Ошибка"});
    }
}
