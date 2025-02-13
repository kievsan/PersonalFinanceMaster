package ru.mail.kievsan.backend.controller;

import lombok.AllArgsConstructor;
import ru.mail.kievsan.backend.model.Identity;
import ru.mail.kievsan.backend.service.ServiceImplFile;


@AllArgsConstructor
public abstract class ControllerImpl <K, T extends Identity<K>, S extends ServiceImplFile<K, T>> implements Controller<K, T> {

    protected final S service;
}
