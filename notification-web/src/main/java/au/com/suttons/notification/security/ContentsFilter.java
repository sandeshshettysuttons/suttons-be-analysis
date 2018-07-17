package au.com.suttons.notification.security;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * Servlet filter that provides a hook into the request pipeline in order to apply the {@link au.com.suttons.notification.security.JossoRequestWrapper}
 * wrapper class.
 */
public class ContentsFilter implements Filter {


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        /**
         * Set up cache headers for main html pages
         */
        String requestURI = httpRequest.getRequestURI().replaceFirst(httpRequest.getContextPath(), "");

//        if (!requestURI.startsWith("/partials")) {
            httpResponse.setHeader("cache-control", "no-cache, no-store");
            httpResponse.setHeader("pragma", "no-cache");
            httpResponse.setDateHeader("Expires", new Date().getTime() + 1000);
//        }

        chain.doFilter(httpRequest, response);
    }

    @Override
    public void destroy() {
    }
}
