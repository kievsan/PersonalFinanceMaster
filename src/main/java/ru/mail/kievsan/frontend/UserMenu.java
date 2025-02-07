package ru.mail.kievsan.frontend;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;

import ru.mail.kievsan.backend.controller.UserController;
import ru.mail.kievsan.backend.exception.NotValidUserException;
import ru.mail.kievsan.backend.exception.VerifyUserPasswordException;
import ru.mail.kievsan.backend.model.Status;
import ru.mail.kievsan.backend.model.dto.Session;
import ru.mail.kievsan.backend.model.entity.User;
import ru.mail.kievsan.util.Utils;


@AllArgsConstructor
public class UserMenu {

    static private final Gson gson = new Gson();
    static private Session session;

    public static void start(Session currentSession, UserController controller) {
        session = currentSession;
        System.out.println("\n*****\nУправление Аккаунтами Пользователей приложения \"Домашние финансы\"!");
        start: while (true) {
            System.out.println("\nUSER-Меню:");
            System.out.println("1. Изменить пароль");
            System.out.println("2. Вернуться в главное меню");
            System.out.print("\nВыберите пункт меню (1-2): ");

            try {
                String choice = session.getScanner().nextLine();
                switch (choice) {
                    case "1" -> {
                        var response = controller.update(getValidPasswordRequest());

                        if (response.getStatus() == Status.FAIL) throw new RuntimeException(response.getMessage());

                        System.out.println(response.getStatus() + "!");
                        System.out.println(response.getBody() == null ? "" : gson.toJson(response.getBody()));

                        session.setCurrentUser(response.getBody());
                    }
                    default -> {
                        if (SessionLoop.choiceCloseApp("Вернуться в главное меню?")) break start;
                    }
                }
            } catch (Exception e) {
                System.out.println("Произошла ошибка: " + e.getMessage());
            }
        }
    }

    private static User getValidPasswordRequest() throws VerifyUserPasswordException, NotValidUserException {
        System.out.print("Введите старый пароль: ");
        String oldPassword = session.getScanner().nextLine();

        Utils.verifyString(oldPassword, session.getCurrentUser().getPassword(), "password verify ERROR");

        System.out.printf("Введите новый пароль (от %d-ти символов): ", User.MIN_PASSWORD_LENGTH);
        String newPassword = session.getScanner().nextLine();

        return new User(session.getCurrentUser().getId(), newPassword, session.getCurrentUser().getRole());
    }

}
