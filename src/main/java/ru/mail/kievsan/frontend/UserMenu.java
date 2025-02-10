package ru.mail.kievsan.frontend;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;

import ru.mail.kievsan.backend.controller.UserController;
import ru.mail.kievsan.backend.exception.NotValidUserException;
import ru.mail.kievsan.backend.exception.VerifyUserPasswordException;
import ru.mail.kievsan.backend.model.ResponseStatus;
import ru.mail.kievsan.backend.model.dto.ResponseEntity;
import ru.mail.kievsan.backend.model.dto.Session;
import ru.mail.kievsan.backend.model.entity.User;
import ru.mail.kievsan.util.Utils;


@AllArgsConstructor
public class UserMenu {

    static private Session session;

    public static void start(Session currentSession) {
        session = currentSession;
        var controller = (UserController) session.getMvc().get("userController");

        System.out.printf("\n*****\nУправление аккаунтом '%s' приложения \"Домашние финансы\"!\n",
                session.getCurrentUser().getId());
        start: while (true) {
            try {
                switch (getMenuPoint()) {
                    case "1" -> {
                        var response = controller.update(getValidPasswordRequest());
                        processResponse(response);
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

    private static String getMenuPoint() {
        System.out.printf("\nUSER-Меню:\t\t\t( %s )\n", session.getCurrentUser());
        System.out.println("1. Изменить пароль");
        System.out.println("2. Вернуться в главное меню");
        System.out.print("\nВыберите пункт меню (1-2): ");
        return session.getScanner().nextLine();
    }

    private static void processResponse(ResponseEntity<?> response) throws RuntimeException, JsonProcessingException {
        if (response.getStatus() == ResponseStatus.FAIL) throw new RuntimeException(response.getMessage());
        System.out.println(response.getStatus() + "!");
        System.out.println(response.getBody() == null ? "" : Utils.toJackson(response.getBody()));
        // System.out.println(response.getBody() == null ? "" : Utils.toJson(response.getBody()));
    }

    private static User getValidPasswordRequest() throws VerifyUserPasswordException, NotValidUserException {
        System.out.print("Введите старый пароль: ");
        String oldPassword = session.getScanner().nextLine();

        Utils.verifyString(oldPassword, session.getCurrentUser().getPassword(), "password verify ERROR");

        System.out.printf("Введите новый пароль (от %d-ти символов): ", User.MIN_PASSWORD_LENGTH);
        String newPassword = session.getScanner().nextLine();

        return new User(session.getCurrentUser(), newPassword);
    }

}
