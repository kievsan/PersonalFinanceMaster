package ru.mail.kievsan.backend.controller;

import lombok.AllArgsConstructor;
import ru.mail.kievsan.backend.model.ActivityStatus;
import ru.mail.kievsan.backend.model.ResponseStatus;
import ru.mail.kievsan.backend.model.dto.ResponseEntity;
import ru.mail.kievsan.backend.model.dto.Session;
import ru.mail.kievsan.backend.model.dto.updateUserStatusRequest;
import ru.mail.kievsan.backend.model.entity.User;
import ru.mail.kievsan.backend.service.UserService;
import ru.mail.kievsan.backend.service.UsersAdminService;

import java.util.List;
import java.util.Objects;


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

    public ResponseEntity<User> updateUserStatus(updateUserStatusRequest request, User currentUser) {
        System.out.println(request); ////////////////////////////
        try{
            checkAccess(request.getId(), currentUser);
            var response = service.updateUserStatus(request.getId(), request.getStatus());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(ResponseStatus.FAIL, e.getMessage());
        }
    }

    public ResponseEntity<User> findUserById(String id) {
        try{
            var response = service.findUserById(id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(ResponseStatus.FAIL, e.getMessage());
        }
    }

    public ResponseEntity<List<User>> getUserList(String request) {
        try{
            var response = service.getUserList(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(ResponseStatus.FAIL, e.getMessage());
        }
    }

    public ResponseEntity<Integer> getUsersNumber(String request) {
        try{
            var response = service.getUsersNumber(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(ResponseStatus.FAIL, e.getMessage());
        }
    }

    public void checkAccess(String requestId, User currentUser) {
        if (Objects.equals(requestId, currentUser.getId())) throw new RuntimeException("Has no access!");
    }

}
