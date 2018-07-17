package au.com.suttons.notification.security.pinLogin;

import au.com.suttons.notification.config.ResourceJNDI;
import au.com.suttons.notification.data.dao.UserDao;
import au.com.suttons.notification.data.dao.TemplateDao;
import au.com.suttons.notification.data.entity.RoleEntity;
import au.com.suttons.notification.data.entity.UserEntity;
import au.com.suttons.notification.data.entity.TemplateEntity;
import au.com.suttons.notification.security.BaseRequestWrapper;
import au.com.suttons.notification.security.FileUploadRequestWrapper;
import au.com.suttons.notification.security.UserHasNoRolesException;
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
public class PinRequestWrapper extends BaseRequestWrapper
{
    private static final Logger logger = LoggerFactory.getLogger(PinRequestWrapper.class);

    private List<FileItem> fileItems;

    private UserDao userDao;
    private TemplateDao templateDao;

    {
        try {
            InitialContext context = new InitialContext();

            this.userDao = (UserDao) context.lookup(ResourceJNDI.userDao.getJndi());
            this.templateDao = (TemplateDao) context.lookup(ResourceJNDI.templateDao.getJndi());
        }
        catch (NamingException nameEx) {
            logger.error("Unable to load an EJB via JNDI.  The bean will not be initialised", nameEx);
        }
    }

    public PinRequestWrapper(HttpServletRequest request) throws IOException
    {
        super(request);
        this.request = request;
        this.channel = CHANNEL_WEB;

        if(ServletFileUpload.isMultipartContent(request)){
            this.fileItems = ((FileUploadRequestWrapper) request).getFileItems();
            this.requestMethod = ((FileUploadRequestWrapper) request).getRequestMethod();
        }

        if(request.getRequestURL().indexOf("pinAccess/login") == -1) {
        	this.injectRoles(request);
        }
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
        String pinLoginSessionId = request.getSession().getId();

        if(pinLoginSessionId == null) {
            throw new UserHasNoRolesException("Failed to retrieve user's information: PIN_LOGIN_SESSION_ID is null");
        }

        UserEntity user = this.userDao.findByPinLoginSessionIdAndIsActive(pinLoginSessionId);
        if (user == null) {
            throw new UserHasNoRolesException("Failed to retrieve user's information.");
        }

        //update last pin access
        user.setLastUpdatedBy(user.getId());
        user = this.userDao.saveAndFlush(user);

        this.principal = new PinUser(user);

        this.principal.setUserId(user.getId());
        this.principal.setDisplayName(user.getPrintName());

        //set "SYSTEM_ADMIN" role
        if(user.isIsSystemAdmin()) {
            List<String> roles = new ArrayList<String>();
            roles.add(ROLE_SYSTEM_ADMIN);
            this.principal.setRoles(roles);
            return;
        }

        //retrieve roles by the current site
        TemplateEntity template = this.templateDao.findByName(ROLE_USER);
        if (template != null) {

            List<String> roles = new ArrayList<String>();
            for(RoleEntity role : template.getRoles()) {
                roles.add(role.getName());
            }
            this.principal.setRoles(roles);
        }
    }

    public List<FileItem> getFileItems() {
        return fileItems;
    }

    public static List<FileItem> getFileItems(HttpServletRequest request) {
        return ((PinRequestWrapper) request).getFileItems();
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