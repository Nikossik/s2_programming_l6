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
            StringBuilder answer = new StringBuilder();
            answer.append("Коллекция выведена в порядке убывания:\n");
            ticketCollection.getTickets().stream()
                    .sorted(Comparator.comparing(Ticket::getPrice).reversed())
                    .forEach(System.out::println);
            for (Ticket ticket : ticketCollection.getTickets()) {
                answer.append(ticket).append("\n");
            }
            return new Task(new String[]{answer.toString()});
        }
    }
}
