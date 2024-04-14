package ru.itmo.lab5.util;

import ru.itmo.lab5.data.models.Coordinates;

public class CoordinatesBuilder {

    /**
     * Создает объект {@link Coordinates}, запрашивая у пользователя координаты X и Y.
     *
     * @return Новый объект {@link Coordinates} с заданными координатами.
     */
    public static Coordinates build() {
        int x = InputHelper.requestInt("Введите координату X: ");
        double y = InputHelper.requestDouble("Введите координату Y: ");
        return new Coordinates(x, y);
    }
}
