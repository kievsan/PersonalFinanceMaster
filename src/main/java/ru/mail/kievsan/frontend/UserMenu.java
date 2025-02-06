package ru.mail.kievsan.frontend;

import lombok.AllArgsConstructor;
import ru.mail.kievsan.backend.controller.UserController;
import ru.mail.kievsan.backend.model.dto.Session;

@AllArgsConstructor
public class UserMenu {

    public static void start(Session session, UserController controller) {
        System.out.println("\n*****\nУправление Аккаунтами Пользователей приложения \"Домашние финансы\"!");
        start: while (true) {
            System.out.println("USER-Меню:");
            System.out.println("1. Изменить пароль");
            System.out.println("2. Вернуться в главное меню");
            System.out.print("\nВыберите пункт меню (1-2): ");

            try {
                String choice = session.getScanner().nextLine();
                switch (choice) {
                    case "1" -> controller.start(session); //walletController.start();
                    default -> {
                        if (SessionLoop.choiceCloseApp("Вернуться в главное меню?")) break start;
                        continue;
                    }
                }
            } catch (Exception e) {
                System.out.println("Произошла ошибка: " + e.getMessage());
            }
        }
    }

}
