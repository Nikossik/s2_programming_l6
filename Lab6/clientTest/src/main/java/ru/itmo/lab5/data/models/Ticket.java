package ru.itmo.lab5.data.models;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Ticket implements Comparable<Ticket>, Serializable {
    private static int nextId = 1;
    private int id;
    private String name;
    private Coordinates coordinates;
    private Date creationDate;
    private Long price;
    private TicketType type;
    private Venue venue;
    private String username;

    public Ticket(String name, Coordinates coordinates, Long price, TicketType type, Venue venue, String username) {
        this.id = ++nextId;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = new Date();
        this.price = price;
        this.type = type;
        this.venue = venue;
        this.username = username;
    }

    public boolean validate() {
        if (name == null || name.isEmpty()) return false;
        if (coordinates == null || !coordinates.validate()) return false;
        return price == null || price > 0;
    }

    @Override
    public int compareTo(Ticket o) {
        if (this.price != null && o.price != null) {
            int priceCompare = this.price.compareTo(o.price);
            if (priceCompare != 0) {
                return priceCompare;
            }
        } else if (this.price == null && o.price != null) {
            return -1;
        } else if (this.price != null) {
            return 1;
        }
        return this.creationDate.compareTo(o.creationDate);
    }

    public static void updateNextId(int nextIdValue) {
        nextId = nextIdValue;
    }

    @Override
    public String toString() {
        return "Ticket(id=" + id + ", name=" + name + ", coordinates=" + coordinates + ", creationDate=" + creationDate + ", price=" + price + ", type=" + type + ", venue=" + venue + ", username=" + username + ")";
    }
}
