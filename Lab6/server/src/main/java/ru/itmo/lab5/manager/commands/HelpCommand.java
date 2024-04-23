package ru.itmo.lab5.manager.commands;

import ru.itmo.lab5.data.TicketCollection;
import ru.itmo.lab5.util.Task;

/**
 * Команда для вывода справки по доступным командам.
 */

public class HelpCommand extends Command {
    /**
     * Конструктор команды help.
     *
     * @param ticketCollection Коллекция билетов, с которой работает команда.
     */
    public HelpCommand(TicketCollection ticketCollection) {
        super("help", "Выводит справку по доступным командам", ticketCollection);
    }

    @Override
    public Task execute(Task task) {
        System.out.println("Доступные команды:");
        return task;
    }
}
