package ua.foxminded.university.util.exceptions;

public class EntityExistsException extends RuntimeException {

    public EntityExistsException() {
    }

    public EntityExistsException(String message) {
        super(message);
    }

    public EntityExistsException(Throwable cause) {
        super(cause);
    }

    public EntityExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
