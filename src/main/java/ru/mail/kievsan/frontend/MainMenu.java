package ru.mail.kievsan.frontend;

import com.fasterxml.jackson.core.JsonProcessingException;

import ru.mail.kievsan.backend.controller.impl.UserController;
import ru.mail.kievsan.backend.model.ResponseStatus;
import ru.mail.kievsan.backend.model.dto.ResponseEntity;
import ru.mail.kievsan.backend.model.dto.Session;
import ru.mail.kievsan.util.Utils;

public class MainMenu {

    static Session session;

    public static void start(Session currentSession) {
        session = currentSession;
        var userController = (UserController) session.getMvc().get("userController");

        System.out.println("\n*****\nГЛАВНОЕ МЕНЮ приложения \"Домашние финансы\"!");
        start: while (true) {
            try {
                switch (getMenuPoint()) {
                    case "1" -> UserMenu.start(session); //walletController;
                    case "2" -> UserMenu.start(session); //budgetController;
                    case "3" -> UserMenu.start(session); //transactionController;
                    case "4" -> {
                        switch (session.getCurrentUser().getRole()) {
                            case USER -> UserMenu.start(session);
                            default -> UsersAdminMenu.start(session);
                        }
                    }
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

    private static String getMenuPoint() {
        System.out.printf("\nMAIN-Меню:\t\t\t( %s )\n", session.getCurrentUser());
        System.out.println("1. Управление кошельками");
        System.out.println("2. Управление категориями и бюджетами");
        System.out.println("3. Управление транзакциями");
        System.out.println("4. Управление аккаунтами");
        System.out.println("5. Выйти из аккаунта");
        System.out.print("\nВыберите пункт меню (1-5): ");
        return session.getScanner().nextLine();
    }

    private static void processResponse(ResponseEntity<?> response) throws RuntimeException, JsonProcessingException {
        if (response.getStatus() == ResponseStatus.FAIL) throw new RuntimeException(response.getMessage());
        System.out.println(response.getStatus() + "!");
        System.out.println(response.getBody() == null ? "" : Utils.toJackson(response.getBody()));
    }
}
