package ru.itmo.lab5.manager.commands;

import ru.itmo.lab5.manager.DatabaseHandler;
import ru.itmo.lab5.util.Task;

/**
 * Команда для удаления первого элемента из коллекции.
 */
public class RemoveFirstCommand extends Command {
    private final DatabaseHandler dbHandler;

    /**
     * Конструктор команды remove_first.
     *
     * @param dbHandler Обработчик базы данных для взаимодействия с БД.
     */
    public RemoveFirstCommand(DatabaseHandler dbHandler) {
        super("remove_first", "Удаляет первый элемент из коллекции", dbHandler);
        this.dbHandler = dbHandler;
    }

    @Override
    public Task execute(Task task, DatabaseHandler dbHandler) {
        boolean removed = this.dbHandler.removeFirst();

        if (removed) {
            return new Task(new String[]{"Первый элемент коллекции успешно удален."});
        } else {
            return new Task(new String[]{"Коллекция уже пуста или не удалось удалить элемент."});
        }
    }
}
