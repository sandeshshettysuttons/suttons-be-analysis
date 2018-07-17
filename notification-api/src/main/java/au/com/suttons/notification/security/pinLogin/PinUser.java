package au.com.suttons.notification.security.pinLogin;

import au.com.suttons.notification.data.entity.UserEntity;
import au.com.suttons.notification.security.StarUser;

/**
 * Implements the {@link java.security.Principal} interface and provides some additional properties that are available through the
 * STAR security model.  These additional properties provide more metadata about the current user.
 */
public class PinUser extends StarUser {

	private String uniqueName;

    public PinUser(UserEntity user) {
        this.username = (user != null) ? (user.getUserName() != null ? user.getUserName() : null) : "Guest";
        this.uniqueName = (user != null) ? (user.getPrintName()+":"+user.getLoginId()) : "Guest";
        this.userId = user.getId();
    }

	public String getUniqueName() {
		return uniqueName;
	}

	public void setUniqueName(String uniqueName) {
		this.uniqueName = uniqueName;
	}

	@Override
	public String getName() {
		//return this.getUniqueName();
		return this.getUsername();
	}
}