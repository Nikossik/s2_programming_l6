package ru.itmo.lab5.manager.commands;

import ru.itmo.lab5.manager.CollectionManager;
import ru.itmo.lab5.util.Task;

/**
 * Команда для удаления первого элемента из коллекции.
 */
public class RemoveFirstCommand extends Command {
    private final CollectionManager collectionManager;

    /**
     * Конструктор команды remove_first.
     *
     * @param collectionManager Менеджер коллекции для взаимодействия с коллекцией.
     */
    public RemoveFirstCommand(CollectionManager collectionManager) {
        super("remove_first", "Удаляет первый элемент из коллекции", collectionManager);
        this.collectionManager = collectionManager;
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
