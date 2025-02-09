package ru.mail.kievsan.backend.model.dto;

import lombok.Data;
import ru.mail.kievsan.backend.model.ResponseStatus;


@Data
public class ResponseEntity<T> {
    private final T body;
    private final ResponseStatus status;
    private String message;

    public ResponseEntity(ResponseStatus status) {
        this(status, "");
    }

    public ResponseEntity(ResponseStatus status, String msg) {
        this(null, status, msg);
    }

    public ResponseEntity(T body, ResponseStatus status) {
        this(body, status, "");
    }

    public ResponseEntity(T body, ResponseStatus status, String msg) {
        this.body = body;
        this.status = status == null ? ResponseStatus.FAIL : status;
        this.message = msg;
    }

    public boolean hasBody() {
        return this.body != null;
    }

    public static <T> ResponseEntity<T> ok() {
        return ok(null);
    }

    public static <T> ResponseEntity<T> ok(T body) {
        return new ResponseEntity<>(body, ResponseStatus.OK);
    }

}
