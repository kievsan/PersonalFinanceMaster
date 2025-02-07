package ru.mail.kievsan.backend.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;


@Getter
public abstract class Identity<K> implements Serializable {
    protected final K id;

    public Identity(K id) {
        this.id = id;
        validateId();
    }

    public abstract void validateId();
}
