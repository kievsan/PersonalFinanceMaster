package ru.mail.kievsan.frontend;

import ru.mail.kievsan.backend.conf.PropertiesLoader;
import ru.mail.kievsan.backend.model.Role;
import ru.mail.kievsan.backend.model.dto.Session;
import ru.mail.kievsan.backend.model.entity.User;
import ru.mail.kievsan.backend.repository.impl.UserFileRepo;
import ru.mail.kievsan.backend.security.PasswordEncoder;
import ru.mail.kievsan.backend.service.AuthService;
import ru.mail.kievsan.backend.service.UserService;

import java.io.IOException;
import java.util.Scanner;


public class SessionLoop {

    static Session session;

    static final UserFileRepo userRepo = new UserFileRepo();
    static final UserService userService = new UserService(userRepo);
    static final AuthService authService = new AuthService(userRepo);

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
            start: while (true) {
                try {
                    if (notValidUser(scanner)) {
                        if (choiceCloseApp()) break;
                        continue;
                    }
                    switch (startMenu()) {
                        case 1 -> MainMenu.start(authService.authenticate(session));
                        case 2 -> MainMenu.start(userService.register(session, null));
                        default -> {
                            if (choiceCloseApp()) break start;
                            continue;
                        }
                    }
                } catch(Exception e) {
                    System.out.println(e.getMessage());
                }
                System.out.println("\n*****\n");
            }
            close();
        }
        System.out.println("\n*****\nСпасибо, что воспользовались приложением \"Домашние финансы\"!");
    }

    public static boolean notValidUser(Scanner scanner) {
        int minLogin = 3, minPassword = 6;

        System.out.printf("Введите логин (от %d-х символов): ", minLogin);
        String login = scanner.nextLine();
        System.out.printf("Введите пароль (от %d-ти символов): ", minPassword);
        String password = scanner.nextLine();

        session = new Session(scanner, new User(login, password, Role.USER), new PasswordEncoder());

        return login.isBlank() || login.length() < minLogin ||
                password.isBlank() || password.length() < minPassword;
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

    public static boolean choiceCloseApp() {
        System.out.print("\nЗавершить приложение? (1 - Да): ");
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
                session.getCurrentUser().getId().isBlank() ? "anonymous" : session.getCurrentUser().getId());
    }
}
