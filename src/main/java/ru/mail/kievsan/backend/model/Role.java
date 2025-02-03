package ru.mail.kievsan.backend.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    ADMIN("ADMIN"),
    USER("USER");

    private final String name;

    @Override
    public String toString() {
        return name.toLowerCase();
    }
}
