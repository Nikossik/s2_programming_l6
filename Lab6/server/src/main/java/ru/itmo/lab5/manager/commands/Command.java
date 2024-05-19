package ru.itmo.lab5.manager.commands;

import lombok.Getter;
import lombok.Setter;
import ru.itmo.lab5.manager.CollectionManager;
import ru.itmo.lab5.manager.DatabaseHandler;
import ru.itmo.lab5.util.Task;

@Getter
@Setter
public abstract class Command {
    protected String name;
    protected String description;
    protected CollectionManager collectionManager;
    protected DatabaseHandler dbHandler;

    public Command(CollectionManager collectionManager, DatabaseHandler dbHandler) {
        this.collectionManager = collectionManager;
        this.dbHandler = dbHandler;
    }

    public abstract Task execute(Task task);
}


