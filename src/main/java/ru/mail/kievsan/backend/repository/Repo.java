package ru.mail.kievsan.backend.repository;

import java.io.Closeable;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface Repo<K, T> extends Closeable {

    Optional<T> save(T entity);

    Optional<T> remove(T entity);

    Optional<T> getById(K id);

    boolean existsById(K id);

    List<T> getAll();

    int size();
}
