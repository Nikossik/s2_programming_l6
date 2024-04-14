package ru.itmo.lab5.util;

import ru.itmo.lab5.data.models.Ticket;

import java.io.Serializable;
import java.util.ArrayList;

public class Task implements Serializable {
    public String[] describe;
    public Ticket ticket;

    public Task(String[] describe) {
        this.describe = describe;
    }

    public Task() {
    }

    public Task(String[] describe, Ticket ticket) {
        this.describe = describe;
        this.ticket = ticket;
    }
}
