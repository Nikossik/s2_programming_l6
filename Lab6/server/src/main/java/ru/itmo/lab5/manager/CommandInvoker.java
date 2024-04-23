package ru.itmo.lab5.manager;

import ru.itmo.lab5.data.TicketCollection;
import ru.itmo.lab5.manager.commands.*;
import ru.itmo.lab5.util.Task;

import java.util.HashMap;
import java.util.Map;

public class CommandInvoker {
    private final Map<String, Command> commandMap = new HashMap<>();

    /**
     * Конструктор класса CommandInvoker.
     * Инициализирует новый экземпляр класса с заданной коллекцией билетов и менеджером дампа.
     *
     * @param ticketCollection Коллекция билетов для управления.
     * @param dumpManager Менеджер для работы с файлами сохранения/загрузки коллекции.
     */
    public CommandInvoker(TicketCollection ticketCollection, DumpManager dumpManager) {

        register("help", new HelpCommand(ticketCollection));
        register("info", new InfoCommand(ticketCollection));
        register("show", new ShowCommand(ticketCollection));
        register("add", new AddCommand(ticketCollection));
        register("update_id", new UpdateIDCommand(ticketCollection));
        register("remove_by_id", new RemoveByIDCommand(ticketCollection));
        register("clear", new ClearCommand(ticketCollection));
        register("save", new SaveCommand(ticketCollection, dumpManager));
        register("remove_at", new RemoveAtCommand(ticketCollection));
        register("remove_first", new RemoveFirstCommand(ticketCollection));
        register("add_if_min", new AddIfMinCommand(ticketCollection));
        register("filter_less_than_venue", new FilterLessThanVenueCommand(ticketCollection));
        register("print_descending", new PrintDescendingCommand(ticketCollection));
        register("print_field_descending_venue", new PrintFieldDescendingVenueCommand(ticketCollection));
    }

    /**
     * Регистрирует новую команду в иинвокере.
     *
     * @param commandName мя команды.
     * @param command Объект команды.
     */
    private void register(String commandName, Command command) {
        commandMap.put(commandName, command);
    }


    public Task executeCommand(Task task) {
        System.out.println(task.describe[0]);
        if (task.describe.length == 0) {
            return new Task(new String[]{"Не указана команда. Введите 'help' для получения списка команд."});
        }

        Command command = commandMap.get(task.describe[0].toLowerCase());
        if (command != null) {
            return command.execute(task);
        } else {
            return new Task(new String[]{"Неизвестная команда '" + task.describe[0] + "'. Введите 'help' для получения списка команд."});
        }
    }
    /**
     * Возвращает зарегистрированные команды.
     *
     * @return все команды.
     */
    public Map<String, Command> getCommands() {
        return commandMap;
    }
}
