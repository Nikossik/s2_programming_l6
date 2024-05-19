package ru.itmo.lab5.util;

import ru.itmo.lab5.data.models.Location;

public class LocationBuilder {

    /**
     * Создает объект {@link Location}, запрашивая у пользователя координаты и название локации.
     *
     * @return Новый объект {@link Location} с заданными параметрами.
     */
    public static Location build() {
        System.out.println("Создание локации.");
        int x = InputHelper.requestInt("Введите координату X локации: ");
        double y = InputHelper.requestDouble("Введите координату Y локации: ");
        String name = InputHelper.requestString("Введите название локации: ", false);
        return new Location(x, y, name);
    }
}
