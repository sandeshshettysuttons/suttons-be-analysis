package au.com.suttons.notification.security.noLogin;

import au.com.suttons.notification.config.ResourceJNDI;
import au.com.suttons.notification.data.dao.UserDao;
import au.com.suttons.notification.data.entity.UserEntity;
import au.com.suttons.notification.security.BaseRequestWrapper;
import au.com.suttons.notification.security.FileUploadRequestWrapper;
import au.com.suttons.notification.security.UserHasNoRolesException;
import au.com.suttons.notification.security.pinLogin.PinUser;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

/**
 * Wraps a standard HTTP request in order to provide additional security information obtained through JOSSO.
 */
public class AdminLoginRequestWrapper extends BaseRequestWrapper
{
    private static final Logger logger = LoggerFactory.getLogger(AdminLoginRequestWrapper.class);

    private List<FileItem> fileItems;

    private UserDao userDao;

    {
        try {
            InitialContext context = new InitialContext();

            this.userDao = (UserDao) context.lookup(ResourceJNDI.userDao.getJndi());
        }
        catch (NamingException nameEx) {
            logger.error("Unable to load an EJB via JNDI.  The bean will not be initialised", nameEx);
        }
    }

    public AdminLoginRequestWrapper(HttpServletRequest request) throws IOException
    {
        super(request);
        this.request = request;
        this.channel = CHANNEL_WEB;

        if(ServletFileUpload.isMultipartContent(request)){
            this.fileItems = ((FileUploadRequestWrapper) request).getFileItems();
            this.requestMethod = ((FileUploadRequestWrapper) request).getRequestMethod();
        }

        this.injectRoles(request);
    }

    @Override
    public boolean isUserInRole(String role) {
    	if(this.principal == null) {
    		return false;
    	}

        return this.principal.hasRole(role);
    }

    @Override
    public Principal getUserPrincipal() {
        return this.principal;
    }

    @Override
    public String getRemoteUser() {
        return this.principal != null ? this.principal.getName() : "";
    }
    private void injectRoles(HttpServletRequest request)
    {

        UserEntity user = this.userDao.findByLoginAndIsActive("system");
        if (user == null) {
            throw new UserHasNoRolesException("Failed to retrieve user's information.");
        }

        this.principal = new PinUser(user);

        this.principal.setUserId(user.getId());
        this.principal.setDisplayName(user.getPrintName());

        //set "SYSTEM_ADMIN" role
        List<String> roles = new ArrayList<String>();
        roles.add(ROLE_SYSTEM_ADMIN);
        this.principal.setRoles(roles);
        return;
    }

    public List<FileItem> getFileItems() {
        return fileItems;
    }

    public static List<FileItem> getFileItems(HttpServletRequest request) {
        return ((AdminLoginRequestWrapper) request).getFileItems();
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