package ru.itmo.lab5.util;

import ru.itmo.lab5.data.models.Address;
import ru.itmo.lab5.data.models.Location;

public class AddressBuilder {
    /**
     * Создает и возвращает экземпляр {@link Address}, собирая данные от пользователя.
     * Для создания объекта {@link Address} запрашиваются улица, почтовый индекс и {@link Location}.
     * Создание {@link Location} осуществляется с использованием {@link LocationBuilder}.
     *
     * @return Новый экземпляр {@link Address} с заполненными полями.
     */
    public static Address build() {
        String street = InputHelper.requestString("Введите улицу: ", false);
        String zipCode = InputHelper.requestString("Введите почтовый индекс: ", false);
        Location location = LocationBuilder.build();
        return new Address(street, zipCode, location);
    }
}
