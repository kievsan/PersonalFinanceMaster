package ru.mail.kievsan.backend.repository.impl;

import ru.mail.kievsan.backend.config.PropertiesLoader;
import ru.mail.kievsan.backend.model.entity.BudgetCategory;
import ru.mail.kievsan.backend.repository.RepoImplFile;

import java.util.Optional;

public class BudgetRepoImplFile extends RepoImplFile<String, BudgetCategory> {

    @Override
    protected String getFilenameOfRepo() {
        return PropertiesLoader.loadCategoriesRepoName();
    }

    public Optional<BudgetCategory> getByUserId(String id) {
        return getById(id);
    }

    public Optional<BudgetCategory> getByStrategyId(String id) {
        return store.values().stream().filter(category -> category.getId().equals(id)).findFirst();
    }

}
