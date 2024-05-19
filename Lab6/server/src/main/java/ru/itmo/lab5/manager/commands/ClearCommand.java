package ru.itmo.lab5.manager.commands;

import ru.itmo.lab5.manager.CollectionManager;
import ru.itmo.lab5.manager.DatabaseHandler;
import ru.itmo.lab5.util.Task;

public class ClearCommand extends Command {
    public ClearCommand(CollectionManager collectionManager, DatabaseHandler dbHandler) {
        super(collectionManager, dbHandler);
        this.name = "clear";
        this.description = "Clear the collection";
    }

    @Override
    public Task execute(Task task) {
        String username = task.getUsername();
        if (username == null || username.isEmpty()) {
            return new Task(new String[]{"Username is required to clear tickets."});
        }
        collectionManager.clear(username);
        return new Task(new String[]{"Collection successfully cleared for user: " + username});
    }
}
