package au.com.suttons.notification.resource.error;

import au.com.suttons.notification.model.ErrorCode;
import au.com.suttons.notification.resource.bean.ResponseErrorBean;
import au.com.suttons.notification.resource.bean.RootResourceBean;

import javax.ejb.ApplicationException;
import javax.ws.rs.core.Response.Status;

/**
 * Same as RestApiException except if this exception
 * is thrown from one EJB and caught by another
 * it will not trigger the rollback of DB transaction
 */
@ApplicationException(rollback = false)
public class RestApiValidationException extends RuntimeException
{
    Integer httpStatus;
    String code;
    String message;
    String developerMessage;
    String moreInfo;
    Exception exception;
    RootResourceBean existingResource;

    public RestApiValidationException(Integer httpStatus, String code)
    {
        this(httpStatus, code, null, null, null);
    }

    public RestApiValidationException(Integer httpStatus, String code, String message, String developerMessage)
    {
        this(httpStatus, code, message, developerMessage, null);
    }

    public RestApiValidationException(Integer httpStatus, String code, String message, String developerMessage, Exception exception)
    {
        this(httpStatus, code, message, developerMessage, exception, null);
    }

    public RestApiValidationException(Integer httpStatus, String code, String message, String developerMessage, Exception exception, RootResourceBean existingResource)
    {
        this.existingResource = existingResource;
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
        this.developerMessage = developerMessage;
        this.exception = exception;
    }

    public static RestApiValidationException getReferenceException(String devMessage, boolean hasMultiple) {

        String message = null;

        if(!hasMultiple){
            message = ResponseErrorBean.INVALID_REFERENCE_FRIENDLY;
            devMessage = ResponseErrorBean.INVALID_REFERENCE_DEVELOPER + devMessage;
        }

        return new RestApiValidationException(
                Status.CONFLICT.getStatusCode(),
                null,
                message,
                devMessage
        );
    }
    public static RestApiValidationException getReferenceException(String devMessage) {
        return getReferenceException(devMessage, false);
    }

    public static RestApiValidationException getBusinessRuleException(String code, String message, String devMessage, RootResourceBean existingResource, boolean hasMultiple) {

        if(!hasMultiple){
            message = ResponseErrorBean.INVALID_BUSINESS_FRIENDLY + message;
            devMessage = ResponseErrorBean.INVALID_BUSINESS_DEVELOPER + devMessage;
        }

        return new RestApiValidationException(
                Status.CONFLICT.getStatusCode(),
                code,
                message,
                devMessage,
                null,
                existingResource
        );
    }

    public static RestApiValidationException getServiceUnavailableException(String code, String message, String devMessage, RootResourceBean existingResource, boolean hasMultiple)
    {
        if (!hasMultiple)
        {
            message = ResponseErrorBean.INVALID_BUSINESS_FRIENDLY + message;
            devMessage = ResponseErrorBean.INVALID_BUSINESS_DEVELOPER + devMessage;
        }

        return new RestApiValidationException(
                Status.SERVICE_UNAVAILABLE.getStatusCode(),
                code,
                message,
                devMessage,
                null,
                existingResource
        );
    }

    public static RestApiValidationException getNotFoundException(String message) {
        return new RestApiValidationException(Status.NOT_FOUND.getStatusCode(), null, ResponseErrorBean.NOT_FOUND_MSG, ResponseErrorBean.NOT_FOUND_DEV_MSG+ " " + message + ".");
    }

    public static RestApiValidationException getAccessRuleException(ErrorCode err, Object[] friendlyArgs, Object[] devArgs) {
        return new RestApiValidationException(Status.FORBIDDEN.getStatusCode(), err.name(), err.formatFriendlyMsg(friendlyArgs), err.formatDeveloperMsg(devArgs));
    }

    public static RestApiValidationException getBusinessRuleException(String message, String devMessage, boolean hasMultiple) {
        return getBusinessRuleException(null, message, devMessage, null, hasMultiple);
    }

    public static RestApiValidationException getBusinessRuleException(ErrorCode err, RootResourceBean existingResource) {
        return getBusinessRuleException(err.name(), err.getFriendlyMsg(), err.getDeveloperMsg(), existingResource, false);
    }

