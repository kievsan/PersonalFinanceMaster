package ru.mail.kievsan.backend.controller;

import ru.mail.kievsan.backend.config.MVC;
import ru.mail.kievsan.backend.model.Identity;
import ru.mail.kievsan.backend.model.dto.ResponseEntity;

public interface Controller<K, T extends Identity<K>> extends MVC {

    ResponseEntity<T> register(T request);
    ResponseEntity<T> update(T request);
}
