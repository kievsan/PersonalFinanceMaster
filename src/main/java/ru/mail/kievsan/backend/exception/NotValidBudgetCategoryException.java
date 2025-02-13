package ru.mail.kievsan.backend.exception;

public class NotValidBudgetCategoryException extends RuntimeException {
    public NotValidBudgetCategoryException(String message) {
        super(message);
    }
}
