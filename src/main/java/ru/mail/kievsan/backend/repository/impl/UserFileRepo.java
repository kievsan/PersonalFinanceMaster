package ru.mail.kievsan.backend.repository.impl;

import ru.mail.kievsan.backend.config.PropertiesLoader;
import ru.mail.kievsan.backend.model.entity.User;


public class UserFileRepo extends RepoImplFile<String, User> {

    @Override
    protected String getFilenameOfRepo() {
        return PropertiesLoader.loadUsersRepoName();
    }
}
