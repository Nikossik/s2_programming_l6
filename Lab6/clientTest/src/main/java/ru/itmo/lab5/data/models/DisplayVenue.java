package ru.itmo.lab5.data.models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DisplayVenue extends Venue {

    private long id;
    private String name;
    private int capacity;
    private VenueType type;
    private DisplayAddress address;

    private transient LongProperty idProperty = new SimpleLongProperty();
    private transient StringProperty nameProperty = new SimpleStringProperty();
    private transient IntegerProperty capacityProperty = new SimpleIntegerProperty();
    private transient ObjectProperty<VenueType> typeProperty = new SimpleObjectProperty<>();
    private transient ObjectProperty<DisplayAddress> addressProperty = new SimpleObjectProperty<>();

    public DisplayVenue(Venue venue) {
        this.id = venue.getId();
        this.name = venue.getName();
        this.capacity = venue.getCapacity();
        this.type = venue.getType();
        this.address = new DisplayAddress(venue.getAddress());

        address.bindProperties();
        idProperty.bindBidirectional(new SimpleLongProperty(id));
        nameProperty.bindBidirectional(new SimpleStringProperty(name));
        capacityProperty.bindBidirectional(new SimpleIntegerProperty(capacity));
        typeProperty.bindBidirectional(new SimpleObjectProperty<>(type));
        addressProperty.bindBidirectional(new SimpleObjectProperty<>(address));
    }

    public void bindProperties() {
        address.bindProperties();
        idProperty.bindBidirectional(new SimpleLongProperty(id));
        nameProperty.bindBidirectional(new SimpleStringProperty(name));
        capacityProperty.bindBidirectional(new SimpleIntegerProperty(capacity));
        typeProperty.bindBidirectional(new SimpleObjectProperty<>(type));
        addressProperty.bindBidirectional(new SimpleObjectProperty<>(address));
    }

}