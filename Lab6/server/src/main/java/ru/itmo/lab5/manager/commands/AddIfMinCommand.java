package ru.itmo.lab5.manager.commands;

import ru.itmo.lab5.data.models.Ticket;
import ru.itmo.lab5.manager.CollectionManager;
import ru.itmo.lab5.manager.DatabaseHandler;
import ru.itmo.lab5.util.Task;

import java.util.Comparator;

public class AddIfMinCommand extends Command {
    public AddIfMinCommand(CollectionManager collectionManager, DatabaseHandler dbHandler) {
        super(collectionManager, dbHandler);
        this.name = "add_if_min";
        this.description = "Добавляет новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции";
    }

    @Override
    public Task execute(Task task) {
        Ticket newTicket = task.getTicket();
        if (newTicket == null) {
            return new Task(new String[]{"Не удалось создать билет. Отмена операции."});
        }

        Ticket minTicket = collectionManager.getTickets().stream()
                .min(Comparator.comparing(Ticket::getPrice))
                .orElse(null);

        if (minTicket == null || newTicket.getPrice() < minTicket.getPrice()) {
            boolean added = collectionManager.add(newTicket);
            if (added) {
                return new Task(new String[]{"Новый билет успешно добавлен в коллекцию."});
            } else {
                return new Task(new String[]{"Ошибка при добавлении билета в коллекцию."});
            }
        } else {
            return new Task(new String[]{"Новый билет не является минимальным. Операция добавления не выполнена."});
        }
    }
}
