package ru.itmo.lab5.exceptions;

public class CollectionOperationException extends RuntimeException {
    public CollectionOperationException(String message) {
        super(message);
    }
}