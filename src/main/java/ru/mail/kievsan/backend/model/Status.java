package ru.mail.kievsan.backend.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.mail.kievsan.util.Utils;

@Getter
@RequiredArgsConstructor
public enum Status {
    OK("OK"),
    FAIL("FAIL");

    private final String name;

    @Override
    public String toString() {
        return Utils.capitalize(name.toLowerCase());
    }
}
