package au.com.suttons.notification.resource.bean;

import java.util.List;

public class TemplateBean extends BaseResourceBean
{
    private Long version;

    private String name;
    private String description;
    private List<RoleBean> roles;
    private Long accessLevel;
    private Long lowerAccessLevelFrom;
    private Long lowerAccessLevelTo;

    public TemplateBean()
    {
        this.setResourceType("templates");
    }

    public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public List<RoleBean> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleBean> roles) {
        this.roles = roles;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

	public Long getAccessLevel() {
		return accessLevel;
	}

	public void setAccessLevel(Long accessLevel) {
		this.accessLevel = accessLevel;
	}

	public Long getLowerAccessLevelFrom() {
		return lowerAccessLevelFrom;
	}

	public void setLowerAccessLevelFrom(Long lowerAccessLevelFrom) {
		this.lowerAccessLevelFrom = lowerAccessLevelFrom;
	}

	public Long getLowerAccessLevelTo() {
		return lowerAccessLevelTo;
	}

	public void setLowerAccessLevelTo(Long lowerAccessLevelTo) {
		this.lowerAccessLevelTo = lowerAccessLevelTo;
	}
    
}
