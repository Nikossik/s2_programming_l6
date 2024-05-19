package ru.itmo.lab5.util;

import ru.itmo.lab5.data.models.Coordinates;
import ru.itmo.lab5.data.models.Ticket;
import ru.itmo.lab5.data.models.TicketType;

import java.util.Objects;

public class TicketBuilder {

    public static Ticket buildTicket(String username) {
        String name = InputHelper.requestString("Enter the name of the ticket: ", false);
        Coordinates coordinates = CoordinatesBuilder.build();
        long price = InputHelper.requestInt("Enter the ticket price: ");
        TicketType type;
        while (true) {
            try {
                System.out.println("Available ticket types: " + TicketType.names());
                type = TicketType.valueOf(Objects.requireNonNull(InputHelper.requestString("Enter the ticket type: ", false)).toUpperCase());
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("The type is specified incorrectly. Available only: VIP, USUAL, CHEAP.");
            }
        }
        return new Ticket(name, coordinates, price, type, VenueBuilder.build(), username);
    }
}
