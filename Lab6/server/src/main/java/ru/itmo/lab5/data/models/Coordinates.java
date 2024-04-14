package ru.itmo.lab5.data.models;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serializable;


/**
 * Класс Coordinates представляет собой координаты в двумерном пространстве.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Coordinates implements Serializable {
    /**
     * Координата X.
     */
    private int x;
    /**
     * Координата Y. Должна быть задана (не может быть null).
     */
    private Double y; // Поле не может быть null

    /**
     * Конструктор класса Coordinates с параметрами для установки координат.
     *
     * @param x Координата по оси X.
     * @param y Координата по оси Y. Должна быть не null.
     */
    public Coordinates(int x, Double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Конструктор по умолчанию для использования в JAXB.
     */
    public Coordinates(){}

    /**
     * Проверяет, задана ли координата Y.
     *
     * @return true, если координата Y не null, иначе false.
     */
    public boolean validate() {
        return y != null;
    }


    /**
     * Возвращает строковое представление координат.
     *
     * @return Строковое представление объекта Coordinates.
     */
    @Override
    public String toString() {
        return "Coordinates{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
