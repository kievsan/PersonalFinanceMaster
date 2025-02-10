package ru.mail.kievsan.backend.service;

import lombok.AllArgsConstructor;
import ru.mail.kievsan.backend.exception.UserNotFoundException;
import ru.mail.kievsan.backend.exception.VerifyUserPasswordException;
import ru.mail.kievsan.backend.model.entity.User;
import ru.mail.kievsan.backend.repository.impl.UserFileRepo;
import ru.mail.kievsan.backend.security.PasswordEncoder;
import ru.mail.kievsan.util.Utils;

import java.util.Arrays;
import java.util.NoSuchElementException;


@AllArgsConstructor
public class UsersAdminService implements Service {

    private final UserFileRepo userRepo;

    public User updateUser(User user) throws UserNotFoundException {
        User newUser = new User(user, PasswordEncoder.encodeBCrypt(user.getPassword()));
        User oldUser = userRepo.save(newUser).orElseThrow(() -> new UserNotFoundException(
                String.format("Обновить данные пользователя не удалось: %s - не найден!", user)));
        return user;
    }


}
