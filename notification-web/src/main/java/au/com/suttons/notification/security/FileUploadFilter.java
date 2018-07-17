package au.com.suttons.notification.security;

import org.apache.commons.fileupload.FileUploadException;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FileUploadFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        FileUploadRequestWrapper fileUploadRequestWrapper = null;
        try {
            fileUploadRequestWrapper = new FileUploadRequestWrapper(httpRequest);
        } catch (FileUploadException e) {
            e.printStackTrace();
        }
        chain.doFilter(fileUploadRequestWrapper, response);
    }

    @Override
    public void destroy() {
    }
}
