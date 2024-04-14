package ru.itmo.lab5.util;

import ru.itmo.lab5.data.models.Address;
import ru.itmo.lab5.data.models.Venue;
import ru.itmo.lab5.data.models.VenueType;

import java.util.Objects;

public class VenueBuilder {
    /**
     * Создает объект {@link Venue}, запрашивая у пользователя необходимые данные.
     *
     * @return Новый объект {@link Venue} с заданными параметрами.
     */
    public static Venue build() {
        System.out.println("Создание места проведения.");
        String name = InputHelper.requestString("Введите имя места проведения: ", false);
        int capacity = InputHelper.requestInt("Введите вместимость: ");
        VenueType type;
        while(true) {
            try {
                System.out.println("Доступные типы мест: " + VenueType.names());
                type = VenueType.valueOf(Objects.requireNonNull(InputHelper.requestString("Введите тип места проведения: ", true)).toUpperCase());
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Неверно указан тип. Доступны только: LOFT, OPEN_AREA, THEATRE, STADIUM.");
            }
        }
        Address address = AddressBuilder.build();
        return new Venue(name, capacity, type, address);
    }
}
