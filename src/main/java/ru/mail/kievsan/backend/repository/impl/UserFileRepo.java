package ru.mail.kievsan.backend.repository.impl;

import ru.mail.kievsan.backend.config.PropertiesLoader;
import ru.mail.kievsan.backend.model.ActivityStatus;
import ru.mail.kievsan.backend.model.entity.User;

import java.util.List;


public class UserFileRepo extends RepoImplFile<String, User> {

    @Override
    protected String getFilenameOfRepo() {
        return PropertiesLoader.loadUsersRepoName();
    }

    public List<User> getUsersByStatus(ActivityStatus status) {
        return getAll().stream().filter(user -> user.getStatus() == status).toList();
    }
}
