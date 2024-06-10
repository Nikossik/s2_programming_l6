package ru.itmo.lab5.data.models;

import java.util.Date;
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
public class DisplayTicket extends Ticket {

    private int id;
    private String name;
    private DisplayCoordinates coordinates;
    private Date creationDate;
    private Long price;
    private TicketType type;
    private DisplayVenue venue;

    private transient IntegerProperty idProperty = new SimpleIntegerProperty();
    private transient StringProperty nameProperty = new SimpleStringProperty();
    private transient ObjectProperty<DisplayCoordinates> coordinatesProperty = new SimpleObjectProperty<>();
    private transient ObjectProperty<Date> creationDateProperty = new SimpleObjectProperty<>();
    private transient LongProperty priceProperty = new SimpleLongProperty();
    private transient ObjectProperty<TicketType> typeProperty = new SimpleObjectProperty<>();
    private transient ObjectProperty<DisplayVenue> venueProperty = new SimpleObjectProperty<>();

    public DisplayTicket(Ticket ticket) {
        this.id = ticket.getId();
        this.name = ticket.getName();
        this.coordinates = new DisplayCoordinates(ticket.getCoordinates());
        this.creationDate = ticket.getCreationDate();
        this.price = ticket.getPrice();
        this.type = ticket.getType();
        this.venue = new DisplayVenue(ticket.getVenue());

        coordinates.bindProperties();
        venue.bindProperties();
        idProperty.bindBidirectional(new SimpleIntegerProperty(id));
        nameProperty.bindBidirectional(new SimpleStringProperty(name));
        coordinatesProperty.bindBidirectional(new SimpleObjectProperty<>(coordinates));
        creationDateProperty.bindBidirectional(new SimpleObjectProperty<>(creationDate));
        priceProperty.bindBidirectional(new SimpleLongProperty(price));
        typeProperty.bindBidirectional(new SimpleObjectProperty<>(type));
        venueProperty.bindBidirectional(new SimpleObjectProperty<>(venue));
    }

    public void bindProperties() {
        coordinates.bindProperties();
        venue.bindProperties();
        idProperty.bindBidirectional(new SimpleIntegerProperty(id));
        nameProperty.bindBidirectional(new SimpleStringProperty(name));
        coordinatesProperty.bindBidirectional(new SimpleObjectProperty<>(coordinates));
        creationDateProperty.bindBidirectional(new SimpleObjectProperty<>(creationDate));
        priceProperty.bindBidirectional(new SimpleLongProperty(price));
        typeProperty.bindBidirectional(new SimpleObjectProperty<>(type));
        venueProperty.bindBidirectional(new SimpleObjectProperty<>(venue));
    }

}

