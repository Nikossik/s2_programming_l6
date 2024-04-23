package ru.itmo.lab5.manager.commands;

import ru.itmo.lab5.data.TicketCollection;
import ru.itmo.lab5.util.Task;

public abstract class Command {
    protected String name;
    protected String description;
    protected TicketCollection ticketCollection;

    public Command(String name, String description, TicketCollection ticketCollection) {
        this.name = name;
        this.description = description;
        this.ticketCollection = ticketCollection;
    }

    public abstract Task execute(Task task);

    public String getName() {
        return name;
    }

}
