package ru.itmo.lab5.manager;

import ru.itmo.lab5.manager.commands.Command;

@FunctionalInterface
public interface CommandFactory {
    Command create(CollectionManager collectionManager, DatabaseHandler dbHandler);
}
