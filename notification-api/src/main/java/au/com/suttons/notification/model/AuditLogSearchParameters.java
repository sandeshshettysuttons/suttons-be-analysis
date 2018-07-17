package au.com.suttons.notification.model;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.Response.Status;

import au.com.suttons.notification.config.AppConfig;
import au.com.suttons.notification.data.entity.UserEntity;
import au.com.suttons.notification.resource.bean.RequestBean;
import au.com.suttons.notification.resource.error.RestApiException;
import au.com.suttons.notification.util.DateUtil;
import au.com.suttons.notification.util.ObjectUtil;

public class AuditLogSearchParameters extends BaseSearchParameters {

    private List<String> activityTypes;

    private Long userId;
    private UserEntity user;

    private String activityResource;
    private Long activityResourceId;
    private Date activityDateFrom;
    private Date activityDateTo;
    private String channel;

    private boolean includesReadOnlyActivities;

	public static AuditLogSearchParameters parse(RequestBean requestBean)
    {
        AuditLogSearchParameters param = new AuditLogSearchParameters();

        String activityTypesStr = requestBean.getQueryParam("activityTypes");
        try {
        	//if no activity types specified, set default display types
            if (!ObjectUtil.isObjectEmpty(activityTypesStr)) {
            	param.setActivityTypes(Arrays.asList(activityTypesStr.split(",")));

            } else if(AppConfig.auditLogTypesDisplay != null && !AppConfig.auditLogTypesDisplay.equals("*")) {
            	param.setActivityTypes(Arrays.asList(AppConfig.auditLogTypesDisplay.split(",")));
            }
        }
        catch (Exception e) {
            throw new RestApiException(Status.BAD_REQUEST.getStatusCode(), null, "Invalid activity types value", null, e);
        }
        
        param.setActivityResource(requestBean.getQueryParam("activityResource"));

        String userIdStr   = requestBean.getQueryParam("userId");
        try {
            if (!ObjectUtil.isObjectEmpty(userIdStr))
            {
                param.setUserId(Long.parseLong(userIdStr));
            }
        }
        catch (Exception e) {
            throw new RestApiException(Status.BAD_REQUEST.getStatusCode(), null, "Invalid User Id", null, e);
        }

        String activityResourceIdStr   = requestBean.getQueryParam("activityResourceId");
        try {
            if (!ObjectUtil.isObjectEmpty(activityResourceIdStr))
            {
                param.setActivityResourceId(Long.parseLong(activityResourceIdStr));
            }
        }
        catch (Exception e) {
            throw new RestApiException(Status.BAD_REQUEST.getStatusCode(), null, "Invalid Resource Id", null, e);
        }

        String activityDateFromStr = requestBean.getQueryParam("activityDateFrom");
        String activityDateToStr = requestBean.getQueryParam("activityDateTo");
        try {
            if (!ObjectUtil.isObjectEmpty(activityDateFromStr) && !ObjectUtil.isObjectEmpty(activityDateToStr)) {
                param.setActivityDateFrom(DateUtil.stringShortDateToDate(activityDateFromStr));
                param.setActivityDateTo(DateUtil.stringShortDateToDate(activityDateToStr));
            }
        } catch (Exception e) {
            throw new RestApiException(Status.BAD_REQUEST.getStatusCode(), null, "Invalid Assessment Date", null, e);
        }

        param.setChannel(requestBean.getQueryParam("channel"));

        return param;
    }

	public List<String> getActivityTypes() {
		return activityTypes;
	}
	public void setActivityTypes(List<String> activityTypes) {
		this.activityTypes = activityTypes;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
    public UserEntity getUser() {
        return user;
    }
    public void setUser(UserEntity user) {
        this.user = user;
    }
	public String getActivityResource() {
		return activityResource;
	}
	public void setActivityResource(String activityResource) {
		this.activityResource = activityResource;
	}
	public Long getActivityResourceId() {
		return activityResourceId;
	}
	public void setActivityResourceId(Long activityResourceId) {
		this.activityResourceId = activityResourceId;
	}
	public Date getActivityDateFrom() {
		return activityDateFrom;
	}
	public void setActivityDateFrom(Date activityDateFrom) {
		this.activityDateFrom = activityDateFrom;
	}
	public Date getActivityDateTo() {
		return activityDateTo;
	}
	public void setActivityDateTo(Date activityDateTo) {
		this.activityDateTo = activityDateTo;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
}
