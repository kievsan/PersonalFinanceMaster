package ru.mail.kievsan.backend.controller;

import lombok.AllArgsConstructor;

import ru.mail.kievsan.backend.model.Status;
import ru.mail.kievsan.backend.model.dto.ResponseEntity;
import ru.mail.kievsan.backend.model.dto.Session;

import ru.mail.kievsan.backend.service.UserService;


@AllArgsConstructor
public class UserController {

    private final UserService service;

    public ResponseEntity<Session> register(Session request) {
        try{
            var response = service.register(request, null);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(Status.FAIL, e.getMessage());
        }
    }

    public ResponseEntity<Session> login(Session request) {
        try{
            var response = service.authenticate(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(Status.FAIL, e.getMessage());
        }
    }

    public ResponseEntity<String> logout(Session request) {
        try{
            var response = service.logout(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(Status.FAIL, e.getMessage());
        }
    }

    public static void start(Session session) {}

}