    public static RestApiValidationException getBusinessRuleException(ErrorCode err, Object[] friendlyArgs, Object[] devArgs, RootResourceBean existingResource) {
        return getBusinessRuleException(err.name(), err.formatFriendlyMsg(friendlyArgs), err.formatDeveloperMsg(devArgs), existingResource, false);
    }

    public static RestApiValidationException getBusinessRuleException(String message, String devMessage, RootResourceBean existingResource) {
        return getBusinessRuleException(null, message, devMessage, existingResource, false);
    }

    public static RestApiValidationException getBusinessRuleException(ErrorCode err) {
        return getBusinessRuleException(err.name(), err.getFriendlyMsg(), err.getDeveloperMsg(), null, false);
    }

    public static RestApiValidationException getBusinessRuleException(ErrorCode err, String message) {
        return getBusinessRuleException(err.name(), message, message, null, false);
    }

    public static RestApiValidationException getBusinessRuleException(ErrorCode err, Object[] friendlyArgs, Object[] devArgs, boolean hasMultiple) {
        return getBusinessRuleException(err.name(), err.formatFriendlyMsg(friendlyArgs), err.formatDeveloperMsg(devArgs), null, hasMultiple);
    }

    public static RestApiValidationException getBusinessRuleException(ErrorCode err, Object[] friendlyArgs, Object[] devArgs) {
        return getBusinessRuleException(err.name(), err.formatFriendlyMsg(friendlyArgs), err.formatDeveloperMsg(devArgs), null, false);
    }

    public static RestApiValidationException getBusinessRuleException(String message, String devMessage) {
        return getBusinessRuleException(null, message, devMessage, null, false);
    }

    public static RestApiValidationException getBusinessRuleException(String message, RootResourceBean existingResource) {
        return getBusinessRuleException(null, message, message, existingResource, false);
    }

    public static RestApiValidationException getBusinessRuleException(String message) {
        return getBusinessRuleException(null, message, message, null, false);
    }

    public static RestApiValidationException getServiceUnavailableException(ErrorCode err, Object[] friendlyArgs, Object[] devArgs) {
        return getServiceUnavailableException(err.name(), err.formatFriendlyMsg(friendlyArgs), err.formatDeveloperMsg(devArgs), null, false);
    }

    public static RestApiValidationException getBadRequestException(ErrorCode err, Object[] friendlyArgs, Object[] devArgs) {
        return new RestApiValidationException(Status.BAD_REQUEST.getStatusCode(), err.name(), err.formatFriendlyMsg(friendlyArgs), err.formatDeveloperMsg(devArgs));
    }

    public static RestApiValidationException getBadRequestException(ErrorCode err) {
        return new RestApiValidationException(Status.BAD_REQUEST.getStatusCode(), err.name(), err.getFriendlyMsg(), err.getDeveloperMsg());
    }

    public static RestApiValidationException getBadRequestException(String message) {
        return new RestApiValidationException(Status.BAD_REQUEST.getStatusCode(), null, message, message);
    }

    public static RestApiValidationException getBadRequestException(String message, String developerMessage) {
        return new RestApiValidationException(Status.BAD_REQUEST.getStatusCode(), null, message, developerMessage);
    }

    public static RestApiValidationException getInternalErrorException(ErrorCode err, Object[] friendlyArgs, Object[] devArgs) {
        return new RestApiValidationException(Status.INTERNAL_SERVER_ERROR.getStatusCode(), err.name(), err.formatFriendlyMsg(friendlyArgs), err.formatDeveloperMsg(devArgs));
    }

    public Integer getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(Integer httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDeveloperMessage() {
        return developerMessage;
    }

    public void setDeveloperMessage(String developerMessage) {
        this.developerMessage = developerMessage;
    }

    public String getMoreInfo() {
        return moreInfo;
    }

    public void setMoreInfo(String moreInfo) {
        this.moreInfo = moreInfo;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public RootResourceBean getExistingResource()
    {
        return existingResource;
    }

    public void setExistingResource(RootResourceBean existingResource)
    {
        this.existingResource = existingResource;
    }

}
