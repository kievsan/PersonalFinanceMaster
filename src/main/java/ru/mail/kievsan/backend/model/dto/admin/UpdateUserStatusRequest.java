package ru.mail.kievsan.backend.model.dto.admin;

import ru.mail.kievsan.backend.model.ActivityStatus;


public record UpdateUserStatusRequest(String id, ActivityStatus status) {
    @Override
    public String toString() {
        return "Request status for user '" + this.id + "': " + this.status;
    }
}
