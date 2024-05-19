package ru.itmo.lab5.manager;

import ru.itmo.lab5.manager.commands.*;
import ru.itmo.lab5.util.Task;

import java.util.HashMap;
import java.util.Map;

public class CommandInvoker {
    private final Map<String, CommandFactory> commandMap = new HashMap<>();
    private final CollectionManager collectionManager;
    private final DatabaseHandler dbHandler;

    public CommandInvoker(CollectionManager collectionManager, DatabaseHandler dbHandler) {
        this.collectionManager = collectionManager;
        this.dbHandler = dbHandler;
        registerCommands();
    }

    private void registerCommands() {
        register("add", AddCommand::new);
        register("add_if_min", AddIfMinCommand::new);
        register("register", RegisterCommand::new);
        register("login", LoginCommand::new);
        register("clear", ClearCommand::new);
        register("filter_less_than_venue", FilterLessThanVenueCommand::new);
        register("help", HelpCommand::new);
        register("info", InfoCommand::new);
        register("print_descending", PrintDescendingCommand::new);
        register("print_field_descending_venue", PrintFieldDescendingVenueCommand::new);
        register("remove_at", RemoveAtCommand::new);
        register("remove_by_id", RemoveByIDCommand::new);
        register("remove_first", RemoveFirstCommand::new);
        register("save", SaveCommand::new);
        register("show", ShowCommand::new);
        register("update_id", UpdateIDCommand::new);
    }

    public void register(String commandName, CommandFactory commandFactory) {
        commandMap.put(commandName, commandFactory);
    }

    public Task executeCommand(Task task) {
        CommandFactory factory = commandMap.get(task.getDescribe()[0]);
        if (factory != null) {
            Command command = factory.create(collectionManager, dbHandler);
            return command.execute(task);
        } else {
            return new Task(new String[]{"Unknown command. Enter 'help' for assistance."});
        }
    }
}
