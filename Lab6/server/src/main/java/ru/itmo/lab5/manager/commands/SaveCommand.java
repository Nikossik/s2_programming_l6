package ru.itmo.lab5.manager.commands;

import ru.itmo.lab5.manager.CollectionManager;
import ru.itmo.lab5.manager.DatabaseHandler;
import ru.itmo.lab5.util.Task;

/**
 * Команда для сохранения коллекции в базе данных.
 */
public class SaveCommand extends Command {

    public SaveCommand(CollectionManager collectionManager, DatabaseHandler dbHandler) {
        super(collectionManager, dbHandler);
        this.name = "save";
        this.description = "Сохраняет коллекцию в базе данных";
    }

    @Override
    public Task execute(Task task) {
        try {
            if (collectionManager.getTickets().isEmpty()) {
                collectionManager.clearAllInDatabase();
                return new Task(new String[]{"Коллекция пуста. Все записи удалены из базы данных."});
            } else {
                boolean saved = collectionManager.saveToDatabase();
                if (saved) {
                    return new Task(new String[]{"Коллекция успешно сохранена в базе данных."});
                } else {
                    return new Task(new String[]{"Ошибка при сохранении коллекции в базу данных."});
                }
            }
        } catch (Exception e) {
            return new Task(new String[]{"Ошибка при сохранении коллекции: " + e.getMessage()});
        }
    }
}
