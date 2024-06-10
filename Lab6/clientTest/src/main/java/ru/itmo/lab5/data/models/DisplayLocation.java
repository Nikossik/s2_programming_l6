package ru.itmo.lab5.data.models;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DisplayLocation extends Location {

    private int x;
    private double y;
    private String name;

    private transient IntegerProperty xProperty = new SimpleIntegerProperty();
    private transient DoubleProperty yProperty = new SimpleDoubleProperty();
    private transient StringProperty nameProperty = new SimpleStringProperty();

    public DisplayLocation(Location location) {
        this.x = location.getX();
        this.y = location.getY();
        this.name = location.getName();

        xProperty.bindBidirectional(new SimpleIntegerProperty(x));
        yProperty.bindBidirectional(new SimpleDoubleProperty(y));
        nameProperty.bindBidirectional(new SimpleStringProperty(name));
    }

    public void bindProperties() {
        xProperty.bindBidirectional(new SimpleIntegerProperty(x));
        yProperty.bindBidirectional(new SimpleDoubleProperty(y));
        nameProperty.bindBidirectional(new SimpleStringProperty(name));
    }
}
