package ru.mail.kievsan.backend.repository.impl;

import ru.mail.kievsan.backend.config.PropertiesLoader;
import ru.mail.kievsan.backend.model.entity.User;
import ru.mail.kievsan.backend.repository.RepoImplFile;


public class UserRepoImplFile extends RepoImplFile<String, User> {

    @Override
    protected String getFilenameOfRepo() {
        return PropertiesLoader.loadUsersRepoName();
    }
}
