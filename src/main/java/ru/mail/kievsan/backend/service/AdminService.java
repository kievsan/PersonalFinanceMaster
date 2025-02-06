package ru.mail.kievsan.backend.service;

import lombok.AllArgsConstructor;

import ru.mail.kievsan.backend.model.dto.Session;
import ru.mail.kievsan.backend.model.entity.User;
import ru.mail.kievsan.backend.repository.impl.UserFileRepo;

import java.util.Arrays;
import java.util.NoSuchElementException;


@AllArgsConstructor
public class AdminService {

    private final UserFileRepo userRepo;

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

    public Session logout(Session request) {
        var id = request.getCurrentUser().getId();
        var role = request.getCurrentUser().getRole().name();
        System.out.printf("User '%s}' (%s) was logged out...\n", id, role);
        System.out.printf("Success logout: %s '%s'", role, id) ;
        return request;
    }
}
