package ru.itmo.lab5.data.models;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.*;

import java.io.Serializable;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Venue implements Serializable {
    private static long idCounter = 0;
    private long id;
    private String name;
    private int capacity;
    private VenueType type;
    private Address address;

    public Venue(String name, int capacity, VenueType type, Address address) {
        this.id = ++idCounter;
        this.name = name;
        this.capacity = capacity;
        this.type = type;
        this.address = address;
    }

    public static void updateNextId(long nextIdValue) {
        idCounter = nextIdValue;
    }

}
