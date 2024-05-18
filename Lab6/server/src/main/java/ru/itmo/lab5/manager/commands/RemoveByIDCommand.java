package ru.itmo.lab5.manager.commands;

import ru.itmo.lab5.manager.CollectionManager;
import ru.itmo.lab5.util.Task;

/**
 * Команда для удаления элемента из коллекции по его ID.
 */
public class RemoveByIDCommand extends Command {
    private final CollectionManager collectionManager;

    /**
     * Конструктор команды remove_by_id.
     *
     * @param collectionManager Менеджер коллекции для взаимодействия с коллекцией.
     */
    public RemoveByIDCommand(CollectionManager collectionManager) {
        super("remove_by_id <id>", "Удаляет элемент из коллекции по его ID", collectionManager);
        this.collectionManager = collectionManager;
    }

    @Override
    public Task execute(Task task) {
        if (task.getDescribe().length < 2 || task.getDescribe()[1].isEmpty()) {
            return new Task(new String[]{"спользование: '" + task.describe[0] + " <id>'"});
        }
        long id;
        try {
            id = Long.parseLong(task.getDescribe()[1]);
        } catch (NumberFormatException e) {
            return new Task(new String[]{"ID должен быть числом. Передано неверное значение: " + task.getDescribe()[1]});
        }

        boolean removed = this.collectionManager.remove(id);

        if (removed) {
            return new Task(new String[]{"Элемент с ID " + id + " был успешно удален из коллекции."});
        } else {
            return new Task(new String[]{"Элемент с таким ID не найден."});
        }
    }
}
