package ru.itmo.lab5.data.models;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serializable;
import java.util.Date;


/**
 * Класс Ticket представляет билет, связанный с определенным событием или местоположением.
 * Билет имеет уникальный идентификатор, название, координаты места, дату создания, цену, тип и место проведения.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Ticket implements Comparable<Ticket>, Serializable {
    private static int nextId = 0;
    private int id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private Date creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private Long price; //Поле может быть null, Значение поля должно быть больше 0
    private TicketType type; //Поле может быть null
    private Venue venue; //Поле может быть null


    /**
     * Создает новый экземпляр билета с указанными параметрами.
     *
     * @param name        Название билета.
     * @param coordinates Координаты места.
     * @param price       Цена билета.
     * @param type        Тип билета.
     * @param venue       Место проведения.
     */
    public Ticket(String name, Coordinates coordinates, Long price, TicketType type, Venue venue) {
        this.id = ++nextId;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = new Date();
        this.price = price;
        this.type = type;
        this.venue = venue;
    }

    /**
     * Конструктор по умолчанию для JAXB.
     */
    public Ticket(){}

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getPrice() {
        return price;
    }

    public Venue getVenue() {
        return venue;
    }

    /**
     * Проверяет валидность билета.
     * Билет считается валидным, если все его поля, кроме цены, типа и места проведения, заданы корректно.
     *
     * @return true, если билет валиден, иначе false.
     */
    public boolean validate() {
        if (name == null || name.isEmpty()) return false;
        if (coordinates == null || !coordinates.validate()) return false;
        // Проверка price на null, price может быть null
        return price == null || price > 0;
        // Поле type и venue могут быть null
    }

    /**
     * Возвращает строковое представление билета.
     * Включает в себя все поля билета: идентификатор, название, координаты, дату создания,
     * цену, тип и место проведения.
     *
     * @return Строковое представление билета.
     */
    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", coordinates=" + coordinates +
                ", creationDate=" + creationDate +
                ", price=" + price +
                ", type=" + (type != null ? type : "N/A") +
                ", venue=" + venue +
                '}';
    }

    /**
     * Сравнивает этот билет с другим билетом для упорядочения.
     * Сравнение производится сначала по цене, затем — по дате создания.
     *
     * @param o Другой объект билета для сравнения.
     * @return Отрицательное целое число, ноль или положительное целое число,
     *         если этот билет меньше, равен или больше указанного объекта.
     */
    @Override
    public int compareTo(Ticket o) {
        // Сначала сравниваем по цене
        if (this.price != null && o.price != null) {
            int priceCompare = this.price.compareTo(o.price);
            if (priceCompare != 0) {
                return priceCompare;
            }
        } else if (this.price == null && o.price != null) {
            return -1; // Считаем, что null меньше любого значения
        } else if (this.price != null) {
            return 1; // Считаем, что любое значение больше null
        }

        // Затем сравниваем по дате создания, если цены равны
        // Считаем, что ранее созданный билет меньше
        return this.creationDate.compareTo(o.creationDate);
    }

    /**
     * Обновляет статический счетчик ID для билетов на указанное значение.
     * Это может быть использовано для синхронизации счетчика с внешним источником данных.
     *
     * @param nextIdValue Новое значение для счетчика ID билетов.
     */
    public static void updateNextId(int nextIdValue) {
        nextId = nextIdValue;
    }
}