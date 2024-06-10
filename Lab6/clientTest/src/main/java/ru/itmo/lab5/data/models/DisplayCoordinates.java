package ru.itmo.lab5.data.models;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DisplayCoordinates extends Coordinates {

    private int x;
    private Double y;

    private transient IntegerProperty xProperty = new SimpleIntegerProperty();
    private transient DoubleProperty yProperty = new SimpleDoubleProperty();

    public DisplayCoordinates(Coordinates coordinates) {
        this.x = coordinates.getX();
        this.y = coordinates.getY();

        xProperty.bindBidirectional(new SimpleIntegerProperty(x));
        yProperty.bindBidirectional(new SimpleDoubleProperty(y));
    }

    public void bindProperties() {
        xProperty.bindBidirectional(new SimpleIntegerProperty(x));
        yProperty.bindBidirectional(new SimpleDoubleProperty(y));
    }

    public IntegerProperty getXProperty() {
        return xProperty;
    }

    public DoubleProperty getYProperty() {
        return yProperty;
    }
}
