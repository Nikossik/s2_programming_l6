package ru.itmo.lab5.manager.commands;

import ru.itmo.lab5.manager.DatabaseHandler;
import ru.itmo.lab5.util.Task;

/**
 * Команда для удаления первого элемента из коллекции.
 */
public class RemoveFirstCommand extends Command {

    public RemoveFirstCommand(DatabaseHandler dbHandler) {
        super(dbHandler);
        this.name = "remove_first";
    }

    @Override
    public Task execute(Task task) {
        try {
            boolean removed = dbHandler.removeFirstTicket();
            if (removed) {
                return new Task(new String[]{"Первый элемент коллекции успешно удален."});
            } else {
                return new Task(new String[]{"Коллекция уже пуста или не удалось удалить элемент."});
            }
        } catch (Exception e) {
            return new Task(new String[]{"Ошибка при удалении элемента: " + e.getMessage()});
        }
    }
}
