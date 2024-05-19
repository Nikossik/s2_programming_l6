package ru.itmo.lab5.util;

import ru.itmo.lab5.data.models.Address;
import ru.itmo.lab5.data.models.Venue;
import ru.itmo.lab5.data.models.VenueType;

import java.util.Objects;

public class VenueBuilder {
    public static Venue build() {
        System.out.println("Creating a venue.");
        String name = InputHelper.requestString("Enter the name of the venue: ", false);
        int capacity = InputHelper.requestInt("Enter the capacity: ");
        VenueType type;
        while(true) {
            try {
                System.out.println("Available types of seats: " + VenueType.names());
                type = VenueType.valueOf(Objects.requireNonNull(InputHelper.requestString("Enter the type of venue: ", true)).toUpperCase());
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("The type is specified incorrectly. Available only: LOFT, OPEN_AREA, THEATRE, STADIUM.");
            }
        }
        Address address = AddressBuilder.build();
        return new Venue(name, capacity, type, address);
    }
}
