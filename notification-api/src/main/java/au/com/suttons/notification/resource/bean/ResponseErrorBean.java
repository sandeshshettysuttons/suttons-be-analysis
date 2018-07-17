package au.com.suttons.notification.resource.bean;

import au.com.suttons.notification.model.ErrorCode;
import au.com.suttons.notification.resource.error.RestApiException;
import au.com.suttons.notification.util.RequestUtil;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response.Status;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.ObjectMapper;

public class ResponseErrorBean implements APIResponse, Serializable
{
    public static final String NOT_FOUND_MSG = "Oops! The resource you requested cannot be found.";
    public static final String NOT_FOUND_DEV_MSG = "Check the API docs for a list of available resources.";
    public static final String INVALID_REFERENCE_FRIENDLY = "It appears that some of the data on the page has changed. Please refresh your browser and try again.";
    public static final String INVALID_REFERENCE_DEVELOPER = "Invalid reference(s): ";
    public static final String INVALID_REFERENCE_MSG_TEMPLATE = "%s with ID %s does not exist;";
    public static final String INVALID_BUSINESS_FRIENDLY = "The requested operation failed for the following reason(s): ";
    public static final String INVALID_BUSINESS_DEVELOPER = "Business rule violation: ";
    public static final String INVALID_DATA_SYNTAX_DEVELOPER = "Invalid data syntax: ";
    public static final String VERSION_MISMATCH_MSG = "Cannot be saved because it has changed or been deleted since it was last read. Please reload the page.";
    public static final String VERSION_MISMATCH_DEV_MSG = "Cannot be saved because it has changed or been deleted since it was last read. Please reload the page.";

    String href;
    Integer httpStatus;
    String code;
    String message;
    String developerMessage;
    String moreInfo;
    HttpServletRequest request;
    List<String> methodInfoList;

    @JsonIgnore
    Throwable exception;

    @JsonIgnore
    HttpServletResponse response;

    public ResponseErrorBean(Integer httpStatus, HttpServletRequest request, HttpServletResponse response, Throwable e)
    {
        this.httpStatus = httpStatus;
        this.response = response;
        this.response.setStatus(httpStatus);

        this.href = RequestUtil.getFullRequestURL(request);
        this.exception = e;

        if(e instanceof RestApiException){
            RestApiException restApiException = (RestApiException)e;
            this.code = restApiException.getCode();
            this.message = restApiException.getMessage();
            this.developerMessage = restApiException.getDeveloperMessage();

            // Set the Content-Location of any CONFLICT responses (the existing resource must be set where the original exception is thrown
            if (restApiException.getHttpStatus().equals(Status.CONFLICT.getStatusCode())) {
                if (restApiException.getExistingResource() != null) {
                    response.setHeader("Content-Location", restApiException.getExistingResource().getHref());
                }
            }
        }else{
            this.code = "";
            this.message = "";

            if(e != null){
                this.developerMessage = "[" + e.getClass().getSimpleName() + "] " + e.getMessage();
            }
        }

    }

    public ResponseErrorBean(Integer httpStatus, HttpServletRequest request, HttpServletResponse response, Throwable e, List<String> methodInfoList)
    {
        this.httpStatus = httpStatus;
        this.request = request;
        this.response = response;
        this.response.setStatus(httpStatus);

        this.href = RequestUtil.getFullRequestURL(request);
        this.exception = e;
        this.methodInfoList = methodInfoList;

        if(e instanceof RestApiException){
            RestApiException restApiException = (RestApiException)e;
            this.code = restApiException.getCode();
            this.message = restApiException.getMessage();
            this.developerMessage = restApiException.getDeveloperMessage();

            // Set the Content-Location of any CONFLICT responses (the existing resource must be set where the original exception is thrown
            if (restApiException.getHttpStatus().equals(Status.CONFLICT.getStatusCode())) {
                if (restApiException.getExistingResource() != null) {
                    response.setHeader("Content-Location", restApiException.getExistingResource().getHref());
                }
            }
        }else{
            this.code = ErrorCode.API_C_001.name();
            this.message = ErrorCode.API_C_001.getFriendlyMsg();

            if(e != null){
                this.developerMessage = "[" + e.getClass().getSimpleName() + "] " + e.getMessage();
            }
        }

    }

    public ResponseErrorBean(HttpServletRequest request, HttpServletResponse response, RestApiException e)
    {
        this(e.getHttpStatus(), request, response, e);
    }

    public ResponseErrorBean(HttpServletRequest request, HttpServletResponse response, RestApiException e, List<String> methodInfoList)
    {
        this(e.getHttpStatus(), request, response, e, methodInfoList);
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public Integer getHttpStatus() {
        return httpStatus;
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

    public Throwable getException() {
        return exception;
    }

    public void setException(Throwable exception) {
        this.exception = exception;
    }

    @JsonIgnore
    public String getAsJSON() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(this) ;
    }
}
