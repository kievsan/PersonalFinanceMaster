package ru.mail.kievsan.frontend;

import com.fasterxml.jackson.core.JsonProcessingException;
import ru.mail.kievsan.backend.conf.PropertiesLoader;
import ru.mail.kievsan.backend.controller.UserController;
import ru.mail.kievsan.backend.exception.NotValidUserException;
import ru.mail.kievsan.backend.model.Role;
import ru.mail.kievsan.backend.model.Status;
import ru.mail.kievsan.backend.model.dto.ResponseEntity;
import ru.mail.kievsan.backend.model.dto.Session;
import ru.mail.kievsan.backend.model.entity.User;
import ru.mail.kievsan.backend.repository.impl.UserFileRepo;
import ru.mail.kievsan.backend.security.PasswordEncoder;
import ru.mail.kievsan.backend.service.UserService;
import ru.mail.kievsan.util.Utils;

import java.util.Scanner;


public class SessionLoop {

    static Session session = new Session();

    static final UserFileRepo userRepo = new UserFileRepo();
    static final UserService userService = new UserService(userRepo);
    static final UserController userController = new UserController(userService);

    static {
        if (userRepo.size() == 0) {
            userService.signup(User.builder()
                    .id(PropertiesLoader.loadAdminLogin())
                    .password(PasswordEncoder.encodeBCrypt(PropertiesLoader.loadAdminPassword()))
                    .role(Role.ADMIN)
                    .build());
        }
    }

    public static void start() {
        System.out.println("\n*****\nДобро пожаловать в приложение \"Домашние финансы\"!");

        try (Scanner scanner = new Scanner(System.in)) {
            session.setScanner(scanner);
            start: while (true) {
                try {
                    newSession();
                    switch (startMenu()) {
                        case 1 -> {
                            var response = userController.login(session.getCurrentUser());
                            processResponse(response);
                            MainMenu.start(session, userController);
                        }
                        case 2 -> {
                            var response = userController.register(session.getCurrentUser());
                            processResponse(response);
                            MainMenu.start(session, userController);
                        }
                        default -> {
                            if (choiceCloseApp("Завершить приложение?")) break start;
                            continue;
                        }
                    }
                } catch(NotValidUserException e) {
                    System.out.println(Status.FAIL + "!\n" + e.getMessage());
                    if (choiceCloseApp("Завершить приложение?")) break;
                    continue;

                } catch(Exception e) {
                    System.out.println(Status.FAIL + "!\n" + e.getMessage());
                }
                System.out.println("\n*****\n");
            }
            close();
        }
        System.out.println("\n*****\nСпасибо, что воспользовались приложением \"Домашние финансы\"!");
    }

    public static void newSession() throws NotValidUserException {
        session = new Session(session.getScanner(), getValidUser());
    }

    public static User getValidUser() throws NotValidUserException {
        System.out.printf("Введите логин (от %d-х символов): ", User.MIN_LOGIN_LENGTH);
        String login = session.getScanner().nextLine();
        System.out.printf("Введите пароль (от %d-ти символов): ", User.MIN_PASSWORD_LENGTH);
        String password = session.getScanner().nextLine();
        return new User(login, password, Role.USER);
    }

    private static void processResponse(ResponseEntity<User> response) throws RuntimeException, JsonProcessingException {
        if (response.getStatus() == Status.FAIL) throw new RuntimeException(response.getMessage());
        System.out.println(response.getStatus() + "!");
        System.out.println(response.getBody() == null ? "" : Utils.toJackson(response.getBody()));
    }

    public static int startMenu() {
        while(true) {
            System.out.println("\n\tЧего изволите?\n----------------------");
            System.out.println("1. Войти");
            System.out.println("2. Зарегистрироваться");
            System.out.println("3. Выйти из приложения");
            System.out.print("\nВыберите пункт меню (1-3): ");
            try {
                int userChoice = session.getScanner().nextInt();
                if (session.getScanner().hasNextLine()) session.getScanner().nextLine();
                return userChoice;
            } catch (Exception ignored) {}
        }
    }

    public static boolean choiceCloseApp(String question) {
        System.out.printf("\n%s (1 - Да): ", question);
        try {
            return session.getScanner().nextLine().charAt(0) == '1';
        } catch (Exception e) {
            System.out.println("- Нет\n");
            return false;
        }
    }

    public static void close() {
        userRepo.close();
        System.out.printf("'%s' закрыл приложение...\n",
                session.getCurrentUser() == null || session.getCurrentUser().getId().isBlank()
                        ? "anonymous" : session.getCurrentUser().getId());
    }
}
