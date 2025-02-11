package ru.mail.kievsan.backend.service;

import lombok.AllArgsConstructor;

import ru.mail.kievsan.backend.exception.UserLockedException;
import ru.mail.kievsan.backend.exception.UserNotFoundException;
import ru.mail.kievsan.backend.exception.VerifyUserPasswordException;
import ru.mail.kievsan.backend.model.ActivityStatus;
import ru.mail.kievsan.backend.model.entity.User;
import ru.mail.kievsan.backend.repository.impl.UserFileRepo;
import ru.mail.kievsan.backend.security.PasswordEncoder;
import ru.mail.kievsan.util.Utils;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.NoSuchElementException;


@AllArgsConstructor
public class UserService implements Service {

    private final UserFileRepo userRepo;

    public User register(User user) throws RuntimeException {
        if (userRepo.existsById(user.getId())) {
            throw new RuntimeException("The login is already in use, registration is not possible!");
        }
        signup(new User(user, PasswordEncoder.encodeBCrypt(user.getPassword())));
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
        String msg = String.format("\n'%s'", user.getId());
        String errMsg = " was not authenticated: wrong username or password!";
        try {
            User targetUser = userRepo.getById(user.getId()).orElseThrow();

            if (targetUser.getStatus() == ActivityStatus.DELETED) throw new UserNotFoundException("user was deleted!");
            if (targetUser.getStatus() == ActivityStatus.LOCKED) throw new UserLockedException(String.format(
                    "%s was LOCKED! %s ", targetUser,
                    targetUser.getStatus_date().format(DateTimeFormatter.ofPattern("(yyyy-MM-dd HH:mm:ss) "))));

            Utils.verifyBCrypt(user.getPassword(), targetUser.getPassword());

            msg += String.format(" with ROLE = %s was authenticated!", targetUser.getRole().name());
            System.out.println("SUCCESS! " + msg);

            return new User(targetUser, user.getPassword());

        } catch (NoSuchElementException | UserNotFoundException | VerifyUserPasswordException e) {
            throw new UserNotFoundException(msg + errMsg);

        } catch (UserLockedException e) {
            throw e;

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
        User newUser = new User(user, PasswordEncoder.encodeBCrypt(user.getPassword()));
        User oldUser = userRepo.save(newUser).orElseThrow(() -> new UserNotFoundException(
                String.format("Обновить данные пользователя не удалось: %s - не найден!", user)));
        return user;
    }

}
