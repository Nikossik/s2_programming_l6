package ru.itmo.lab5.manager.commands;

import ru.itmo.lab5.data.models.Ticket;
import ru.itmo.lab5.manager.CollectionManager;
import ru.itmo.lab5.util.Task;

import java.util.Comparator;

/**
 * Команда для добавления нового билета в коллекцию, если его цена меньше минимальной в коллекции.
 * Запрашивает у пользователя данные для создания билета и добавляет его в коллекцию при условии.
 */
public class AddIfMinCommand extends Command {

    private final CollectionManager collectionManager;

    /**
     * Конструктор для команды добавления билета, если его цена меньше минимальной.
     *
     * @param collectionManager Менеджер коллекции для взаимодействия с коллекцией.
     */
    public AddIfMinCommand(CollectionManager collectionManager) {
        super("add_if_min", "Добавляет новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции", collectionManager);
        this.collectionManager = collectionManager;
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
