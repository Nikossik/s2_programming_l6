package ru.itmo.lab5.manager.commands;

import ru.itmo.lab5.manager.CollectionManager;
import ru.itmo.lab5.data.models.Ticket;
import ru.itmo.lab5.util.Task;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Команда для вывода элементов коллекции в порядке убывания цены.
 */
public class PrintDescendingCommand extends Command {
    private final CollectionManager collectionManager;

    /**
     * Конструктор команды print_descending.
     *
     * @param collectionManager Менеджер коллекции для взаимодействия с коллекцией.
     */
    public PrintDescendingCommand(CollectionManager collectionManager) {
        super("print_descending", "Выводит элементы коллекции в порядке убывания", collectionManager);
        this.collectionManager = collectionManager;
    }

    @Override
    public Task execute(Task task) {
        List<Ticket> sortedTickets = collectionManager.getTickets().stream()
                .sorted(Comparator.comparing(Ticket::getPrice).reversed())
                .collect(Collectors.toList());

        if (sortedTickets.isEmpty()) {
            return new Task(new String[]{"Коллекция пуста."});
        } else {
            StringBuilder answer = new StringBuilder("Коллекция выведена в порядке убывания:\n");
            sortedTickets.forEach(ticket -> answer.append(ticket).append("\n"));
            return new Task(new String[]{answer.toString()});
        }
    }
}
