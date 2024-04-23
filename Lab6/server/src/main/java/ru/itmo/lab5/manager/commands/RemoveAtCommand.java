package ru.itmo.lab5.manager.commands;

import ru.itmo.lab5.data.TicketCollection;
import ru.itmo.lab5.util.Task;

/**
 * Команда для удаления элемента коллекции по указанному индексу.
 */
public class RemoveAtCommand extends Command {
    /**
     * Конструктор команды remove_at.
     *
     * @param ticketCollection Коллекция билетов, с которой работает команда.
     */
    public RemoveAtCommand(TicketCollection ticketCollection) {
        super("remove_at <index>", "Удаляет элемент из коллекции по его индексу", ticketCollection);
    }

    @Override
    public Task execute(Task task) {
        if (task.describe.length < 2 || task.describe[1].isEmpty()) {
            return new Task(new String[]{"Необходимо указать индекс элемента для удаления. использование: remove_at <index>"});
        }

        int index;
        try {
            index = Integer.parseInt(task.describe[1]);
        } catch (NumberFormatException e) {
            return new Task(new String[]{"индекс должен быть целым числом. Передано неверное значение: " + task.describe[1]});
        }

        try {
            if (ticketCollection.removeAt(index)) {
                return new Task(new String[]{"Элемент с индексом " + index + " был успешно удален из коллекции."});
            } else {
                return new Task(new String[]{"Элемент с таким индексом не найден."});
            }
        } catch (IndexOutOfBoundsException e) {
            return new Task(new String[]{"индекс выходит за пределы коллекции. Передано неверное значение: " + index});
        }
    }
}
