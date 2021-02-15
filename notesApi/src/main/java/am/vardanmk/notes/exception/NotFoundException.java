package am.vardanmk.notes.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NotFoundException extends RuntimeException {

    private final String msg = "Something went wrong, note doesn't updated";

    public NotFoundException(String msg) {
        super(msg);
    }

    public NotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }
}