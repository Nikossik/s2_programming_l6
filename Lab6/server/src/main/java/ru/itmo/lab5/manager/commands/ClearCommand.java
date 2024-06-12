package ru.itmo.lab5.manager.commands;

import ru.itmo.lab5.manager.DatabaseHandler;
import ru.itmo.lab5.util.Task;

import java.sql.SQLException;

public class ClearCommand extends Command {
    public ClearCommand(DatabaseHandler dbHandler) {
        super(dbHandler);
        this.name = "clear";
    }

    @Override
    public Task execute(Task task) throws SQLException {
        String username = task.getUsername();
        if (username == null || username.isEmpty()) {
            return new Task(new String[]{"false"});
        }
        dbHandler.clearTicketsByUsername(username);
        return new Task(new String[]{"true"});
    }
}
