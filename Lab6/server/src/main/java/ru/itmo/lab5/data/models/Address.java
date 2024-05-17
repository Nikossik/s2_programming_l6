package ru.itmo.lab5.data.models;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Класс Address представляет собой адрес в системе управления билетами.
 * Содержит информацию об улице, почтовом индексе и городе.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Setter
@Getter
public class Address implements Serializable {
    /**
     * Улица адреса.
     */
    private String street;
    /**
     * Почтовый индекс адреса.
     */
    private String zipCode;
    /**
     * Город, в котором находится адрес.
     */
    private Location town;

    /**
     * Конструктор класса Address с параметрами для создания полного адреса.
     *
     * @param street Улица адреса.
     * @param zipCode Почтовый индекс адреса.
     * @param town Город адреса.
     */
    public Address(String street, String zipCode, Location town) {
        this.street = street;
        this.zipCode = zipCode;
        this.town = town;
    }
    /**
     * Конструктор по умолчанию для JAXB.
     */
    public Address(){}

    /**
     * Представляет информацию об адресе в виде строки.
     *
     * @return Строковое представление адреса.
     */
    @Override
    public String toString() {
        return "Address{" +
                "street='" + street + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", town=" + town +
                '}';
    }
}
