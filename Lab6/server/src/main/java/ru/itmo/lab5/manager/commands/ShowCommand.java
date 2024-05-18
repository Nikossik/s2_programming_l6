package ru.itmo.lab5.manager.commands;

import ru.itmo.lab5.manager.CollectionManager;
import ru.itmo.lab5.data.models.Ticket;
import ru.itmo.lab5.util.Task;

/**
 * Команда для вывода всех элементов коллекции.
 */
public class ShowCommand extends Command {
    private final CollectionManager collectionManager;

    /**
     * Конструктор команды show.
     *
     * @param collectionManager Менеджер коллекции для взаимодействия с коллекцией.
     */
    public ShowCommand(CollectionManager collectionManager) {
        super("show", "Выводит все элементы коллекции", collectionManager);
        this.collectionManager = collectionManager;
    }

    @Override
    public Task execute(Task task) {
        try {
            var tickets = collectionManager.getTickets();
            if (tickets.isEmpty()) {
                return new Task(new String[]{"Коллекция пуста."});
            } else {
                StringBuilder answer = new StringBuilder();
                answer.append("Элементы коллекции:\n");
                for (Ticket ticket : tickets) {
                    answer.append(ticket).append("\n");
                }
                return new Task(new String[]{answer.toString()});
            }
        } catch (Exception e) {
            return new Task(new String[]{"Ошибка при получении билетов: " + e.getMessage()});
        }
    }
}
