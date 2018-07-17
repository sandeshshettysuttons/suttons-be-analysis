package au.com.suttons.notification.security.noLogin;

import au.com.suttons.notification.security.BaseRequestWrapper;
import au.com.suttons.notification.security.UserHasNoRolesException;
import org.slf4j.MDC;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AdminSecurityFilter implements Filter
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

            AdminLoginRequestWrapper adminLoginRequestWrapper = new AdminLoginRequestWrapper(httpRequest);

            String userLogin      = adminLoginRequestWrapper.getRemoteUser();
            MDC.put(MDC_USER, userLogin);
            MDC.put(MDC_URL, 
            		adminLoginRequestWrapper.getRequestURI() + (adminLoginRequestWrapper.getQueryString() != null ? "?"+ adminLoginRequestWrapper.getQueryString() : ""));

            request.setAttribute(BaseRequestWrapper.class.getName(), adminLoginRequestWrapper);
            chain.doFilter(httpRequest, response);
        } catch (UserHasNoRolesException ex) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + AD_LOGOUT);
        }
    }

    @Override
    public void destroy() {
    }
}
