package au.com.suttons.notification.security;

import java.security.Principal;
import java.util.List;

/**
 * Implements the {@link java.security.Principal} interface and provides some additional properties that are available through the
 * STAR security model.  These additional properties provide more metadata about the current user.
 */
public class StarUser implements Principal {

    protected String username;
    protected Long userId;
    protected String displayName;
    protected List<String> roles;

        /**
     * Constructs a new, blank STAR user not backed by any other principal.  The username will be set as 'Guest' and
     * the first and last name properties will be empty.
     */
    public StarUser() {
        this(null);
    }

    /**
     * Constructs a new STAR user backed by a JOSSO user principal.  User metadata will be read from the JOSSO user.
     *
     * @param user The JOSSO authentication principal
     */
    public StarUser(Principal user) {
        this.username = (user != null) ? user.getName() : "Guest";
    }

    /**
     * Gets the username of the currently logged in user.
     *
     * @return The user's username, or 'Guest' if there is no security context.
     */
    @Override
    public String getName() {
        return this.username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

	public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        if (this.roles != null) {
            for (String role : this.roles) {
                sb.append(role).append(", ");
            }
            if (sb.length() > 2) {
                sb.delete(sb.length() - 3, sb.length() - 1);
            }
        }
        return sb.toString();
    }

    public boolean hasRole(String role) {
        return (this.roles != null && this.roles.contains(role));
    }
}