package ru.itmo.lab5.manager.commands;

import ru.itmo.lab5.manager.CollectionManager;
import ru.itmo.lab5.util.Task;

/**
 * Команда для очистки коллекции билетов.
 * Удаляет все элементы из коллекции.
 */
public class ClearCommand extends Command {

    private final CollectionManager collectionManager;

    /**
     * Конструктор для команды очистки коллекции.
     *
     * @param collectionManager Менеджер коллекции для взаимодействия с коллекцией.
     */
    public ClearCommand(CollectionManager collectionManager) {
        super("clear", "Очищает коллекцию", collectionManager);
        this.collectionManager = collectionManager;
    }

    @Override
    public Task execute(Task task) {
        collectionManager.clear();
        return new Task(new String[]{"Коллекция успешно очищена."});
    }
}
