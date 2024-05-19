package ru.itmo.lab5.data.models;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Setter
@Getter
public class Location implements Serializable {

    private int x;
    private double y;
    private String name;

    public Location(int x, double y, String name) {
        this.x = x;
        this.y = y;
        this.name = name;
    }

    public Location(){}

    @Override
    public String toString() {
        return "Location{" +
                "x=" + x +
                ", y=" + y +
                ", name='" + (name != null ? name : "N/A") + '\'' +
                '}';
    }

}
