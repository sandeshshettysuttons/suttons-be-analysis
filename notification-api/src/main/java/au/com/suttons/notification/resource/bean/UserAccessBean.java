package au.com.suttons.notification.resource.bean;

public class UserAccessBean extends SecondaryResourceBean
{
    private Long version;

    private UserBean user;

	//department role
	private DepartmentBean department;

    private TemplateBean template;

    public UserAccessBean()
    {
        this.setResourceType("userAccess");
    }

    public String getOrgName() {
    	if(department != null) {
    		return department.getName();
    	} else {
    		return "";
    	}
    }

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public UserBean getUser() {
		return user;
	}

	public void setUser(UserBean user) {
		this.user = user;
	}

	public DepartmentBean getDepartment() {
		return department;
	}

	public void setDepartment(DepartmentBean department) {
		this.department = department;
	}

	public TemplateBean getTemplate() {
		return template;
	}

	public void setTemplate(TemplateBean template) {
		this.template = template;
	}
}
