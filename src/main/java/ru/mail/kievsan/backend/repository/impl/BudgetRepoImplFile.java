package ru.mail.kievsan.backend.repository.impl;

import ru.mail.kievsan.backend.config.PropertiesLoader;
import ru.mail.kievsan.backend.model.entity.BudgetCategory;
import ru.mail.kievsan.backend.repository.RepoImplFile;

public class BudgetRepoImplFile extends RepoImplFile<String, BudgetCategory> {

    @Override
    protected String getFilenameOfRepo() {
        return PropertiesLoader.loadCategoriesRepoName();
    }
}
