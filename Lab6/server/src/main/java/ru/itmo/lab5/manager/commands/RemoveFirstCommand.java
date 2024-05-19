package ru.itmo.lab5.manager.commands;

import ru.itmo.lab5.manager.CollectionManager;
import ru.itmo.lab5.manager.DatabaseHandler;
import ru.itmo.lab5.util.Task;

/**
 * Команда для удаления первого элемента из коллекции.
 */
public class RemoveFirstCommand extends Command {

    public RemoveFirstCommand(CollectionManager collectionManager, DatabaseHandler dbHandler) {
        super(collectionManager, dbHandler);
        this.name = "remove_first";
        this.description = "Удаляет первый элемент из коллекции";
    }

    @Override
    public Task execute(Task task) {
        boolean removed = this.collectionManager.removeFirst();

        if (removed) {
            return new Task(new String[]{"Первый элемент коллекции успешно удален."});
        } else {
            return new Task(new String[]{"Коллекция уже пуста или не удалось удалить элемент."});
        }
    }
}
