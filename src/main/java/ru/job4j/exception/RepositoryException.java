package ru.job4j.exception;

/**
 * Использовать при ошибках в работе с БД
 */
public class RepositoryException extends RuntimeException {
    public RepositoryException(String message) {
        super(message);
    }
}