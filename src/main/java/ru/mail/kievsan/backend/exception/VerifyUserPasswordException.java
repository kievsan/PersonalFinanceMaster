package ru.mail.kievsan.backend.exception;

public class VerifyUserPasswordException extends RuntimeException {
    public VerifyUserPasswordException(String message) {
        super(message);
    }
}
