package ru.itmo.lab5.util;

import ru.itmo.lab5.data.models.Address;
import ru.itmo.lab5.data.models.Location;

public class AddressBuilder {

    public static Address build() {
        String street = InputHelper.requestString("Enter the street: ", false);
        String zipCode = InputHelper.requestString("Enter the zip code: ", false);
        Location location = LocationBuilder.build();
        return new Address(street, zipCode, location);
    }
}
