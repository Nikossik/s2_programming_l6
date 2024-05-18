package ru.itmo.lab5.manager;

import ru.itmo.lab5.manager.commands.*;
import ru.itmo.lab5.util.Task;

import java.util.HashMap;
import java.util.Map;

public class CommandInvoker {
    private final Map<String, Command> commandMap = new HashMap<>();
    private final CollectionManager collectionManager;
    private final DatabaseHandler dbHandler;

    public CommandInvoker(CollectionManager collectionManager, DatabaseHandler dbHandler) {
        this.collectionManager = collectionManager;
        this.dbHandler = dbHandler;
        registerCommands();
    }

    private void registerCommands() {
        register("register", new RegisterCommand(dbHandler));
        register("login", new LoginCommand(dbHandler));
        register("add", new AddCommand(collectionManager));
        register("clear", new ClearCommand(collectionManager));
        register("info", new InfoCommand(collectionManager));
        register("remove_at", new RemoveAtCommand(collectionManager));
        register("remove_by_id", new RemoveByIDCommand(collectionManager));
        register("remove_first", new RemoveFirstCommand(collectionManager));
        register("show", new ShowCommand(collectionManager));
        register("update_id", new UpdateIDCommand(collectionManager));
        register("filter_less_than_venue", new FilterLessThanVenueCommand(collectionManager));
        register("print_descending", new PrintDescendingCommand(collectionManager));
        register("print_field_descending_venue", new PrintFieldDescendingVenueCommand(collectionManager));
        register("help", new HelpCommand(collectionManager));
        register("save", new SaveCommand(collectionManager));
    }

    public void register(String commandName, Command command) {
        commandMap.put(commandName, command);
    }

    public Task executeCommand(Task task) {
        Command command = commandMap.get(task.getDescribe()[0]);
        if (command != null) {
            return command.execute(task);
        } else {
            return new Task(new String[]{"Неизвестная команда. Введите 'help' для помощи."});
        }
    }
}
