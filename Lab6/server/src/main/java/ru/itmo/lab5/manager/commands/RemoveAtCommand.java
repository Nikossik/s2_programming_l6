package ru.itmo.lab5.manager.commands;

import ru.itmo.lab5.manager.DatabaseHandler;
import ru.itmo.lab5.util.Task;

/**
 * Команда для удаления элемента коллекции по указанному индексу.
 */
public class RemoveAtCommand extends Command {

    public RemoveAtCommand(DatabaseHandler dbHandler) {
        super(dbHandler);
        this.name = "remove_at <index>";
    }

    @Override
    public Task execute(Task task) {
        if (task.getDescribe().length < 2 || task.getDescribe()[1].isEmpty()) {
            return new Task(new String[]{"Необходимо указать индекс элемента для удаления. использование: remove_at <index>"});
        }

        int index;
        try {
            index = Integer.parseInt(task.getDescribe()[1]);
        } catch (NumberFormatException e) {
            return new Task(new String[]{"ндекс должен быть целым числом. Передано неверное значение: " + task.getDescribe()[1]});
        }

        try {
            boolean removed = dbHandler.removeTicketAt(index);
            if (removed) {
                return new Task(new String[]{"Элемент с индексом " + index + " был успешно удален из коллекции."});
            } else {
                return new Task(new String[]{"Элемент с таким индексом не найден."});
            }
        } catch (Exception e) {
            return new Task(new String[]{"Ошибка при удалении элемента: " + e.getMessage()});
        }
    }
}
