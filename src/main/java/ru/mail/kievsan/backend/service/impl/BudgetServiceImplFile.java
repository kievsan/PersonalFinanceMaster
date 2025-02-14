package ru.mail.kievsan.backend.service.impl;

import ru.mail.kievsan.backend.model.entity.BudgetCategory;
import ru.mail.kievsan.backend.repository.impl.BudgetRepoImplFile;
import ru.mail.kievsan.backend.repository.impl.UserRepoImplFile;
import ru.mail.kievsan.backend.service.ServiceImplFile;

import java.util.Comparator;
import java.util.List;


public class BudgetServiceImplFile extends ServiceImplFile<String, BudgetCategory> {

    final UserRepoImplFile userRepo;

    public BudgetServiceImplFile(BudgetRepoImplFile repo, UserRepoImplFile userRepo) {
        super(repo);
        this.userRepo = userRepo;
    }


    @Override
    public BudgetCategory register(BudgetCategory category) throws RuntimeException {
        List<BudgetCategory> userBudget = getBudgetByUserId(category.getUserId());

        printRequest(category);
        printUserBudget(userBudget);

        if (budgetExistsCategoryByName(category.getName(), userBudget)) {
            throw new RuntimeException("The category name is already in use for this user budget, " +
                    "registration is not possible!");
        }
        signup(category);
        return category;
    }

    @Override
    public void signup(BudgetCategory newCategory) throws RuntimeException {
        String msg = String.format("New category for budget of user '%s'", newCategory.getUserId());
        try {
            repo.save(newCategory);
            var category = repo.getById(newCategory.getId()).orElseThrow();
            msg += String.format(" signup:\t%s", category);
            System.out.println(msg);
        } catch (RuntimeException e) {
            msg += " was not signup:\t%s" + e.getMessage();
            throw new RuntimeException(msg);
        }
    }

    @Override
    public BudgetCategory update(BudgetCategory category) {
        return null;
    }

    public List<BudgetCategory> getBudgetByUserId(String id) {
        return repo.getAll().stream()
                .filter(category -> category.getUserId().equals(id))
                .sorted(new CategoryComparator())
                .toList();
    }


    public boolean budgetExistsCategoryByName(String categoryName, List<BudgetCategory> userBudget) {
        return userBudget.stream().anyMatch(category -> category.getName().equals(categoryName));
    }

    public void printRequest(BudgetCategory category) {
        System.out.printf("\nRequest registration category for budget of user '%s'\n", category.getUserId());
        System.out.println("------------------------------------------------\n" + category.toShortString());
        System.out.println("------------------------------------------------\n");
    }

    public void printUserBudget(List<BudgetCategory> userBudget) {
        for (BudgetCategory category : userBudget) {
            System.out.println(category.toShortString());
        }
        System.out.println("------------------------------------------------\n");
    }

    static class CategoryComparator implements Comparator<BudgetCategory> {
        public int compare(BudgetCategory a, BudgetCategory b){
            return a.getName().compareTo(b.getName());
        }
    }
}
