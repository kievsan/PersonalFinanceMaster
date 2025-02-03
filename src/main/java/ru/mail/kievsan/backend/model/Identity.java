package ru.mail.kievsan.backend.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;


@Getter
@AllArgsConstructor
public abstract class Identity<K> implements Serializable {
    protected final K id;
}
