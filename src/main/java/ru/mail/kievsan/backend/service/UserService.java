package ru.mail.kievsan.backend.service;

import ru.mail.kievsan.backend.model.Role;
import ru.mail.kievsan.backend.model.dto.session.SessionUser;
import ru.mail.kievsan.backend.model.entity.User;
import ru.mail.kievsan.backend.repository.impl.UserFileRepo;
import ru.mail.kievsan.util.Utils;

import java.util.function.Predicate;

public class UserService {
    private final UserFileRepo userRepo;

    public UserService(UserFileRepo userRepo) {
        this.userRepo = userRepo;
    }

    public User register(SessionUser request, User currentUser) {
        Predicate<User> USER_IS_ADMIN = user-> user != null && user.getRole() == Role.ADMIN;
        var newUser = User.builder()
                .id(request.getLogin())
                .password(request.getPassword())
                .role(USER_IS_ADMIN.test(currentUser) ? request.getRole() : Role.USER)
                .build();
        if (userRepo.existsById(newUser.getId())) {
            throw new RuntimeException("The username is already in use, registration is not possible!");
        }
        signup(newUser);
        return newUser;
    }

    public void signup(User newUser) {
        String msg = Utils.capitalize(newUser.getRole().toString());
        try {
            userRepo.save(newUser);
            var user = userRepo.getById(newUser.getId()).orElseThrow();
            msg += " signup: Id=" + user.getId();
            System.out.println(msg);
        } catch (RuntimeException e) {
            msg += " was not signup: %s" + e.getMessage();
            throw new RuntimeException(msg);
        }
    }

}
