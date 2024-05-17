package ru.itmo.lab5.manager.commands;

import ru.itmo.lab5.manager.DatabaseHandler;
import ru.itmo.lab5.util.Task;

/**
 * Команда для очистки коллекции билетов в базе данных.
 * Удаляет все элементы из базы данных.
 */
public class ClearCommand extends Command {
    private final DatabaseHandler dbHandler;

    /**
     * Конструктор для команды очистки коллекции.
     *
     * @param dbHandler Обработчик базы данных для взаимодействия с БД.
     */
    public ClearCommand(DatabaseHandler dbHandler) {
        super("clear", "Очищает коллекцию", dbHandler);
        this.dbHandler = dbHandler;
    }

    @Override
    public Task execute(Task task, DatabaseHandler dbHandler) {
        try {
            boolean isCleared = this.dbHandler.clearTickets();
            if (isCleared) {
                return new Task(new String[]{"Коллекция успешно очищена."});
            } else {
                return new Task(new String[]{"Не удалось очистить коллекцию."});
            }
        } catch (Exception e) {
            return new Task(new String[]{"Ошибка при очистке коллекции: " + e.getMessage()});
        }
    }
}
