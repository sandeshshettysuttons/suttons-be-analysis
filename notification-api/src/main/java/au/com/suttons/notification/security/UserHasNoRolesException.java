package au.com.suttons.notification.security;

/**
 * Indicates that a service operation that creates or updates an entity has rejected
 * the operation because of the state of the supplied entity.  Common causes include
 * out of range values and missing values for mandatory fields.
 */
public class UserHasNoRolesException extends RuntimeException {

    public UserHasNoRolesException() {
        super();
    }

    public UserHasNoRolesException(String message) {
        super(message);
    }

    public UserHasNoRolesException(String message, Exception inner) {
        super(message, inner);
    }
}
