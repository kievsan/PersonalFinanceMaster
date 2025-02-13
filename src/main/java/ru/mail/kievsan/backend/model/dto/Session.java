package ru.mail.kievsan.backend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import ru.mail.kievsan.backend.config.MVC;
import ru.mail.kievsan.backend.controller.impl.UserController;
import ru.mail.kievsan.backend.controller.impl.UsersAdminController;
import ru.mail.kievsan.backend.model.entity.User;
import ru.mail.kievsan.backend.repository.impl.UserFileRepo;
import ru.mail.kievsan.backend.service.impl.UserFileService;
import ru.mail.kievsan.backend.service.impl.UsersAdminFileService;

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
        final UserFileService userFileService = new UserFileService(userRepo);
        mvc.put("userRepo", userRepo);
        mvc.put("userService", new UserFileService(userRepo));
        mvc.put("userController", new UserController(userFileService));

        final UsersAdminFileService usersAdminFileService = new UsersAdminFileService(userRepo);
        mvc.put("usersAdminService", usersAdminFileService);
        mvc.put("usersAdminController", new UsersAdminController(usersAdminFileService));
    }
}
