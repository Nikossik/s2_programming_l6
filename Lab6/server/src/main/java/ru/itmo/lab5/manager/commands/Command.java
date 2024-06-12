package ru.itmo.lab5.manager.commands;

import lombok.Getter;
import lombok.Setter;
import ru.itmo.lab5.manager.DatabaseHandler;
import ru.itmo.lab5.util.Task;

import java.sql.SQLException;

@Getter
@Setter
public abstract class Command {
    protected String name;
    protected DatabaseHandler dbHandler;

    public Command(DatabaseHandler dbHandler) {
        this.dbHandler = dbHandler;
    }

    public abstract Task execute(Task task) throws SQLException;
}


