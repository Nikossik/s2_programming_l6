package ru.itmo.lab5.manager.commands;

import ru.itmo.lab5.data.models.Ticket;
import ru.itmo.lab5.manager.DatabaseHandler;
import ru.itmo.lab5.util.Task;

/**
 * Команда для обновления элемента коллекции с указанным ID.
 */
public class UpdateIDCommand extends Command {
    public UpdateIDCommand(DatabaseHandler dbHandler) {
        super(dbHandler);
        this.name = "update_id <id>";
    }

    @Override
    public Task execute(Task task) {
        if (task.getDescribe().length < 2 || task.getDescribe()[1].isEmpty()) {
            return new Task(new String[]{"false"});
        }
        long id;
        try {
            id = Long.parseLong(task.getDescribe()[1]);
        } catch (NumberFormatException e) {
            return new Task(new String[]{"false"});
        }

        Ticket updatedTicket = task.getTicket();
        if (updatedTicket != null) {
            try {
                boolean updated = dbHandler.updateTicketById(id, updatedTicket);
                if (updated) {
                    return new Task(new String[]{"true"});
                } else {
                    return new Task(new String[]{"false"});
                }
            } catch (Exception e) {
                return new Task(new String[]{"false"});
            }
        } else {
            return new Task(new String[]{"false"});
        }
    }
}
