package ru.mail.kievsan.backend.controller;

import lombok.AllArgsConstructor;
import ru.mail.kievsan.backend.model.ResponseStatus;
import ru.mail.kievsan.backend.model.dto.ResponseEntity;
import ru.mail.kievsan.backend.model.dto.Session;
import ru.mail.kievsan.backend.model.entity.User;
import ru.mail.kievsan.backend.service.UserService;
import ru.mail.kievsan.backend.service.UsersAdminService;


@AllArgsConstructor
public class UsersAdminController implements Controller {

    private final UsersAdminService service;

    public ResponseEntity<User> update(User user) {
        try{
            var response = service.updateUser(user);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(ResponseStatus.FAIL, e.getMessage());
        }
    }

}
