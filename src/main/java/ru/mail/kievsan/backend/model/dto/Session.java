package ru.mail.kievsan.backend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import ru.mail.kievsan.backend.config.MVC;
import ru.mail.kievsan.backend.controller.impl.BudgetController;
import ru.mail.kievsan.backend.controller.impl.UserController;
import ru.mail.kievsan.backend.controller.impl.UsersAdminController;
import ru.mail.kievsan.backend.model.entity.User;
import ru.mail.kievsan.backend.repository.impl.BudgetRepoImplFile;
import ru.mail.kievsan.backend.repository.impl.UserRepoImplFile;
import ru.mail.kievsan.backend.service.impl.BudgetServiceImplFile;
import ru.mail.kievsan.backend.service.impl.UserServiceImplFile;
import ru.mail.kievsan.backend.service.impl.UsersAdminServiceImplFile;

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

        final UserRepoImplFile userRepo = new UserRepoImplFile();
        final UserServiceImplFile userService = new UserServiceImplFile(userRepo);
        mvc.put("userRepo", userRepo);
        mvc.put("userService", new UserServiceImplFile(userRepo));
        mvc.put("userController", new UserController(userService));

        final UsersAdminServiceImplFile usersAdminFileService = new UsersAdminServiceImplFile(userRepo);
        mvc.put("usersAdminService", usersAdminFileService);
        mvc.put("usersAdminController", new UsersAdminController(usersAdminFileService));

        final BudgetRepoImplFile budgetRepo = new BudgetRepoImplFile();
        final BudgetServiceImplFile budgetService = new BudgetServiceImplFile(budgetRepo, userRepo);
        mvc.put("budgetRepo", budgetRepo);
        mvc.put("budgetService", new BudgetServiceImplFile(budgetRepo, userRepo));
        mvc.put("budgetController", new BudgetController(budgetService));
    }
}
