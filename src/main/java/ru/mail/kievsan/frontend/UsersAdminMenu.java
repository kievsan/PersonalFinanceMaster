package ru.mail.kievsan.frontend;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;

import ru.mail.kievsan.backend.config.PropertiesLoader;
import ru.mail.kievsan.backend.controller.UsersAdminController;
import ru.mail.kievsan.backend.exception.NotValidUserException;
import ru.mail.kievsan.backend.exception.VerifyUserPasswordException;
import ru.mail.kievsan.backend.model.ActivityStatus;
import ru.mail.kievsan.backend.model.ResponseStatus;
import ru.mail.kievsan.backend.model.Role;
import ru.mail.kievsan.backend.model.dto.ResponseEntity;
import ru.mail.kievsan.backend.model.dto.Session;
import ru.mail.kievsan.backend.model.dto.admin.updateUserStatusRequest;
import ru.mail.kievsan.backend.model.entity.User;
import ru.mail.kievsan.util.Utils;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


@AllArgsConstructor
public class UsersAdminMenu {

    static private Session session;

    public static void start(Session currentSession) {
        session = currentSession;
        var controller = (UsersAdminController) session.getMvc().get("usersAdminController");

        System.out.println("\n*****\nУправление Аккаунтами приложения \"Домашние финансы\"!");
        start: while (true) {
            try {
                switch (getMenuPoint()) {
                    case "1" -> {
                        var response = controller.update(getValidPasswordRequest());
                        processResponse(response);
                        session.setCurrentUser(response.getBody());
                    }
                    case "2" -> {
                        var response = controller.register(getValidUser());
                        processResponse(response);
                    }
                    case "3" -> {
                        var request = new updateUserStatusRequest(getValidLogin(), getValidUserStatus());
                        System.out.println(request); ////////////////////////////
                        var response = controller.updateUserStatus(request, session.getCurrentUser());
                        processResponse(response);
                    }
                    case "4" -> {
                        var response = controller.findUserById(getValidLogin());
                        processResponse(response);
                    }
                    case "5" -> {
                        var response = controller.getUserList(getUserListFilter());
                        processListResponse(response);
                    }
                    case "6" -> {
                        var response = controller.getUsersNumber(getUserListFilter());
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
    }

    private static String getMenuPoint() {
        System.out.printf("\nUSER-Меню админа:\t\t\t( %s )\n----------------\n", session.getCurrentUser());
        System.out.println("1. Изменить свой пароль");
        System.out.println("2. Зарегистрировать пользователя");
        System.out.println("3. Сменить статус пользователя");
        System.out.println("4. Найти пользователя");
        System.out.println("5. Список пользователей");
        System.out.println("6. Кол-во пользователей");
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

    private static void processListResponse(ResponseEntity<List<User>> response) throws RuntimeException, JsonProcessingException {
        if (response.getStatus() == ResponseStatus.FAIL) throw new RuntimeException(response.getMessage());
        System.out.println(response.getStatus() + "!");

        System.out.print("Вывести результат в (1-Файл, 2-Консоль): ");
        if (Objects.equals(session.getScanner().nextLine().trim(), "1")) { // в Файл
            System.out.print("сохраняю данные в файл...");
            System.out.println(uploadUserList(response.getBody()));
            return;
        }
        System.out.println(response.getBody() == null ? "" : Utils.prettyJackson
                .writerFor(List.class).writeValueAsString(response.getBody()));
    }

    private static User getValidPasswordRequest() throws VerifyUserPasswordException, NotValidUserException {
        System.out.print("Введите старый пароль: ");
        String oldPassword = session.getScanner().nextLine();

        Utils.verifyString(oldPassword, session.getCurrentUser().getPassword(), "password verify ERROR");

        System.out.printf("Введите новый пароль (от %d-ти символов): ", User.MIN_PASSWORD_LENGTH);
        String newPassword = session.getScanner().nextLine();

        return new User(session.getCurrentUser(), newPassword);
    }

    public static String getValidLogin() throws NotValidUserException {
        System.out.printf("Введите логин (от %d-х символов): ", User.MIN_LOGIN_LENGTH);
        String login = session.getScanner().nextLine();
        new User(login, "0".repeat(User.MIN_PASSWORD_LENGTH));
        return login;
    }

    public static User getValidUser() throws NotValidUserException {
        System.out.printf("Введите логин (от %d-х символов): ", User.MIN_LOGIN_LENGTH);
        String login = session.getScanner().nextLine();
        System.out.printf("Введите пароль (от %d-ти символов): ", User.MIN_PASSWORD_LENGTH);
        String password = session.getScanner().nextLine();
        System.out.print("Выберите роль (1-ADMIN, 2-USER): ");
        Role role = Objects.equals(session.getScanner().nextLine().trim(), "1") ? Role.ADMIN : Role.USER;
        return new User(login, password, role);
    }

    public static ActivityStatus getValidUserStatus() {
        var userStatusValuesMap = getUserStatusValuesMap();
        //System.out.println(userStatusValuesMap); ////////////////////////
        ActivityStatus defaultStatus = userStatusValuesMap.values().stream().findFirst()
                .orElseThrow(() -> new RuntimeException("User statuses are not set!"));

        System.out.printf("Введите status (%s): ", getUserStatusValuesStr());
        var rawStatus = session.getScanner().nextLine() + " ";

        var status = userStatusValuesMap.get(rawStatus.substring(0, 1));
        return status == null ? defaultStatus : status;
    }

    public static String getUserStatusValuesStr() {
        List<String> values = new ArrayList<>();
        for (var value : ActivityStatus.values()) values.add(value.name().charAt(0) + "-" + value);
        return String.join(", ", values);
    }

    public static Map<String, ActivityStatus> getUserStatusValuesMap() {
        Map<String, ActivityStatus> values = new HashMap<>();
        for (var value : ActivityStatus.values()) values.put(value.name().substring(0, 1), value);
        return values;
    }

    public static String getUserListFilter() {
        System.out.println("\nВведите фильтр для списка (F-full, A-active, L-locked, D-deleted)");
        System.out.print("или их комбинации (AL, AD, LD): ");
        return getValidUserListFilter(session.getScanner().nextLine());
    }

    public static String getValidUserListFilter(String filter) {
        String regex = "^(?=.*[AFDL])";     // Хотя бы одна большая латинская буква в начале
        String regex2 = "[ADL]";    // Ещё только большие латинские буквы, КРОМЕ 'F'
        String regex3 = "{1,2}"; // Длина в интервале от MIN до MAX
        regex += regex2 + regex3 + "$";
        if (!filter.matches(regex)) return "F";
        if ( filter.startsWith("F") || filter.startsWith("AA") || filter.startsWith("DD") || filter.startsWith("LL")) return "F";
        if (filter.startsWith("DA")) return  "AD";
        if (filter.startsWith("LA")) return  "AL";
        if (filter.startsWith("LD")) return  "DL";
        return filter;
    }

    private static String uploadUserList(List<User> userList) {
        final String filename = PropertiesLoader.loadDataSourcePath() + "/" +
                getFilenameOfResponse("full_user_list.json");
        // System.out.println(filename);
        try {
            File file = new File(filename);
            file.createNewFile();
            Utils.prettyJackson.writerFor(List.class).writeValue(file, userList);
            return filename;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String getFilenameOfResponse(String baseName) {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + "_" + baseName;
    }
}
