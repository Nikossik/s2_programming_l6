package ru.itmo.lab5.manager.commands;

import ru.itmo.lab5.manager.DatabaseHandler;
import ru.itmo.lab5.util.Task;

public class RegisterCommand extends Command {

    public RegisterCommand(DatabaseHandler dbHandler) {
        super(dbHandler);
        this.name = "register";
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
