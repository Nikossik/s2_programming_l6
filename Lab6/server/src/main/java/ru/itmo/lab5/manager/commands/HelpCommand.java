package ru.itmo.lab5.manager.commands;

import ru.itmo.lab5.data.TicketCollection;
import ru.itmo.lab5.manager.CommandInvoker;
import ru.itmo.lab5.util.Task;

/**
 * Команда для вывода справки по доступным командам.
 */

public class HelpCommand extends Command {
    private final CommandInvoker commandInvoker;
    /**
     * Конструктор команды help.
     *
     * @param ticketCollection Коллекция билетов, с которой работает команда.
     * @param commandInvoker нвокер команд для доступа к списку команд.
     */
    public HelpCommand(TicketCollection ticketCollection, CommandInvoker commandInvoker) {
        super("help", "Выводит справку по доступным командам", ticketCollection);
        this.commandInvoker = commandInvoker;
    }

    @Override
    public Task execute(Task task) {
        return new Task(new String[]{"Блять Никита просто блять верни строку а не выебывайся, ты просто жрешь jvm чтоб сложить строки, это просто пиздец"});
    }
}
