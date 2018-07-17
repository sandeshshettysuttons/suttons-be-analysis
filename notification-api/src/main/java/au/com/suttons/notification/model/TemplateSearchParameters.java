package au.com.suttons.notification.model;

import java.util.List;

import javax.ws.rs.core.Response.Status;

import au.com.suttons.notification.resource.bean.RequestBean;
import au.com.suttons.notification.resource.error.RestApiException;
import au.com.suttons.notification.util.ObjectUtil;

public class TemplateSearchParameters
{
    private String departmentCode;
    private String userName;
    private Long userId;
    private List<LowerAccessLevelRange> lowerAccessLevelRanges;

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public List<LowerAccessLevelRange> getLowerAccessLevelRanges() {
		return lowerAccessLevelRanges;
	}

	public void setLowerAccessLevelRanges(List<LowerAccessLevelRange> lowerAccessLevelRanges) {
		this.lowerAccessLevelRanges = lowerAccessLevelRanges;
	}

	public static TemplateSearchParameters parse(RequestBean requestBean)
    {
        String userIdStr     = requestBean.getQueryParam("userId");

        TemplateSearchParameters param = new TemplateSearchParameters();

        try {
            if (!ObjectUtil.isObjectEmpty(userIdStr))
            {
                param.setUserId(Long.parseLong(userIdStr));
            }
        }
        catch (Exception e) {
            throw new RestApiException(Status.BAD_REQUEST.getStatusCode(), null, "Invalid UserId value", null, e);
        }

        param.setUserName(requestBean.getQueryParam("displayName"));
        param.setDepartmentCode(requestBean.getDepartmentCode());
        return param;
    }

    public class LowerAccessLevelRange {
        private Long lowerAccessLevelFrom;
        private Long lowerAccessLevelTo;

        public LowerAccessLevelRange(Long lowerAccessLevelFrom, Long lowerAccessLevelTo) {
            this.lowerAccessLevelFrom = lowerAccessLevelFrom;
            this.lowerAccessLevelTo = lowerAccessLevelTo;
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
}
