package ru.mail.kievsan.backend.model.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.mail.kievsan.backend.model.ActivityStatus;


public record updateUserStatusRequest(String id, ActivityStatus status) {
    @Override
    public String toString() {
        return "Request status for user '" + this.id + "': " + this.status;
    }
}
