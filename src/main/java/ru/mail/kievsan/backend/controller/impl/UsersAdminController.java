package ru.mail.kievsan.backend.controller.impl;

import ru.mail.kievsan.backend.controller.ControllerImpl;
import ru.mail.kievsan.backend.model.ResponseStatus;
import ru.mail.kievsan.backend.model.dto.ResponseEntity;
import ru.mail.kievsan.backend.model.dto.admin.UpdateUserStatusRequest;
import ru.mail.kievsan.backend.model.entity.User;
import ru.mail.kievsan.backend.service.impl.UsersAdminServiceImplFile;

import java.util.List;
import java.util.Objects;


public class UsersAdminController extends ControllerImpl<String, User, UsersAdminServiceImplFile> {

    public UsersAdminController(UsersAdminServiceImplFile service) {
        super(service);
    }

    public ResponseEntity<User> register(User request) {
        try{
            var response = service.register(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(ResponseStatus.FAIL, e.getMessage());
        }
    }

    public ResponseEntity<User> update(User request) {
        try{
            var response = service.update(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(ResponseStatus.FAIL, e.getMessage());
        }
    }

    public ResponseEntity<User> updateUserStatus(UpdateUserStatusRequest request, User currentUser) {
        try{
            checkRightsForUpdate(request.id(), currentUser);
            var response = service.updateUserStatus(request.id(), request.status());
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

    public void checkRightsForUpdate(String requestId, User currentUser) {
        if (Objects.equals(requestId, currentUser.getId())) throw new RuntimeException("Has no access!");
        if (service.isSystemById(requestId) || (!currentUser.isSystem() && service.isAdminById(requestId))
        ) throw new RuntimeException("Has no rights!");
    }

}
