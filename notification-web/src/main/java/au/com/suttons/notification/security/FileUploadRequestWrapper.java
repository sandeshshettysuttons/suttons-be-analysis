package au.com.suttons.notification.security;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileUploadRequestWrapper extends HttpServletRequestWrapper {

    private List<FileItem> fileItems = new ArrayList<FileItem>();
    private String requestMethod;

    public FileUploadRequestWrapper(HttpServletRequest request) throws IOException, FileUploadException {
        super(request);

        if (ServletFileUpload.isMultipartContent(request)) {
            List<FileItem> items = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
            for (FileItem item : items) {
                if (item != null && !item.isFormField()) {
                    fileItems.add(item);
                }

                if(item != null && item.getFieldName().equals("_method")){
                    requestMethod = item.getString();
                }
            }
        }
    }

    public List<FileItem> getFileItems() {
        return fileItems;
    }

    public String getRequestMethod() {
        return requestMethod;
    }
}


