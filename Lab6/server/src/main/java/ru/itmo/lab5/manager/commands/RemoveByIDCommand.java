package ru.itmo.lab5.manager.commands;

import ru.itmo.lab5.manager.CollectionManager;
import ru.itmo.lab5.manager.DatabaseHandler;
import ru.itmo.lab5.util.Task;

public class RemoveByIDCommand extends Command {

    public RemoveByIDCommand(CollectionManager collectionManager, DatabaseHandler dbHandler) {
        super(collectionManager, dbHandler);
        this.name = "remove_by_id <id>";
        this.description = "Удаляет элемент из коллекции по его ID";
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
