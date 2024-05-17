package ru.itmo.lab5.manager.commands;

import ru.itmo.lab5.manager.DatabaseHandler;
import ru.itmo.lab5.util.Task;

/**
 * Команда для удаления элемента коллекции по указанному индексу.
 */
public class RemoveAtCommand extends Command {
    private final DatabaseHandler dbHandler;

    /**
     * Конструктор команды remove_at.
     *
     * @param dbHandler Обработчик базы данных для взаимодействия с БД.
     */
    public RemoveAtCommand(DatabaseHandler dbHandler) {
        super("remove_at <index>", "Удаляет элемент из коллекции по его индексу", dbHandler);
        this.dbHandler = dbHandler;
    }

    @Override
    public Task execute(Task task, DatabaseHandler dbHandler) {
        if (task.describe.length < 2 || task.describe[1].isEmpty()) {
            return new Task(new String[]{"Необходимо указать индекс элемента для удаления. использование: remove_at <index>"});
        }

        int index;
        try {
            index = Integer.parseInt(task.describe[1]);
        } catch (NumberFormatException e) {
            return new Task(new String[]{"ндекс должен быть целым числом. Передано неверное значение: " + task.describe[1]});
        }

        try {
            if (this.dbHandler.removeAt(index)) {
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
