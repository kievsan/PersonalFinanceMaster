package ru.mail.kievsan.frontend;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import ru.mail.kievsan.backend.controller.impl.BudgetController;
import ru.mail.kievsan.backend.exception.NotValidUserException;
import ru.mail.kievsan.backend.exception.VerifyUserPasswordException;
import ru.mail.kievsan.backend.model.ResponseStatus;
import ru.mail.kievsan.backend.model.dto.ResponseEntity;
import ru.mail.kievsan.backend.model.dto.Session;
import ru.mail.kievsan.backend.model.entity.BudgetCategory;
import ru.mail.kievsan.backend.repository.impl.BudgetRepoImplFile;
import ru.mail.kievsan.util.Utils;


@AllArgsConstructor
public class BudgetMenu {

    static private Session session;

    public static void start(Session currentSession) {
        session = currentSession;
        var controller = (BudgetController) session.getMvc().get("budgetController");

        System.out.printf("\n*****\nУправление бюджетом '%s' приложения \"Домашние финансы\"!\n",
                session.getCurrentUser().getId());
        start: while (true) {
            try {
                switch (getMenuPoint()) {
                    case "1" -> {
                        var response = controller.register(getValidCategoryRequest());
                        processResponse(response);
                    }
                    case "2" -> {
                        var response = controller.update(getValidCategoryRequest());
                        processResponse(response);
                    }
                    default -> {
                        if (SessionLoop.choiceCloseApp("Вернуться в главное меню?")) break start;
                    }
                }
            } catch (Exception e) {
                System.out.println("Произошла ошибка: " + e.getMessage());
            }
        }
        close();
    }

    private static String getMenuPoint() {
        System.out.printf("\nBudget-Меню:\t\t\t( %s )\n", session.getCurrentUser());
        System.out.println("1. Зарегистрировать категорию");
        System.out.println("2. Редактировать категорию");
        System.out.println("3. Удалить категорию");
        System.out.println("4. Список категорий");
        System.out.println("5. Вернуться в главное меню");
        System.out.print("\nВыберите пункт меню (1-5): ");
        return session.getScanner().nextLine();
    }

    public static void close() {
        var repo = (BudgetRepoImplFile) session.getMvc().get("budgetRepo");
        repo.close();
        System.out.println("SUCCESS!");
    }

    private static void processResponse(ResponseEntity<?> response) throws RuntimeException, JsonProcessingException {
        if (response.getStatus() == ResponseStatus.FAIL) throw new RuntimeException(response.getMessage());
        System.out.println(response.getStatus() + "!");
        System.out.println(response.getBody() == null ? "" : Utils.toJackson(response.getBody()));
    }

    private static BudgetCategory getValidCategoryRequest() throws VerifyUserPasswordException, NotValidUserException {
        System.out.printf("Введите бюджетную категорию (%d-%d символов): ",
                BudgetCategory.MIN_NAME_LENGTH, BudgetCategory.MAX_NAME_LENGTH);
        String name = session.getScanner().nextLine();

        System.out.print("Введите бюджетный лимит: ");
        double limit = session.getScanner().nextDouble();
        session.getScanner().nextLine();

        var category = new BudgetCategory(session.getCurrentUser().getId(), false, name, limit);
        System.out.println("---------\n" + category);
        return category;

//        return new BudgetCategory(session.getCurrentUser().getId(), false, name, limit);
    }

}
