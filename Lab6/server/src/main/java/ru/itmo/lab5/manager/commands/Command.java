package ru.itmo.lab5.manager.commands;

import lombok.Getter;
import lombok.Setter;
import ru.itmo.lab5.manager.CollectionManager;
import ru.itmo.lab5.manager.DatabaseHandler;
import ru.itmo.lab5.util.Task;

public abstract class Command {
    @Getter
    @Setter
    private CollectionManager collectionManager;
    @Getter
    @Setter
    private DatabaseHandler dbHandler;
    protected String name;
    protected String description;

    public Command(String name, String description, CollectionManager collectionManager) {
        this.name = name;
        this.description = description;
        this.collectionManager = collectionManager;
    }

    public Command(String name, String description, DatabaseHandler dbHandler) {
        this.name = name;
        this.description = description;
        this.dbHandler = dbHandler;
    }

    public abstract Task execute(Task task);
}
