package ru.itmo.lab5.manager.commands;

import ru.itmo.lab5.manager.CollectionManager;
import ru.itmo.lab5.manager.DatabaseHandler;
import ru.itmo.lab5.util.Task;

public class RemoveByIDCommand extends Command {

    public RemoveByIDCommand(CollectionManager collectionManager, DatabaseHandler dbHandler) {
        super(collectionManager, dbHandler);
        this.name = "remove_by_id <id>";
        this.description = "Delete a specified collection by id";
    }

    @Override
    public Task execute(Task task) {
        if (task.getDescribe().length < 2 || task.getDescribe()[1].isEmpty()) {
            return new Task(new String[]{"Using: '" + task.getDescribe()[0] + " <id>'"});
        }
        long id;
        try {
            id = Long.parseLong(task.getDescribe()[1]);
        } catch (NumberFormatException e) {
            return new Task(new String[]{"ID must be an INT " + task.getDescribe()[1]});
        }

        boolean removed = this.collectionManager.remove(id);

        if (removed) {
            return new Task(new String[]{"Element with ID: " + id + " successfully deleted."});
        } else {
            return new Task(new String[]{"Element with ID: haven't been removed."});
        }
    }
}
