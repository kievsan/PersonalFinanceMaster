package ru.mail.kievsan.backend.repository.impl;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.Getter;
import ru.mail.kievsan.backend.conf.PropertiesLoader;
import ru.mail.kievsan.backend.model.Identity;
import ru.mail.kievsan.backend.repository.Repo;

import java.io.File;
import java.io.IOException;
import java.util.*;


public abstract class RepoImplFile <K, T extends Identity<K>> implements Repo<K, T> {

    private final ObjectMapper objectMapper = new ObjectMapper();
    final File REPO_FILE = new File(PropertiesLoader.loadDataSourcePath() + "/" + getFilenameOfRepo());

    private final Map<K, T> store = load();


    protected abstract String getFilenameOfRepo();

    private Map<K, T> load() {
        try {
            if (REPO_FILE.createNewFile()) {
                return new HashMap<>();
            }
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return objectMapper.readerForMapOf(HashMap.class).readValue(REPO_FILE);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

   public void upload() {
        try {
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
            objectMapper.writeValue(REPO_FILE, store);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @JsonAnySetter
    public Optional<T> save(T entity) {
        return Optional.ofNullable(store.put(entity.getId(), entity));
    }

    @Override
    public Optional<T> remove(T entity) {
        return Optional.ofNullable(store.remove(entity.getId()));
    }

    @Override
    public Optional<T> getById(K id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public boolean existsById(K id) {
        return store.containsKey(id);
    }

    @Override
    public List<T> getAll() {
        return store.values().stream().toList();
    }

    @Override
    public int size() {
        return store.size();
    }

    @Override
    public void close() throws IOException {

    }
    protected String getEntityName() {
        return this.getClass().getSimpleName();
    }
}
