package ru.mail.kievsan.backend.service;

import ru.mail.kievsan.backend.model.Identity;
import ru.mail.kievsan.backend.repository.RepoImplFile;


public abstract class ServiceImplFile <K, T extends Identity<K>> implements Service<K, T> {

    protected final RepoImplFile<K, T> repo;

    public ServiceImplFile(RepoImplFile<K, T> repo) {
        this.repo = repo;
    }
}
