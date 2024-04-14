package ru.itmo.lab5.manager.commands;

import ru.itmo.lab5.data.TicketCollection;
import ru.itmo.lab5.data.models.Ticket;
import ru.itmo.lab5.util.Task;

/**
 * Команда для вывода всех элементов коллекции.
 */
public class ShowCommand extends Command {
    /**
     * Конструктор команды show.
     *
     * @param ticketCollection Коллекция билетов, с которой работает команда.
     */
    public ShowCommand(TicketCollection ticketCollection) {
        super("show", "Выводит все элементы коллекции", ticketCollection);
    }

    @Override
    public Task execute(Task task) {
        if (ticketCollection.getTickets().isEmpty()) {
            return new Task(new String[]{"Коллекция пуста."});
        } else {
            String answer = "";
            answer = answer+ "Элементы коллекции:\n";
            for (Ticket ticket : ticketCollection.getTickets()) {
                answer=answer+ticket+"\n";
            }
            return new Task(new String[]{answer});
        }
    }
}
