package am.vardanmk.notes.config;

public class UserRegistrationNotAllowedException extends IllegalStateException {

    private final String msg;

    public UserRegistrationNotAllowedException(String msg) {
        this.msg = msg;
    }
}
