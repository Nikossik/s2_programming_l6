package ru.itmo.lab5.manager.commands;

import ru.itmo.lab5.manager.CollectionManager;
import ru.itmo.lab5.data.models.Ticket;
import ru.itmo.lab5.manager.DatabaseHandler;
import ru.itmo.lab5.util.Task;

/**
 * Команда для вывода всех элементов коллекции.
 */
public class ShowCommand extends Command {

    public ShowCommand(CollectionManager collectionManager, DatabaseHandler dbHandler) {
        super(collectionManager, dbHandler);
        this.name = "show";
        this.description = "Выводит все элементы коллекции";
    }

    @Override
    public Task execute(Task task) {
        try {
            var tickets = collectionManager.getTickets();
            if (tickets.isEmpty()) {
                return new Task(new String[]{"Collection is empty."});
            } else {
                StringBuilder answer = new StringBuilder();
                answer.append("Collection items:\n");
                for (Ticket ticket : tickets) {
                    answer.append(ticket).append("\n");
                }
                return new Task(new String[]{answer.toString()});
            }
        } catch (Exception e) {
            return new Task(new String[]{"Error retrieving tickets: " + e.getMessage()});
        }
    }

}
