package ru.mail.kievsan.backend.service;

import lombok.AllArgsConstructor;

import ru.mail.kievsan.backend.exception.UserNotFoundException;
import ru.mail.kievsan.backend.exception.VerifyUserPasswordException;
import ru.mail.kievsan.backend.model.Role;
import ru.mail.kievsan.backend.model.entity.User;
import ru.mail.kievsan.backend.repository.impl.UserFileRepo;
import ru.mail.kievsan.backend.security.PasswordEncoder;
import ru.mail.kievsan.util.Utils;

import java.util.Arrays;
import java.util.NoSuchElementException;


@AllArgsConstructor
public class UserService {

    private final UserFileRepo userRepo;

    public User register(User user) throws RuntimeException {
        if (userRepo.existsById(user.getId())) {
            throw new RuntimeException("The login is already in use, registration is not possible!");
        }
        var newUser = User.builder()
                .id(user.getId())
                .password(PasswordEncoder.encodeBCrypt(user.getPassword()))
                .role(Role.USER)
                .build();
        signup(newUser);
        return user;
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

    public User authenticate(User user) throws RuntimeException {

        String msg = String.format("User '%s'", user.getId());
        String errMsg = " was not authenticated: wrong username or password!";
        try {
            User targetUser = userRepo.getById(user.getId()).orElseThrow();

             Utils.verifyBCrypt(user.getPassword(), targetUser.getPassword());

            msg += String.format(" with ROLE = %s was authenticated!", targetUser.getRole().name());
            System.out.println("SUCCESS! " + msg);

            return user;

        } catch (NoSuchElementException e) {
            throw new RuntimeException(msg + errMsg);

        } catch (VerifyUserPasswordException e) {
            throw new RuntimeException(msg + "\nIt's wrong password!");

        } catch (RuntimeException e) {
            throw new RuntimeException("\t User authenticate service exception: " +
                    Arrays.toString(e.getStackTrace()) + "\n\t\t" + e.getMessage());
        }
    }

    public String logout(User user) {
        var id = user.getId();
        var role = user.getRole().name();
        //System.out.printf("(%s) '%s'  was logged out...\n", role, id);
        return String.format("Success logout: %s '%s'", role, id);
    }

    public User updateUser(User user) throws UserNotFoundException {
        User newUser = new User(
                user.getId(),
                PasswordEncoder.encodeBCrypt(user.getPassword()),
                user.getRole(),
                user.getReg_date(),
                user.getStatus(),
                user.isDel());
        User oldUser = userRepo.save(newUser).orElseThrow(() -> new UserNotFoundException(
                String.format("Обновить данные пользователя не удалось: %s - не найден!", user)));
        return user;
    }

}
