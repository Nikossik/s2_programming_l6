package ru.itmo.lab5.manager;

import ru.itmo.lab5.manager.commands.*;
import ru.itmo.lab5.util.Task;

import java.util.HashMap;
import java.util.Map;

public class CommandInvoker {
    private final Map<String, Command> commandMap = new HashMap<>();
    private final DatabaseHandler databaseHandler;

    public CommandInvoker(DatabaseHandler databaseHandler) {
        this.databaseHandler = databaseHandler;
        registerCommands();
    }

    private void registerCommands() {
        register("help", new HelpCommand(databaseHandler));
        register("info", new InfoCommand(databaseHandler));
        register("show", new ShowCommand(databaseHandler));
        register("add", new AddCommand(databaseHandler));
        register("update_id", new UpdateIDCommand(databaseHandler));
        register("remove_by_id", new RemoveByIDCommand(databaseHandler));
        register("clear", new ClearCommand(databaseHandler));
        register("remove_at", new RemoveAtCommand(databaseHandler));
        register("remove_first", new RemoveFirstCommand(databaseHandler));
        register("add_if_min", new AddIfMinCommand(databaseHandler));
        register("filter_less_than_venue", new FilterLessThanVenueCommand(databaseHandler));
        register("print_descending", new PrintDescendingCommand(databaseHandler));
        register("print_field_descending_venue", new PrintFieldDescendingVenueCommand(databaseHandler));
        register("SaveCommand", new SaveCommand(databaseHandler));
    }

    private void register(String commandName, Command command) {
        commandMap.put(commandName, command);
    }

    public Task executeCommand(Task task) {
        String commandName = task.describe[0].toLowerCase();
        Command command = commandMap.get(commandName);
        if (command != null) {
            return command.execute(task, databaseHandler);
        } else {
            return new Task(new String[]{"Неизвестная команда '" + commandName + "'. Введите 'help' для получения списка команд."});
        }
    }

    public Map<String, Command> getCommands() {
        return commandMap;
    }
}
