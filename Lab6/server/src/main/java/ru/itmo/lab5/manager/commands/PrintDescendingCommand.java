package ru.itmo.lab5.manager.commands;

import ru.itmo.lab5.data.TicketCollection;
import ru.itmo.lab5.data.models.Ticket;
import ru.itmo.lab5.util.Task;

import java.util.Comparator;
/**
 * Команда для вывода элементов коллекции в порядке убывания цены.
 */
public class PrintDescendingCommand extends Command {
    /**
     * Конструктор команды print_descending.
     *
     * @param ticketCollection Коллекция билетов, с которой работает команда.
     */
    public PrintDescendingCommand(TicketCollection ticketCollection) {
        super("print_descending", "Выводит элементы коллекции в порядке убывания", ticketCollection);
    }

    @Override
    public Task execute(Task task) {
        if (ticketCollection.getTickets().isEmpty()) {
            return new Task(new String[]{"Коллекция пуста."});
        } else {
            ticketCollection.getTickets().stream()
                    .sorted(Comparator.comparing(Ticket::getPrice).reversed())
                    .forEach(System.out::println);
            return new Task(new String[]{"Элементы коллекции выведены в порядке убывания."});
        }
    }
}
