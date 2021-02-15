package am.vardanmk.notes.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class BadRequestException extends RuntimeException {

    private final String msg = "Title can't be null or greater then 50 chars, or your note is longer then 1000";

    public BadRequestException(String msg) {
        super(msg);
    }

    public BadRequestException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
