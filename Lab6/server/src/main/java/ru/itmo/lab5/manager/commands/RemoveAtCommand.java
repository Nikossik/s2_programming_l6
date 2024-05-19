package ru.itmo.lab5.manager.commands;

import ru.itmo.lab5.manager.CollectionManager;
import ru.itmo.lab5.manager.DatabaseHandler;
import ru.itmo.lab5.util.Task;

/**
 * Команда для удаления элемента коллекции по указанному индексу.
 */
public class RemoveAtCommand extends Command {

    public RemoveAtCommand(CollectionManager collectionManager, DatabaseHandler dbHandler) {
        super(collectionManager, dbHandler);
        this.name = "remove_at <index>";
        this.description = "Удаляет элемент из коллекции по его индексу";
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
            if (this.collectionManager.removeAt(index)) {
                return new Task(new String[]{"Элемент с индексом " + index + " был успешно удален из коллекции."});
            } else {
                return new Task(new String[]{"Элемент с таким индексом не найден."});
            }
        } catch (IndexOutOfBoundsException e) {
            return new Task(new String[]{"ндекс выходит за пределы коллекции. Передано неверное значение: " + index});
        } catch (Exception e) {
            return new Task(new String[]{"Ошибка при удалении элемента: " + e.getMessage()});
        }
    }
}
