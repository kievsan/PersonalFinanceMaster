package ru.mail.kievsan.backend.service.impl;

import ru.mail.kievsan.backend.exception.UserNotFoundException;
import ru.mail.kievsan.backend.model.ActivityStatus;
import ru.mail.kievsan.backend.model.Role;
import ru.mail.kievsan.backend.model.entity.User;
import ru.mail.kievsan.backend.repository.impl.UserFileRepo;

import java.util.List;


public class UsersAdminFileService extends ServiceImplFile<String, User> {

   UserFileService userService;

    public UsersAdminFileService(UserFileRepo userRepo) {
        super(userRepo);
    }


    @Override
    public User register(User user) {
        return userService.register(user);
    }

    @Override
    public void signup(User newUser) {
        userService.signup(newUser);
    }

    @Override
    public User update(User user) {
        return userService.update(user);
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
        return repo.getById(id).orElseThrow(() -> new UserNotFoundException("user '" + id + "' not found!"));
    }

    public List<User> getUserListByStatus(ActivityStatus status) {
        return repo.getAll().stream().filter(user -> user.getStatus() == status).toList();
    }

    public List<User> getUserList(String filter) {
        List<User> userList;
        switch (filter) {
            case "A" -> {
                userList = getUserListByStatus(ActivityStatus.ACTIVE);
                System.out.printf("Выбран status: %s\n", ActivityStatus.ACTIVE);
            }
            case "D" -> {
                userList = getUserListByStatus(ActivityStatus.DELETED);
                System.out.printf("Выбран status: %s\n", ActivityStatus.DELETED);
            }
            case "L" -> {
                userList = getUserListByStatus(ActivityStatus.LOCKED);
                System.out.printf("Выбран status: %s\n", ActivityStatus.LOCKED);
            }
            case "AD" -> {
                userList = getUserListByStatus(ActivityStatus.ACTIVE);
                userList.addAll(getUserListByStatus(ActivityStatus.DELETED));
                System.out.printf("Выбран status:\t%s + %s\n", ActivityStatus.ACTIVE, ActivityStatus.DELETED);
            }
            case "AL" -> {
                userList = getUserListByStatus(ActivityStatus.ACTIVE);
                userList.addAll(getUserListByStatus(ActivityStatus.LOCKED));
                System.out.printf("Выбран status:\t%s + %s\n", ActivityStatus.ACTIVE, ActivityStatus.LOCKED);
            }
            case "DL" -> {
                userList = getUserListByStatus(ActivityStatus.DELETED);
                userList.addAll(getUserListByStatus(ActivityStatus.LOCKED));
                System.out.printf("Выбран status:\t%s_and_%s\n", ActivityStatus.DELETED, ActivityStatus.LOCKED);
            }
            default -> {
                System.out.println("Выбран status: Full");
                return repo.getAll();
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
