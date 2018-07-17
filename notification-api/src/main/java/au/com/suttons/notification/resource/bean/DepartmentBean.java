package au.com.suttons.notification.resource.bean;

public class DepartmentBean extends BaseResourceBean
{
    private Long version;

    private String name;
    private String code;
	private Boolean isActive;

    public DepartmentBean()
    {
        this.setResourceType("department");
    }

    public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
}
