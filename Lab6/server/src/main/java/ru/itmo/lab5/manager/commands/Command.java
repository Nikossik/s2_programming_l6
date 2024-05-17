package ru.itmo.lab5.manager.commands;

import lombok.Getter;
import ru.itmo.lab5.manager.DatabaseHandler;
import ru.itmo.lab5.util.Task;

public abstract class Command {
    @Getter
    protected String name;
    protected String description;
    protected DatabaseHandler dbHandler;

    public Command(String name, String description, DatabaseHandler dbHandler) {
        this.name = name;
        this.description = description;
        this.dbHandler = dbHandler;
    }

    public abstract Task execute(Task task, DatabaseHandler dbHandler);

}
