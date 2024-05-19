package ru.itmo.lab5.util;

import ru.itmo.lab5.data.models.Location;

public class LocationBuilder {

    public static Location build() {
        System.out.println("Creating a location.");
        int x = InputHelper.requestInt("Enter the X coordinate of the location: ");
        double y = InputHelper.requestDouble("Enter the Y coordinate of the location: ");
        String name = InputHelper.requestString("Enter the name of the location: ", false);
        return new Location(x, y, name);
    }
}
