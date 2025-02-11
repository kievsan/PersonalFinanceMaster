package ru.mail.kievsan.backend.repository.impl;

import com.fasterxml.jackson.databind.ObjectMapper;

import ru.mail.kievsan.backend.config.PropertiesLoader;
import ru.mail.kievsan.backend.model.ActivityStatus;
import ru.mail.kievsan.backend.model.Identity;
import ru.mail.kievsan.backend.model.entity.User;
import ru.mail.kievsan.backend.repository.Repo;
import ru.mail.kievsan.util.Utils;

import java.io.File;
import java.util.*;


public abstract class RepoImplFile <K, T extends Identity<K>> implements Repo<K, T> {

    private final ObjectMapper jacksonMapper = Utils.prettyJackson;
    final File REPO_FILE = new File(PropertiesLoader.loadDataSourcePath() + "/" + getFilenameOfRepo());

    private final Map<K, T> store = load();

    {
        System.out.printf("\nзагружаю...\n%s\n", store);
    }

    protected abstract String getFilenameOfRepo();

    private HashMap<K, T> load() {
        try {
            if (REPO_FILE.createNewFile()) {
                return new HashMap<>();
            }
            return jacksonMapper.readerForMapOf(User.class).readValue(REPO_FILE);
        } catch (Exception e) {
            return new HashMap<>();
        }
    }

   public void upload() {
        try {
            jacksonMapper.writerFor(HashMap.class).writeValue(REPO_FILE, store);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
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
    public void close() {
        System.out.println("сохраняю данные...");
        this.upload();
        System.out.printf("\n%s\n\n", this.getAll());
    }

    protected String getEntityName() {
        return this.getClass().getSimpleName();
    }
}
