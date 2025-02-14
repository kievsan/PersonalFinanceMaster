package ru.mail.kievsan.backend.controller.impl;

import ru.mail.kievsan.backend.controller.ControllerImpl;
import ru.mail.kievsan.backend.model.ResponseStatus;
import ru.mail.kievsan.backend.model.dto.ResponseEntity;
import ru.mail.kievsan.backend.model.entity.BudgetCategory;
import ru.mail.kievsan.backend.service.impl.BudgetServiceImplFile;


public class BudgetController extends ControllerImpl<String, BudgetCategory, BudgetServiceImplFile> {

    public BudgetController(BudgetServiceImplFile budgetService) {
        super(budgetService);
    }

    @Override
    public ResponseEntity<BudgetCategory> register(BudgetCategory request) {
        try{
            var response = service.register(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(ResponseStatus.FAIL, e.getMessage());
        }
    }

    @Override
    public ResponseEntity<BudgetCategory> update(BudgetCategory request) {
        try{
            var response = service.update(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(ResponseStatus.FAIL, e.getMessage());
        }
    }
}
