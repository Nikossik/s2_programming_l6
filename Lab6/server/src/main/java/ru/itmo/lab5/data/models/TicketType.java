package ru.itmo.lab5.data.models;

import java.io.Serializable;

public enum TicketType implements Serializable {
    VIP,
    USUAL,
    CHEAP;

    public static String names() {
        StringBuilder nameList = new StringBuilder();
        for (var ticketType : values()) {
            nameList.append(ticketType.name()).append(", ");
        }
        return nameList.substring(0, nameList.length() - 2);
    }
}
