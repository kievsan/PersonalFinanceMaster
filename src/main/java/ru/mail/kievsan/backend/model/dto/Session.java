package ru.mail.kievsan.backend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import ru.mail.kievsan.backend.config.MVC;
import ru.mail.kievsan.backend.controller.UserController;
import ru.mail.kievsan.backend.controller.UsersAdminController;
import ru.mail.kievsan.backend.model.entity.User;
import ru.mail.kievsan.backend.repository.impl.UserFileRepo;
import ru.mail.kievsan.backend.service.UserService;
import ru.mail.kievsan.backend.service.UsersAdminService;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


@Getter
@Setter
@AllArgsConstructor
public class Session {
    User currentUser;
    Scanner scanner;
    Map<String, MVC> mvc;

    public Session() {
        setMvc();
    }

    public void setMvc() {
        this.mvc = new HashMap<>();

        final UserFileRepo userRepo = new UserFileRepo();
        final UserService userService = new UserService(userRepo);
        mvc.put("userRepo", userRepo);
        mvc.put("userService", new UserService(userRepo));
        mvc.put("userController", new UserController(userService));

        final UsersAdminService usersAdminService = new UsersAdminService(userRepo);
        mvc.put("usersAdminService", usersAdminService);
        mvc.put("usersAdminController", new UsersAdminController(usersAdminService));
    }
}
