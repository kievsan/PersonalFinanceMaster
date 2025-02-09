package ru.mail.kievsan.backend.repository;

import ru.mail.kievsan.backend.config.MVC;

import java.io.Closeable;
import java.util.List;
import java.util.Optional;

public interface Repo<K, T> extends Closeable, MVC {

    Optional<T> save(T entity);

    Optional<T> remove(T entity);

    Optional<T> getById(K id);

    boolean existsById(K id);

    List<T> getAll();

    int size();
}
