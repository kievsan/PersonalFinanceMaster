package ru.mail.kievsan.backend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.mail.kievsan.backend.model.ActivityStatus;


@Getter
@AllArgsConstructor
public class updateUserStatusRequest {
    final String id;
    final ActivityStatus status;

    @Override
    public String toString() {
        return "Request status for user '" + this.id + "': " + this.status;
    }
}
