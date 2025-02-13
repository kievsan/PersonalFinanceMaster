package ru.mail.kievsan.backend.service;

import ru.mail.kievsan.backend.config.MVC;
import ru.mail.kievsan.backend.model.Identity;


public interface Service<K, T extends Identity<K>> extends MVC {
    T register(T entity);
    void signup(T newEntity);
    T update(T entity);
}
