package ru.mail.kievsan.backend.exception;

public class NotValidUserException extends RuntimeException {
    public NotValidUserException(String message) {
        super(message);
    }
}
