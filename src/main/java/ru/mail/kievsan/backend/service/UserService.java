package ru.mail.kievsan.backend.service;

import lombok.AllArgsConstructor;

import ru.mail.kievsan.backend.model.Role;
import ru.mail.kievsan.backend.model.dto.Session;
import ru.mail.kievsan.backend.model.entity.User;
import ru.mail.kievsan.backend.repository.impl.UserFileRepo;
import ru.mail.kievsan.backend.security.PasswordEncoder;
import ru.mail.kievsan.util.Utils;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.function.Predicate;


@AllArgsConstructor
public class UserService {

    private final UserFileRepo userRepo;

    public Session register(Session request, User owner) throws RuntimeException {
        Predicate<User> USER_IS_ADMIN = user-> user != null && user.getRole() == Role.ADMIN;
        var newUser = User.builder()
                .id(request.getCurrentUser().getId())
                .password(PasswordEncoder.encodeBCrypt(request.getCurrentUser().getPassword()))
                .role(USER_IS_ADMIN.test(owner) ? request.getCurrentUser().getRole() : Role.USER)
                .build();
        if (userRepo.existsById(newUser.getId())) {
            throw new RuntimeException("The username is already in use, registration is not possible!");
        }
        signup(newUser);
        request.setCurrentUser(newUser);
        return request;
    }

    public void signup(User newUser) throws RuntimeException {
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

    public Session authenticate(Session request) throws RuntimeException {

        String msg = String.format("User '%s'", request.getCurrentUser().getId());
        String errMsg = " was not authenticated: wrong username or password!";
        try {
            User user = userRepo.getById(request.getCurrentUser().getId()).orElseThrow();

            if (!request.getEncoder().verifyBCrypt(request.getCurrentUser().getPassword(), user.getPassword())) {
                throw new NoSuchElementException("wrong password!");
            }
            msg += String.format(" with ROLE = %s was authenticated!", user.getRole().name());
            System.out.println("SUCCESS! " + msg);

            request.setCurrentUser(user);
            return request;

        } catch (NoSuchElementException e) {
            throw new RuntimeException(msg + errMsg);

        } catch (RuntimeException e) {
            throw new RuntimeException("\t authenticate service exception: " +
                    Arrays.toString(e.getStackTrace()) + "\n\t\t" + e.getMessage());
        }
    }

    public String logout(Session request) {
        var id = request.getCurrentUser().getId();
        var role = request.getCurrentUser().getRole().name();
        System.out.printf("(%s) '%s'  was logged out...\n", role, id);
        return String.format("Success logout: %s '%s'", role, id);
    }

}
