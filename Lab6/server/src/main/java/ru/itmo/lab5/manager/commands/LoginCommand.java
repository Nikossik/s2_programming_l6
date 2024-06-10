package ru.itmo.lab5.manager.commands;

import ru.itmo.lab5.manager.CollectionManager;
import ru.itmo.lab5.manager.DatabaseHandler;
import ru.itmo.lab5.util.Task;

public class LoginCommand extends Command {

    public LoginCommand(CollectionManager collectionManager, DatabaseHandler dbHandler) {
        super(collectionManager, dbHandler);
        this.name = "login";
        this.description = "Logs in a user";
    }

    @Override
    public Task execute(Task task) {
        try {
            boolean loginSuccess = DatabaseHandler.checkUserPassword(task.getUsername(), task.getPassword());
            if (loginSuccess) {
                return new Task(new String[]{"true"});
            } else {
                return new Task(new String[]{"false"});
            }
        } catch (Exception e) {
            return new Task(new String[]{"Error during login: " + e.getMessage()});
        }
    }
}
