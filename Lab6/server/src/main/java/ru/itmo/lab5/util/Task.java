package ru.itmo.lab5.util;

import ru.itmo.lab5.data.models.Ticket;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class Task implements Serializable {
    private String[] describe;
    private Ticket ticket;
    private String username;
    private String password;
    private String collectionType;
    private boolean status;

    public Task(String[] describe) {
        this.describe = describe;
    }

    public Task(String[] describe, boolean status) {
        this.describe = describe;
        this.status = status;
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
