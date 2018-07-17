package au.com.suttons.notification.security;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Wraps a standard HTTP request in order to provide additional security information obtained through JOSSO.
 */
public class ActiveDirectoryRequestWrapper extends HttpServletRequestWrapper
{
    private static final Logger logger = LoggerFactory.getLogger(ActiveDirectoryRequestWrapper.class);

    private List<FileItem> fileItems;
    private String requestMethod;

    public ActiveDirectoryRequestWrapper(HttpServletRequest request) throws IOException
    {
        super(request);

        if(ServletFileUpload.isMultipartContent(request)){
            this.fileItems = ((FileUploadRequestWrapper) request).getFileItems();
            this.requestMethod = ((FileUploadRequestWrapper) request).getRequestMethod();
        }
    }

    public static String getRequestMethod(HttpServletRequest request) {
        return ((ActiveDirectoryRequestWrapper) request).getRequestMethod();
    }
    public String getRequestMethod() {
        return requestMethod;
    }

    public List<FileItem> getFileItems() {
        return fileItems;
    }

    public static List<FileItem> getFileItems(HttpServletRequest request) {
        return ((ActiveDirectoryRequestWrapper) request).getFileItems();
    }

    public static FileItem getFileItem(HttpServletRequest request) {
        List<FileItem> items = getFileItems(request);
        if (items != null && items.size() > 0) {
            return items.get(0);
        } else {
            return null;
        }
    }
}