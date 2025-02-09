package ru.mail.kievsan.frontend;

import com.fasterxml.jackson.core.JsonProcessingException;
import ru.mail.kievsan.backend.controller.UserController;
import ru.mail.kievsan.backend.model.ResponseStatus;
import ru.mail.kievsan.backend.model.dto.ResponseEntity;
import ru.mail.kievsan.backend.model.dto.Session;
import ru.mail.kievsan.util.Utils;

public class MainMenu {

    public static void start(Session session, UserController userController) {
        System.out.println("\n*****\nГЛАВНОЕ МЕНЮ приложения \"Домашние финансы\"!");
        start: while (true) {
            System.out.println("\nMAIN-Меню:");
            System.out.println("1. Управление кошельками");
            System.out.println("2. Управление категориями и бюджетами");
            System.out.println("3. Управление транзакциями");
            System.out.println("4. Управление аккаунтом");
            System.out.println("5. Администрирование");
            System.out.println("6. Выйти из аккаунта");
            System.out.print("\nВыберите пункт меню (1-6): ");

            try {
                String choice = session.getScanner().nextLine();
                switch (choice) {
                    case "1" -> UserMenu.start(session, userController); //walletController;
                    case "2" -> UserMenu.start(session, userController); //budgetController;
                    case "3" -> UserMenu.start(session, userController); //transactionController;
                    case "4" -> UserMenu.start(session, userController); //adminController;
                    case "5" -> UserMenu.start(session, userController);
                    default -> {
                        if (SessionLoop.choiceCloseApp("Выйти из аккаунта?")) {
                            var response = userController.logout(session.getCurrentUser());
                            processResponse(response);
                            break start;
                        }
                        //continue;
                    }
                }
            } catch (Exception e) {
                System.out.println("Произошла ошибка: " + e.getMessage());
            }
        }
    }

    private static void processResponse(ResponseEntity<?> response) throws RuntimeException, JsonProcessingException {
        if (response.getStatus() == ResponseStatus.FAIL) throw new RuntimeException(response.getMessage());
        System.out.println(response.getStatus() + "!");
        System.out.println(response.getBody() == null ? "" : Utils.toJackson(response.getBody()));
    }
}
