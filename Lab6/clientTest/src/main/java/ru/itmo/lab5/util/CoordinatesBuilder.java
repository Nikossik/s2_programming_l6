package ru.itmo.lab5.util;

import ru.itmo.lab5.data.models.Coordinates;

public class CoordinatesBuilder {

    public static Coordinates build() {
        int x = InputHelper.requestInt("Enter the X coordinate: ");
        double y = InputHelper.requestDouble("Enter the Y coordinate: ");
        return new Coordinates(x, y);
    }
}
