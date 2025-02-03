package ru.mail.kievsan.backend.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Currency {
    CNY("Китайская йена"),
    EUR("Евро"),
    RUB("Российский рубль"),
    USD("Доллар США");

    private final String name;
}
