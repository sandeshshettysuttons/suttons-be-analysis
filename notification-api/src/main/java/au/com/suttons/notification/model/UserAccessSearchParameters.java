package au.com.suttons.notification.model;

import javax.ws.rs.core.Response.Status;

import au.com.suttons.notification.resource.bean.RequestBean;
import au.com.suttons.notification.resource.error.RestApiException;
import au.com.suttons.notification.util.ObjectUtil;

public class UserAccessSearchParameters extends BaseSearchParameters
{
    private Long userId;
    private Long companyId;
    private Long siteId;
    private Long templateId;

	public static UserAccessSearchParameters parse(RequestBean requestBean)
    {
        String userIdStr     = requestBean.getQueryParam("userId");
        String companyIdStr         = requestBean.getQueryParam("companyId");
        String siteIdStr         = requestBean.getQueryParam("siteId");
        String templateIdStr         = requestBean.getQueryParam("templateId");

        UserAccessSearchParameters param = new UserAccessSearchParameters();

        try {
            if (!ObjectUtil.isObjectEmpty(userIdStr))
            {
                param.setUserId(Long.parseLong(userIdStr));
            }
        }
        catch (Exception e) {
            throw new RestApiException(Status.BAD_REQUEST.getStatusCode(), null, "Invalid userId value", null, e);
        }

        try {
            if (!ObjectUtil.isObjectEmpty(companyIdStr))
            {
                param.setCompanyId(Long.parseLong(companyIdStr));
            }
        }
        catch (Exception e) {
            throw new RestApiException(Status.BAD_REQUEST.getStatusCode(), null, "Invalid companyId value", null, e);
        }

        try {
            if (!ObjectUtil.isObjectEmpty(siteIdStr))
            {
                param.setSiteId(Long.parseLong(siteIdStr));
            }
        }
        catch (Exception e) {
            throw new RestApiException(Status.BAD_REQUEST.getStatusCode(), null, "Invalid siteId value", null, e);
        }

        try {
            if (!ObjectUtil.isObjectEmpty(templateIdStr))
            {
                param.setTemplateId(Long.parseLong(templateIdStr));
            }
        }
        catch (Exception e) {
            throw new RestApiException(Status.BAD_REQUEST.getStatusCode(), null, "Invalid templateId value", null, e);
        }

        return param;
    }

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public Long getSiteId() {
		return siteId;
	}

	public void setSiteId(Long siteId) {
		this.siteId = siteId;
	}

	public Long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}
}
