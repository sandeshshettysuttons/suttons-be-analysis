package au.com.suttons.notification.model;

import au.com.suttons.notification.resource.bean.RequestBean;

public class MailRecipientSearchParameters extends BaseSearchParameters
{

    private String name;
    private String type;
    private String status;

    private String companyId;

	public static MailRecipientSearchParameters parse(RequestBean requestBean)
    {

        MailRecipientSearchParameters param = new MailRecipientSearchParameters();

        param.setName(requestBean.getQueryParam("name"));
        param.setType(requestBean.getQueryParam("type"));
        param.setStatus(requestBean.getQueryParam("status"));

        param.setCompanyId(requestBean.getQueryParam("companyId"));

        return param;
    }

    public MailRecipientSearchParameters() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }
}
