package ru.mail.kievsan.backend.controller.impl;

import ru.mail.kievsan.backend.controller.ControllerImpl;
import ru.mail.kievsan.backend.model.dto.ResponseEntity;
import ru.mail.kievsan.backend.model.entity.BudgetCategory;
import ru.mail.kievsan.backend.service.impl.BudgetServiceImplFile;


public class BudgetController extends ControllerImpl<String, BudgetCategory, BudgetServiceImplFile> {

    public BudgetController(BudgetServiceImplFile budgetService) {
        super(budgetService);
    }

    @Override
    public ResponseEntity<BudgetCategory> register(BudgetCategory request) {
        return null;
    }

    @Override
    public ResponseEntity<BudgetCategory> update(BudgetCategory request) {
        return null;
    }
}
