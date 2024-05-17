package ru.itmo.lab5.manager.commands;

import ru.itmo.lab5.manager.DatabaseHandler;
import ru.itmo.lab5.util.Task;

/**
 * Команда для удаления элемента из коллекции по его ID.
 */
public class RemoveByIDCommand extends Command {
    private final DatabaseHandler dbHandler;

    /**
     * Конструктор команды remove_by_id.
     *
     * @param dbHandler Обработчик базы данных для взаимодействия с БД.
     */
    public RemoveByIDCommand(DatabaseHandler dbHandler) {
        super("remove_by_id <id>", "Удаляет элемент из коллекции по его ID", dbHandler);
        this.dbHandler = dbHandler;
    }

    @Override
    public Task execute(Task task, DatabaseHandler dbHandler) {
        if (task.describe.length < 2 || task.describe[1].isEmpty()) {
            return new Task(new String[]{"использование: '" + getName() + " <id>'"});
        }
        long id;
        try {
            id = Long.parseLong(task.describe[1]);
        } catch (NumberFormatException e) {
            return new Task(new String[]{"ID должен быть числом. Передано неверное значение: " + task.describe[1]});
        }

        boolean removed = this.dbHandler.removeById(id);

        if (removed) {
            return new Task(new String[]{"Элемент с ID " + id + " был успешно удален из коллекции."});
        } else {
            return new Task(new String[]{"Элемент с таким ID не найден."});
        }
    }
}
