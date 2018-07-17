package au.com.suttons.notification.model;

import au.com.suttons.notification.resource.bean.RequestBean;
import au.com.suttons.notification.util.ObjectUtil;

public class DepartmentSearchParameters extends BaseSearchParameters
{
    //code or name
    private String typeahead;

    private String code;
    private String name;
    private Boolean isActive;

	public static DepartmentSearchParameters parse(RequestBean requestBean)
    {
        DepartmentSearchParameters param = new DepartmentSearchParameters();

        param.setTypeahead(requestBean.getQueryParam("typeahead"));
        param.setCode(requestBean.getQueryParam("code"));
        param.setName(requestBean.getQueryParam("name"));

        String isActiveStr = requestBean.getQueryParam("isActive");

        if (!ObjectUtil.isObjectEmpty(isActiveStr)) {
            param.setIsActive(Boolean.parseBoolean(isActiveStr));
        }

        return param;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean isActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getTypeahead() {
        return typeahead;
    }

    public void setTypeahead(String typeahead) {
        this.typeahead = typeahead;
    }

}
