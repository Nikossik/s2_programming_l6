package ru.itmo.lab5.data.models;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Класс Location представляет собой местоположение с координатами и названием.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Setter
@Getter
public class Location implements Serializable {
    /**
     * Координата X местоположения.
     */
    private int x;
    /**
     * Координата Y местоположения.
     */
    private double y;
    /**
     * Название местоположения. Длина строки не должна превышать 210 символов.
     * Может быть null, если название не задано.
     */
    private String name; //Длина строки не должна быть больше 210, Поле может быть null

    /**
     * Создает новый экземпляр Location с заданными координатами и названием.
     *
     * @param x    Координата X местоположения.
     * @param y    Координата Y местоположения.
     * @param name Название местоположения. Может быть null.
     */
    public Location(int x, double y, String name) {
        this.x = x;
        this.y = y;
        this.name = name;
    }
    /**
     * Конструктор по умолчанию для использования в JAXB.
     */
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
