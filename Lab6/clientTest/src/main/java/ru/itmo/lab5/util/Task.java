package ru.itmo.lab5.util;

import lombok.Getter;
import lombok.Setter;
import ru.itmo.lab5.data.models.Ticket;

import java.io.Serializable;

@Getter
@Setter
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

    public Task(String[] describe, Ticket ticket, String username, String password) {
        this.describe = describe;
        this.ticket = ticket;
        this.username = username;
        this.password = password;
    }
}
