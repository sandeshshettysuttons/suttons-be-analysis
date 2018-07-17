package au.com.suttons.notification.security;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;

import au.com.suttons.notification.data.dao.UserDao;
import au.com.suttons.notification.data.entity.UserEntity;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.com.suttons.notification.config.ResourceJNDI;
import au.com.suttons.notification.data.dao.TemplateDao;
import au.com.suttons.notification.data.entity.RoleEntity;
import au.com.suttons.notification.data.entity.TemplateEntity;
import au.com.suttons.notification.util.RequestUtil;

/**
 * Wraps a standard HTTP request in order to provide additional security information obtained through AD
 */
public class ActiveDirectoryRequestWrapper extends BaseRequestWrapper
{
    private static final Logger logger = LoggerFactory.getLogger(ActiveDirectoryRequestWrapper.class);

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

    public ActiveDirectoryRequestWrapper(HttpServletRequest request) throws IOException
    {
        super(request);
        this.request = request;
        if (request.getHeader("User-Agent").indexOf("notification-ios") != -1) {
            this.channel = CHANNEL_MOBILE;
        } else {
            this.channel = CHANNEL_WEB;
        }

        if(ServletFileUpload.isMultipartContent(request)){
            this.fileItems = ((FileUploadRequestWrapper) request).getFileItems();
            this.requestMethod = ((FileUploadRequestWrapper) request).getRequestMethod();
        }

        this.principal = new StarUser(request.getUserPrincipal());
        this.injectRoles(request);
    }

    @Override
    public boolean isUserInRole(String role) {
        return this.principal.hasRole(role);
    }

    @Override
    public Principal getUserPrincipal() {
        return this.principal;
    }

    @Override
    public String getRemoteUser() {
        return this.principal.getName();
    }
    private void injectRoles(HttpServletRequest request)
    {
        UserEntity user = this.userDao.findByLoginAndIsActive(this.principal.getName());
        if (user == null) {
            throw new UserHasNoRolesException("Failed to retrieve user's information.");
        }

        this.principal.setUserId(user.getId());
        this.principal.setDisplayName(user.getPrintName());

        //set "SYSTEM_ADMIN" role
        if(user.isIsSystemAdmin()) {
            List<String> roles = new ArrayList<String>();
            roles.add(ROLE_SYSTEM_ADMIN);
            this.principal.setRoles(roles);
            return;
        }

        //exclude public resources
        if(request.getRequestURI().indexOf("api/myProfile") > -1
                || request.getRequestURI().indexOf("api/api-docs") > -1
            	|| request.getRequestURI().indexOf("api/feedback") > -1) {
            return;
        }

        //retrieve roles by the current department
        String departmentCode = RequestUtil.getDepartmentCode(request);
        List<TemplateEntity> templates = this.templateDao.findByDepartmentCode(this.principal.getName(), departmentCode);
        if (templates != null && templates.size() > 0) {

            List<String> roles = new ArrayList<String>();
            for(TemplateEntity templateEntity : templates) {
                for(RoleEntity role : templateEntity.getRoles()) {
                    roles.add(role.getName());
                }
            }
            this.principal.setRoles(roles);
        }
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