package au.com.suttons.notification.security.pinLogin;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.MDC;

import au.com.suttons.notification.security.BaseRequestWrapper;
import au.com.suttons.notification.security.UserHasNoRolesException;

/**
 * Servlet filter that provides a hook into the request pipeline in order to apply the {@link JossoRequestWrapper}
 * wrapper class.
 */
public class PinSecurityFilter implements Filter
{
    private final String AD_LOGOUT = "/logout";

    public static final String MDC_USER = "user_id";
    public static final String MDC_URL = "url";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        try {
        	//skip role injection for logout request
        	if(AD_LOGOUT.equals(httpRequest.getServletPath())) {
                chain.doFilter(httpRequest, response);
                return;
        	}

            PinRequestWrapper pinRequestWrapper = new PinRequestWrapper(httpRequest);

            String userLogin      = pinRequestWrapper.getRemoteUser();
            MDC.put(MDC_USER, userLogin);
            MDC.put(MDC_URL, 
            		pinRequestWrapper.getRequestURI() + (pinRequestWrapper.getQueryString() != null ? "?"+pinRequestWrapper.getQueryString() : ""));

            request.setAttribute(BaseRequestWrapper.class.getName(), pinRequestWrapper);
            chain.doFilter(httpRequest, response);
        } catch (UserHasNoRolesException ex) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + AD_LOGOUT);
        }
    }

    @Override
    public void destroy() {
    }
}
