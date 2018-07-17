package au.com.suttons.notification.resource;

import java.io.UnsupportedEncodingException;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import au.com.suttons.notification.data.dao.UserDao;
import au.com.suttons.notification.resource.bean.UserBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.com.suttons.notification.interceptor.annotation.ApiRequest;
import au.com.suttons.notification.interceptor.annotation.AuditLog;
import au.com.suttons.notification.interceptor.annotation.PublicResource;
import au.com.suttons.notification.model.TemplateSearchParameters;
import au.com.suttons.notification.resource.bean.RequestBean;
import au.com.suttons.notification.resource.bean.ResponseCollectionBean;
import au.com.suttons.notification.resource.bean.ResponseInstanceBean;
import au.com.suttons.notification.resource.page.PageRequest;
import au.com.suttons.notification.resource.page.Pageable;
import au.com.suttons.notification.security.BaseRequestWrapper;
import au.com.suttons.notification.service.UserService;
import au.com.suttons.notification.service.TemplateService;

@Path("/myProfile")
@RequestScoped
public class MyProfileResource extends BaseResource
{
    private static final Logger logger = LoggerFactory.getLogger(MyProfileResource.class);

    @EJB
    private UserService userService;
    @EJB
    private TemplateService templateService;
    @EJB
    private UserDao userDao;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
    @ApiRequest @PublicResource
    @AuditLog(template="{user.name} logged in.", activityType=AuditLog.Type.LOGIN)
    public Object getMyProfile(@Context HttpServletRequest httpRequest, @Context HttpServletResponse httpResponse)
    {
        ResponseInstanceBean result = new ResponseInstanceBean();
        RequestBean requestBean = ((BaseRequestWrapper)httpRequest).getRequestBean();

    	logger.info("MyProfileResource : getMyProfile -- currentUsername "+requestBean.getUserName());
        UserBean item = null;

        item = userService.getUserByLogin(requestBean.getUserName(), requestBean);

        requestBean.setMandatoryExpand("user(*)");
        result.setRequestBean(requestBean);
        result.setItem(item);

		return result;
    }

  	@GET
    @Path("/template")
	@Produces(MediaType.APPLICATION_JSON)
    @ApiRequest @PublicResource
    @AuditLog(template="{user.name} get roles.", activityType=AuditLog.Type.LIST)
    public Object getTemplate(@Context HttpServletRequest httpRequest, @Context HttpServletResponse httpResponse) throws Exception
    {
        RequestBean requestBean = ((BaseRequestWrapper)httpRequest).getRequestBean();
        ResponseCollectionBean result =this.getRoles(requestBean);
        return result;
    }

  	@PUT @Path("/logout")
    @ApiRequest @PublicResource
    @AuditLog(template="{user.name} logged out.", activityType=AuditLog.Type.LOGOUT)
    public Object logoutFromDevice(@Context HttpServletRequest httpRequest, @Context HttpServletResponse httpResponse) throws UnsupportedEncodingException
    {
        RequestBean requestBean = ((BaseRequestWrapper)httpRequest).getRequestBean();
        return null;
    }

    private ResponseCollectionBean getRoles(RequestBean requestBean) throws Exception
    {
        ResponseCollectionBean result = new ResponseCollectionBean();

        //Construct Pageable instance for collection pagination
        Pageable pageable = new PageRequest(requestBean.getPage(), requestBean.getLimit());

        TemplateSearchParameters searchParameters = new TemplateSearchParameters();
        searchParameters.setDepartmentCode(requestBean.getQueryParam("departmentCode"));
        searchParameters.setUserName(requestBean.getUserName());

        result.setPageInfo(this.templateService.getTemplates(pageable, searchParameters, requestBean));

        return result;
    }
}
