package au.com.suttons.notification.resource.bean;

import au.com.suttons.notification.resource.page.Sort;
import au.com.suttons.notification.util.StringUtil;

import java.util.List;

public class UserBean extends BaseResourceBean
{
    private Long version;

    private String name;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String status;
    private String userName;
    private String loginId;
    private String password;
    private String nonADLoginSessionId;

    private Boolean isSystemAdmin;

    //to return accessable deparments (myprofile)
    private List<DepartmentBean> departments;

	private List<UserAccessBean> userAccesses;

    private List<TemplateBean> templates;

    public UserBean()
    {
        this.setResourceType("user");
    }

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

	public String getName()
    {
        return StringUtil.spaceConcatStrings(this.firstName, this.lastName).trim();
    }

	public void setName() {
		this.name = this.getName();
	}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

	public Boolean getIsSystemAdmin() {
        return isSystemAdmin;
    }

    public void setIsSystemAdmin(Boolean isSystemAdmin) {
        this.isSystemAdmin = isSystemAdmin;
    }

    public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getNonADLoginSessionId() {
		return nonADLoginSessionId;
	}

	public void setNonADLoginSessionId(String nonADLoginSessionId) {
		this.nonADLoginSessionId = nonADLoginSessionId;
	}

    public List<DepartmentBean> getDepartments() {
        return departments;
    }

    public void setDepartments(List<DepartmentBean> departments) {
        this.departments = departments;
    }

//    public List<UserAccessBean> getUserAccess() {
//		return userAccesses;
//	}

//	public void setUserAccess(List<UserAccessBean> userAccesses) {
//		this.userAccesses = userAccesses;
//	}

    public List<UserAccessBean> getUserAccesses() {
        return userAccesses;
    }

    public void setUserAccesses(List<UserAccessBean> userAccesses) {
        this.userAccesses = userAccesses;
    }

    public List<TemplateBean> getTemplates() {  return templates; }

    public void setTemplates(List<TemplateBean> templates) { this.templates = templates; }

    public static Sort sortByFirstNameASCAndLastNameASC() {
        Sort.Order firstNameOrder = new Sort.Order(Sort.Direction.ASC, "firstName");
        Sort.Order lastNameOrder = new Sort.Order(Sort.Direction.ASC, "lastName");
        return new Sort(firstNameOrder, lastNameOrder);
    }
}
