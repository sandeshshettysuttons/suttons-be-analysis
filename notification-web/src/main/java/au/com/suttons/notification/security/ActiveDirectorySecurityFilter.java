package au.com.suttons.notification.security;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet filter that provides a hook into the request pipeline in order to apply the {@link JossoRequestWrapper}
 * wrapper class.
 */
public class ActiveDirectorySecurityFilter implements Filter
{
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        ActiveDirectoryRequestWrapper activeDirectoryRequestWrapper = new ActiveDirectoryRequestWrapper(httpRequest);
        chain.doFilter(activeDirectoryRequestWrapper, response);
}

    @Override
    public void destroy() {
    }
}
