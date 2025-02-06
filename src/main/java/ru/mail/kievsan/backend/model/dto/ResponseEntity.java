package ru.mail.kievsan.backend.model.dto;

import lombok.Data;
import ru.mail.kievsan.backend.model.Status;


@Data
public class ResponseEntity<T> {
    private final T body;
    private final Status status;
    private String message;

    public ResponseEntity(Status status) {
        this(status, "");
    }

    public ResponseEntity(Status status, String msg) {
        this(null, status, msg);
    }

    public ResponseEntity(T body, Status status) {
        this(body, status, "");
    }

    public ResponseEntity(T body, Status status, String msg) {
        this.body = body;
        this.status = status == null ? Status.FAIL : status;
        this.message = msg;
    }

    public boolean hasBody() {
        return this.body != null;
    }

    public static <T> ResponseEntity<T> ok() {
        return ok(null);
    }

    public static <T> ResponseEntity<T> ok(T body) {
        return new ResponseEntity<>(body, Status.OK);
    }

}
