package ru.itmo.lab5.manager;

import ru.itmo.lab5.manager.commands.Command;

@FunctionalInterface
public interface CommandFactory {
    Command create(DatabaseHandler dbHandler);
}
