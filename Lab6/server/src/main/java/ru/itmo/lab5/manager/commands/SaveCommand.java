package ru.itmo.lab5.manager.commands;

import ru.itmo.lab5.data.models.Ticket;
import ru.itmo.lab5.manager.DatabaseHandler;
import ru.itmo.lab5.util.Task;

import java.util.List;

/**
 * Команда для сохранения текущего состояния коллекции в базу данных.
 */
@SuppressWarnings("unchecked")
public class SaveCommand extends Command {
    private final DatabaseHandler dbHandler;

    /**
     * Конструктор команды save.
     *
     * @param dbHandler Обработчик базы данных для взаимодействия с БД.
     */
    public SaveCommand( DatabaseHandler dbHandler) {
        super("save", "Сохраняет коллекцию в базу данных", dbHandler);
        this.dbHandler = dbHandler;
    }

    @Override
    public Task execute(Task task, DatabaseHandler dbHandler) {
        try {
            boolean isSaved = this.dbHandler.saveCollectionToDatabase((List<Ticket>) task.ticket);
            if (isSaved) {
                return new Task(new String[]{"Коллекция сохранена в базу данных"});
            } else {
                return new Task(new String[]{"Ошибка при сохранении коллекции в базу данных"});
            }
        } catch (Exception e) {
            return new Task(new String[]{"Ошибка при сохранении коллекции: " + e.getMessage()});
        }
    }
}
