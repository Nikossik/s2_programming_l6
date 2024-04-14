package ru.itmo.lab5.manager.commands;

import ru.itmo.lab5.data.TicketCollection;
import ru.itmo.lab5.data.models.Ticket;
import ru.itmo.lab5.util.Task;


import java.util.Optional;
import java.util.Scanner;
/**
 * Команда для обновления элемента коллекции с указанным ID.
 */
public class UpdateIDCommand extends Command {
    /**
     * Конструктор команды update_id.
     *
     * @param ticketCollection Коллекция билетов, с которой работает команда.
     * @param scanner          Сканер для ввода данных пользователем.
     */
    public UpdateIDCommand(TicketCollection ticketCollection, Scanner scanner) {
        super("update_id <id>", "Обновляет значение элемента коллекции, ID которого равен заданному", ticketCollection);
    }

    @Override
    public Task execute(Task task) {
        if (task.describe[1].isEmpty()) {
            return new Task(new String[]{"спользование: '" + getName() + "'"});
        }
        long id;
        try {
            id = Long.parseLong(task.describe[1]);
        } catch (NumberFormatException e) {
            return new Task(new String[]{"ID должен быть числом. Передано неверное значение: " + task.describe[1]});
        }

        Optional<Ticket> ticketToUpdate = ticketCollection.getTickets().stream()
                .filter(ticket -> ticket.getId() == id)
                .findFirst();

        if (ticketToUpdate.isPresent()) {
            Ticket updatedTicket = task.ticket;
            if (updatedTicket != null) {
                ticketCollection.update(updatedTicket);
                return new Task(new String[]{"Билет с ID " + id + " был успешно обновлен."});
            } else {
                return new Task(new String[]{"Ошибка при создании билета. Обновление отменено."});
            }
        } else {
            return new Task(new String[]{"Билет с таким ID не найден."});
        }
    }
}
