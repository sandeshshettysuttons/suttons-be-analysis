package au.com.suttons.notification.exception;

/**
 * Indicates that a service operation that creates or updates an entity has rejected
 * the operation because of the state of the supplied entity.  Common causes include
 * out of range values and missing values for mandatory fields.
 */
public class NotFoundException extends RestApiException {

    public NotFoundException() {
        super();
    }

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Exception inner) {
        super(message, inner);
    }

    public NotFoundException(Exception inner) {
        super(inner);
    }
}
