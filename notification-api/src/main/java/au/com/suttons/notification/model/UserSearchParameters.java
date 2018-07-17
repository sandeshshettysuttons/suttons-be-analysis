package au.com.suttons.notification.model;

import au.com.suttons.notification.resource.bean.RequestBean;
import au.com.suttons.notification.resource.error.RestApiException;
import au.com.suttons.notification.util.ObjectUtil;

import javax.ws.rs.core.Response.Status;

public class UserSearchParameters extends BaseSearchParameters
{

    private String departmentCode;
    private Boolean isSystemAdmin;
    private String firstName;
    private String lastName;
    private String name;
    private String status;
    private Boolean isActive;

    //name or trainingId
    private String typeahead;
    private TemplateSearchParameters.LowerAccessLevelRange lowerAccessLevelRange;

	public static UserSearchParameters parse(RequestBean requestBean)
    {

        UserSearchParameters param = new UserSearchParameters();
        param.setDepartmentCode(requestBean.getDepartmentCode());

        param.setFirstName(requestBean.getQueryParam("firstName"));
        param.setLastName(requestBean.getQueryParam("lastName"));
        param.setName(requestBean.getQueryParam("name"));
        param.setStatus(requestBean.getQueryParam("status"));
        param.setTypeahead(requestBean.getQueryParam("typeahead"));

        String isActive = requestBean.getQueryParam("isActive");
        try {
            if (!ObjectUtil.isObjectEmpty(isActive))
            {
                param.setIsActive(Boolean.parseBoolean(isActive));
            }
        }
        catch (Exception e) {
            throw new RestApiException(Status.BAD_REQUEST.getStatusCode(), null, "Invalid IsActive value", null, e);
        }

        String isSystemAdminStr = requestBean.getQueryParam("isSystemAdmin");

        try {
            if (!ObjectUtil.isObjectEmpty(isSystemAdminStr))
            {
                param.setIsSystemAdmin(Boolean.parseBoolean(isSystemAdminStr));
            }
        }
        catch (Exception e) {
            throw new RestApiException(Status.BAD_REQUEST.getStatusCode(), null, "Invalid IsSystemAdmin value", null, e);
        }

        return param;
    }

    public UserSearchParameters() {
    }

    public String getDepartmentCode() { return departmentCode; }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Boolean isIsSystemAdmin() { return isSystemAdmin; }

    public void setIsSystemAdmin(Boolean isSystemAdmin) {
        this.isSystemAdmin = isSystemAdmin;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

	public String getTypeahead() {
		return typeahead;
	}

	public void setTypeahead(String typeahead) {
		this.typeahead = typeahead;
	}

    public TemplateSearchParameters.LowerAccessLevelRange getLowerAccessLevelRange() {
        return lowerAccessLevelRange;
    }

    public void setLowerAccessLevelRange(TemplateSearchParameters.LowerAccessLevelRange lowerAccessLevelRange) {
        this.lowerAccessLevelRange = lowerAccessLevelRange;
    }

    public Boolean isIsActive() { return isActive; }

    public void setIsActive(Boolean active) { isActive = active; }

}
