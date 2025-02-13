package ru.mail.kievsan.backend.service.impl;

import ru.mail.kievsan.backend.model.entity.BudgetCategory;
import ru.mail.kievsan.backend.repository.impl.BudgetRepoImplFile;
import ru.mail.kievsan.backend.repository.impl.UserRepoImplFile;
import ru.mail.kievsan.backend.service.ServiceImplFile;


public class BudgetServiceImplFile extends ServiceImplFile<String, BudgetCategory> {

    public BudgetServiceImplFile(BudgetRepoImplFile repo, UserRepoImplFile userRepo) {
        super(repo);
    }


    @Override
    public BudgetCategory register(BudgetCategory category) {
        return null;
    }

    @Override
    public void signup(BudgetCategory newCategory) {

    }

    @Override
    public BudgetCategory update(BudgetCategory category) {
        return null;
    }
}
