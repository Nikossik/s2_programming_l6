package ru.itmo.lab5.util;

import ru.itmo.lab5.data.models.Ticket;

import java.io.Serializable;

public class Task implements Serializable {
    public String[] describe;
    public Ticket ticket;
    public String username;
    public String password;

    public Task(String[] describe) {
        this.describe = describe;
    }

    public Task() {
    }

    public Task(String[] describe, Ticket ticket) {
        this.describe = describe;
        this.ticket = ticket;
    }

    public Task(String[] describe, Ticket ticket, String username, String password) {
        this.describe = describe;
        this.ticket = ticket;
        this.username = username;
        this.password = password;
    }

    // Getters and Setters
}