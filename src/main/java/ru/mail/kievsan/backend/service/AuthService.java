package ru.mail.kievsan.backend.service;

import lombok.AllArgsConstructor;

import ru.mail.kievsan.backend.model.dto.session.SessionUser;
import ru.mail.kievsan.backend.model.entity.User;
import ru.mail.kievsan.backend.repository.impl.UserFileRepo;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Objects;


@AllArgsConstructor
public class AuthService {

    private final UserFileRepo userRepo;

    public User authenticate(SessionUser request) throws RuntimeException {

        var requestUser = User.builder().id(request.getLogin()).password(request.getPassword()).build();

        String msg = String.format("User '%s'", request.getLogin());
        String errMsg = " was not authenticated: wrong username or password!";
        try {
            User currentUser = userRepo.getById(requestUser.getId()).orElseThrow();
            if (!Objects.equals(request.getPassword(), currentUser.getPassword())) {
                throw new NoSuchElementException("wrong password!");
            }
            msg += String.format(" with ROLE = %s was authenticated!", currentUser.getRole().name());
            System.out.println("SUCCESS! " + msg);
            return currentUser;
        } catch (NoSuchElementException e) {
            throw new RuntimeException(msg + errMsg);
        } catch (ClassCastException e) {
            throw new RuntimeException("\t authenticate service - ClassCastException: " +
                    Arrays.toString(e.getStackTrace()) + "\n\t\t" + e.getMessage()
            );
        }

    }

    public String logout(User user) {
        System.out.printf("User '%s}' (%s) was logged out...\n", user.getId(), user.getRole().name());
        return String.format("Success logout: %s '%s'", user.getRole().name(), user.getId()) ;
    }

}
