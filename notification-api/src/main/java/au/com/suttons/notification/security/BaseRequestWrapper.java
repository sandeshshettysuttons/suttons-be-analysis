package au.com.suttons.notification.security;

import au.com.suttons.notification.resource.bean.RequestBean;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * Wraps a standard HTTP request in order to provide additional security information obtained through JOSSO.
 */
public abstract class BaseRequestWrapper extends HttpServletRequestWrapper
{
	public final static String ROLE_SYSTEM_ADMIN = "SYSTEM_ADMIN";
	public final static String ROLE_USER = "USER";

    public final static String CHANNEL_WEB = "WEB";
    public final static String CHANNEL_MOBILE = "MOBILE";

    protected StarUser principal;
    protected RequestBean requestBean;
    protected HttpServletRequest request;
    protected String requestMethod;
    protected String channel;

    public BaseRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    public static String getRequestMethod(HttpServletRequest request) {
        return ((BaseRequestWrapper) request).getRequestMethod();
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public RequestBean getRequestBean() {
        return requestBean;
    }

    public void setRequestBean(RequestBean requestBean) {
        this.requestBean = requestBean;
    }

    public String getChannel() {
        return channel;
    }
}


