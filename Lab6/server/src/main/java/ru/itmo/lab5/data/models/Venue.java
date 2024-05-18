package ru.itmo.lab5.data.models;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.*;

import java.io.Serializable;

/**
 * Класс, представляющий место проведения.
 * Содержит уникальный идентификатор, название, вместимость, тип и адрес.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Venue implements Serializable {
    private static long idCounter = 0;
    private long id; // Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; // Поле не может быть null, Строка не может быть пустой
    private int capacity; // Значение поля должно быть больше 0
    private VenueType type; // Поле может быть null
    private Address address; // Поле не может быть null

    /**
     * Конструктор для создания объекта места проведения с заданными параметрами.
     *
     * @param name Название места проведения.
     * @param capacity Вместимость места проведения.
     * @param type Тип места проведения.
     * @param address Адрес места проведения.
     */
    public Venue(String name, int capacity, VenueType type, Address address) {
        this.id = ++idCounter;
        this.name = name;
        this.capacity = capacity;
        this.type = type;
        this.address = address;
    }

    /**
     * Обновляет счетчик ID на заданное значение.
     *
     * @param nextIdValue Новое значение счетчика ID.
     */
    public static void updateNextId(long nextIdValue) {
        idCounter = nextIdValue;
    }
}
