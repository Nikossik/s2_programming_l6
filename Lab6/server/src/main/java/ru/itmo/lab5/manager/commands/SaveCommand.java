package ru.itmo.lab5.manager.commands;

import ru.itmo.lab5.manager.CollectionManager;
import ru.itmo.lab5.util.Task;

/**
 * Команда для сохранения текущего состояния коллекции в базу данных.
 */
public class SaveCommand extends Command {
    private final CollectionManager collectionManager;

    public SaveCommand(CollectionManager collectionManager) {
        super("save", "Saves the current collection state to the database", collectionManager);
        this.collectionManager = collectionManager;
    }

    @Override
    public Task execute(Task task) {
        if (!collectionManager.getTickets().isEmpty()) {
            try {
                if (collectionManager.saveToDatabase()) {
                    return new Task(new String[]{"Collection saved to database."});
                } else {
                    return new Task(new String[]{"Error saving collection to database."});
                }
            } catch (Exception e) {
                return new Task(new String[]{"Error saving collection: " + e.getMessage()});
            }
        } else {
            return new Task(new String[]{"Collection is empty!"});
        }
    }
}
