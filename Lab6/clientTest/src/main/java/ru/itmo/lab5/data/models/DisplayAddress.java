package ru.itmo.lab5.data.models;

import javafx.beans.property.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DisplayAddress extends Address {

    private final String street;
    private final String zipCode;
    private final DisplayLocation town;

    // JavaFX Properties
    private final transient StringProperty streetProperty = new SimpleStringProperty();
    private final transient StringProperty zipCodeProperty = new SimpleStringProperty();
    private final transient ObjectProperty<DisplayLocation> townProperty = new SimpleObjectProperty<>();

    public DisplayAddress(Address address) {
        this.street = address.getStreet();
        this.zipCode = address.getZipCode();
        this.town = new DisplayLocation(address.getTown());

        town.bindProperties();
        streetProperty.bindBidirectional(new SimpleStringProperty(street));
        zipCodeProperty.bindBidirectional(new SimpleStringProperty(zipCode));
        townProperty.bindBidirectional(new SimpleObjectProperty<>(town));
    }

    public void bindProperties() {
        town.bindProperties();
        streetProperty.bindBidirectional(new SimpleStringProperty(street));
        zipCodeProperty.bindBidirectional(new SimpleStringProperty(zipCode));
        townProperty.bindBidirectional(new SimpleObjectProperty<>(town));
    }

}
