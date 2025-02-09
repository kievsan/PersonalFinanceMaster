package ru.mail.kievsan.frontend;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import ru.mail.kievsan.backend.controller.UserController;
import ru.mail.kievsan.backend.controller.UsersAdminController;
import ru.mail.kievsan.backend.exception.NotValidUserException;
import ru.mail.kievsan.backend.exception.VerifyUserPasswordException;
import ru.mail.kievsan.backend.model.ResponseStatus;
import ru.mail.kievsan.backend.model.dto.ResponseEntity;
import ru.mail.kievsan.backend.model.dto.Session;
import ru.mail.kievsan.backend.model.entity.User;
import ru.mail.kievsan.util.Utils;


@AllArgsConstructor
public class UsersAdminMenu {

    static private Session session;

    public static void start(Session currentSession) {
        session = currentSession;
        var controller = (UsersAdminController) session.getMvc().get("usersAdminController");
        var userController = (UserController) session.getMvc().get("userController");

        System.out.println("\n*****\nУправление Аккаунтами приложения \"Домашние финансы\"!");
        start: while (true) {
            try {
                switch (getMenuPoint()) {
                    case "1" -> {
                        var response = userController.update(getValidPasswordRequest());
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
        System.out.printf("\nUSER-Меню админа:\t\t\t( %s )\n----------------\n", session.getCurrentUser());
        System.out.println("1. Изменить пароль");
        System.out.println("2. Зарегистрировать пользователя");
        System.out.println("3. Заблокировать пользователя");
        System.out.println("4. Удалить пользователя");
        System.out.println("5. Найти пользователя");
        System.out.println("6. Список пользователей");
        System.out.println("7. Вернуться в главное меню");
        System.out.print("\nВыберите пункт меню (1-7): ");
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
        User user = session.getCurrentUser();

        return new User(user.getId(), newPassword, user.getRole(), user.getReg_date(), user.getStatus(), user.isDel());
    }

}
