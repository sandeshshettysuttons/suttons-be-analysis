package au.com.suttons.notification.resource.bean;

import java.util.Date;

public class AuditLogBean extends BaseResourceBean
{
    private Long version;

    private String activityType;
    private String activityResource;
    private Long activityResourceId;
    private Date activityDate;
    private String channel;
    private String description;
    private String activityTimestamp;

    private UserBean user;

    public AuditLogBean()
    {
        this.setResourceType("auditLog");
    }

	public UserBean getUser() {
		return user;
	}

	public void setUser(UserBean user) {
		this.user = user;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public String getActivityType() {
		return activityType;
	}

	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}

	public String getActivityResource() {
		return activityResource;
	}

	public void setActivityResource(String activityResource) {
		this.activityResource = activityResource;
	}

	public Long getActivityResourceId() {
		return activityResourceId;
	}

	public void setActivityResourceId(Long activityResourceId) {
		this.activityResourceId = activityResourceId;
	}

	public Date getActivityDate() {
		return activityDate;
	}

	public void setActivityDate(Date activityDate) {
		this.activityDate = activityDate;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getActivityTimestamp() {
		return activityTimestamp;
	}

	public void setActivityTimestamp(String activityTimestamp) {
		this.activityTimestamp = activityTimestamp;
	}
}
