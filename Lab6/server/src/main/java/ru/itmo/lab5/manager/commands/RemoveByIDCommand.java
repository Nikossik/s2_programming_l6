package ru.itmo.lab5.manager.commands;

import ru.itmo.lab5.data.TicketCollection;
import ru.itmo.lab5.util.Task;

/**
 * Команда для удаления элемента из коллекции по его ID.
 */
public class RemoveByIDCommand extends Command {
    /**
     * Конструктор команды remove_by_id.
     *
     * @param ticketCollection Коллекция билетов, с которой работает команда.
     */
    public RemoveByIDCommand(TicketCollection ticketCollection) {
        super("remove_by_id <id>", "Удаляет первый элемент из коллекции", ticketCollection);
    }

    @Override
    public Task execute(Task task) {
        if (task.describe[1].isEmpty()) {
            return new Task(new String[]{"спользование: '" + getName() + "'"});
        }
        long id;
        try {
            id = Long.parseLong(task.describe[1]);
        } catch (NumberFormatException e) {
            return new Task(new String[]{"ID должен быть числом. Передано неверное значение: " + task.describe[1]});
        }

        boolean removed = ticketCollection.remove(id);

        if (removed) {
            return new Task(new String[]{"Элемент с ID " + id + " был успешно удален из коллекции."});
        } else {
            return new Task(new String[]{"Элемент с таким ID не найден."});
        }
    }
}

