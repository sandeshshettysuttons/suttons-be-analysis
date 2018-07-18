package au.com.suttons.notification.model;

import au.com.suttons.notification.resource.bean.RequestBean;

public class CompanySearchParameters extends BaseSearchParameters
{
    private String code;
    private String name;
    private String status;

	public static CompanySearchParameters parse(RequestBean requestBean)
    {
        CompanySearchParameters param = new CompanySearchParameters();

        param.setCode(requestBean.getQueryParam("code"));
        param.setName(requestBean.getQueryParam("name"));
        param.setStatus(requestBean.getQueryParam("status"));

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
