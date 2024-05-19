package ru.itmo.lab5.util;

import ru.itmo.lab5.data.models.Coordinates;
import ru.itmo.lab5.data.models.Ticket;
import ru.itmo.lab5.data.models.TicketType;

import java.util.Objects;

public class TicketBuilder {

    /**
     * Создает объект {@link Ticket}, запрашивая у пользователя необходимые данные.
     *
     * @return Новый объект {@link Ticket} с заданными параметрами.
     */
    public static Ticket buildTicket(String username) {
        String name = InputHelper.requestString("Введите имя билета: ", false);
        Coordinates coordinates = CoordinatesBuilder.build();
        long price = InputHelper.requestInt("Введите цену билета: ");
        TicketType type;
        while (true) {
            try {
                System.out.println("Доступные типы билетов: " + TicketType.names());
                type = TicketType.valueOf(Objects.requireNonNull(InputHelper.requestString("Введите тип билета: ", false)).toUpperCase());
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Неверно указан тип. Доступны только: VIP, USUAL, CHEAP.");
            }
        }
        return new Ticket(name, coordinates, price, type, VenueBuilder.build(), username);
    }
}
