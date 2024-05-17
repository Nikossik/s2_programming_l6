package ru.itmo.lab5.manager.commands;

import ru.itmo.lab5.manager.DatabaseHandler;
import ru.itmo.lab5.data.models.Ticket;
import ru.itmo.lab5.util.Task;

import java.util.Optional;

/**
 * Команда для обновления элемента коллекции с указанным ID.
 */
public class UpdateIDCommand extends Command {
    private final DatabaseHandler dbHandler;

    /**
     * Конструктор команды update_id.
     *
     * @param dbHandler Обработчик базы данных для взаимодействия с БД.
     */
    public UpdateIDCommand(DatabaseHandler dbHandler) {
        super("update_id <id>", "Обновляет значение элемента коллекции, ID которого равен заданному", dbHandler);
        this.dbHandler = dbHandler;
    }

    @Override
    public Task execute(Task task, DatabaseHandler dbHandler) {
        if (task.describe.length < 2 || task.describe[1].isEmpty()) {
            return new Task(new String[]{"использование: '" + getName() + "'"});
        }
        long id;
        try {
            id = Long.parseLong(task.describe[1]);
        } catch (NumberFormatException e) {
            return new Task(new String[]{"ID должен быть числом. Передано неверное значение: " + task.describe[1]});
        }

        Optional<Ticket> ticketToUpdate = this.dbHandler.getTicketById(id);

        if (ticketToUpdate.isPresent()) {
            Ticket updatedTicket = task.ticket;
            if (updatedTicket != null) {
                this.dbHandler.updateTicket(updatedTicket);
                return new Task(new String[]{"Билет с ID " + id + " был успешно обновлен."});
            } else {
                return new Task(new String[]{"Ошибка при создании билета. Обновление отменено."});
            }
        } else {
            return new Task(new String[]{"Билет с таким ID не найден."});
        }
    }
}
