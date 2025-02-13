package ru.mail.kievsan.backend.controller.impl;

import ru.mail.kievsan.backend.controller.ControllerImpl;
import ru.mail.kievsan.backend.model.ResponseStatus;
import ru.mail.kievsan.backend.model.dto.ResponseEntity;
import ru.mail.kievsan.backend.model.dto.Session;

import ru.mail.kievsan.backend.model.entity.User;
import ru.mail.kievsan.backend.service.impl.UserFileService;


public class UserController extends ControllerImpl<String, User, UserFileService> {

    public UserController(UserFileService userService) {
        super(userService);
    }

    @Override
    public ResponseEntity<User> register(User request) {
        try{
            var response = service.register(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(ResponseStatus.FAIL, e.getMessage());
        }
    }

    public ResponseEntity<User> login(User request) {
        try{
            var response = service.authenticate(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(ResponseStatus.FAIL, e.getMessage());
        }
    }

    public ResponseEntity<String> logout(User request) {
        try{
            var response = service.logout(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(ResponseStatus.FAIL, e.getMessage());
        }
    }

    @Override
    public ResponseEntity<User> update(User request) {
        try{
            var response = service.update(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(ResponseStatus.FAIL, e.getMessage());
        }
    }

    public static void start(Session session) {}

}
