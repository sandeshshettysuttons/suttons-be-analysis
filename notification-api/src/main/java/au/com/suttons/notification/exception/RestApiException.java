package au.com.suttons.notification.exception;

import au.com.suttons.notification.resource.bean.ResponseErrorBean;
import org.apache.commons.lang.StringUtils;

/**
 * Indicates that a service operation that creates or updates an entity has rejected
 * the operation because of the state of the supplied entity.  Common causes include
 * out of range values and missing values for mandatory fields.
 */
public class RestApiException extends RuntimeException {

    private ResponseErrorBean responseErrorBean;
            
    public RestApiException() {
        super();
    }

    public RestApiException(String message) {
        super(message);
    }

    public RestApiException(ResponseErrorBean responseErrorBean) {
        super( (responseErrorBean != null && StringUtils.isNotBlank(responseErrorBean.getMessage()))
                ? responseErrorBean.getMessage() 
                : "There is an error while processing your request.");
        this.responseErrorBean = responseErrorBean;
    }

    public RestApiException(String message, Exception inner) {
        super(message, inner);
    }

    public RestApiException(Exception inner) {
        super(inner);
    }

    public ResponseErrorBean getResponseErrorBean() {
        return responseErrorBean;
    }

    public int getStatus() {
        return this.getResponseErrorBean().getHttpStatus();
    }
}
