package ru.itmo.lab5.manager.commands;

import ru.itmo.lab5.manager.CollectionManager;
import ru.itmo.lab5.manager.DatabaseHandler;
import ru.itmo.lab5.util.Task;

public class RegisterCommand extends Command {

    public RegisterCommand(CollectionManager collectionManager, DatabaseHandler dbHandler) {
        super(collectionManager, dbHandler);
        this.name = "register";
        this.description = "Registers a new user";
    }

    @Override
    public Task execute(Task task) {
        try {
            DatabaseHandler.createUser(task.getUsername(), task.getPassword());
            return new Task(new String[]{"true"});
        } catch (Exception e) {
            return new Task(new String[]{"false"});
        }
    }
}
