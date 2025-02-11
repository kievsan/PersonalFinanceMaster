package ru.mail.kievsan.backend.service;

import lombok.AllArgsConstructor;
import ru.mail.kievsan.backend.exception.UserNotFoundException;
import ru.mail.kievsan.backend.exception.VerifyUserPasswordException;
import ru.mail.kievsan.backend.model.ActivityStatus;
import ru.mail.kievsan.backend.model.Role;
import ru.mail.kievsan.backend.model.entity.User;
import ru.mail.kievsan.backend.repository.impl.UserFileRepo;
import ru.mail.kievsan.backend.security.PasswordEncoder;
import ru.mail.kievsan.util.Utils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


@AllArgsConstructor
public class UsersAdminService implements Service {

    private final UserFileRepo userRepo;

    public User updateUser(User user) throws UserNotFoundException {
        User newUser = new User(user, PasswordEncoder.encodeBCrypt(user.getPassword()));
        User oldUser = userRepo.save(newUser).orElseThrow(() -> new UserNotFoundException(
                String.format("Couldn't update the user: %s not found!", user)));
        return user;
    }

    public User updateUserStatus(String id, ActivityStatus status) {
        User user = getUserById(id);
        user.setStatus(status);
        user.setStatus_date();
        return user.copy();
    }

    public User findUserById(String id) {
        return getUserById(id).copy();
    }

    private User getUserById(String id) throws UserNotFoundException {
        return userRepo.getById(id).orElseThrow(() -> new UserNotFoundException("user '" + id + "' not found!"));
    }

    public List<User> getUserList(String filter) {
        List<User> userList;
        switch (filter) {
            case "A" -> {
                userList = userRepo.getUsersByStatus(ActivityStatus.ACTIVE);
                System.out.printf("Выбран status: %s\n", ActivityStatus.ACTIVE);
            }
            case "D" -> {
                userList =  userRepo.getUsersByStatus(ActivityStatus.DELETED);
                System.out.printf("Выбран status: %s\n", ActivityStatus.DELETED);
            }
            case "L" -> {
                userList =  userRepo.getUsersByStatus(ActivityStatus.LOCKED);
                System.out.printf("Выбран status: %s\n", ActivityStatus.LOCKED);
            }
            case "AD" -> {
                userList = userRepo.getUsersByStatus(ActivityStatus.ACTIVE);
                userList.addAll(userRepo.getUsersByStatus(ActivityStatus.DELETED));
                System.out.printf("Выбран status:\t%s + %s\n", ActivityStatus.ACTIVE, ActivityStatus.DELETED);
            }
            case "AL" -> {
                userList = userRepo.getUsersByStatus(ActivityStatus.ACTIVE);
                userList.addAll(userRepo.getUsersByStatus(ActivityStatus.LOCKED));
                System.out.printf("Выбран status:\t%s + %s\n", ActivityStatus.ACTIVE, ActivityStatus.LOCKED);
            }
            case "DL" -> {
                userList = userRepo.getUsersByStatus(ActivityStatus.DELETED);
                userList.addAll(userRepo.getUsersByStatus(ActivityStatus.LOCKED));
                System.out.printf("Выбран status:\t%s_and_%s\n", ActivityStatus.DELETED, ActivityStatus.LOCKED);
            }
            default -> {
                System.out.println("Выбран status: Full");
                return userRepo.getAll();
            }
        }
        return userList;
    }

    public Integer getUsersNumber(String filter) {
        return getUserList(filter).size();
    }

    public boolean isAdminById(String id) {
        try {
            return getUserById(id).getRole() == Role.ADMIN;
        } catch (UserNotFoundException ignored) {
            return false;
        }
    }

    public boolean isSystemById(String id) {
        try {
            return getUserById(id).isSystem();
        } catch (UserNotFoundException ignored) {
            return false;
        }
    }

}
