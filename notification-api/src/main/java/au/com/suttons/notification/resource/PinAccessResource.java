package au.com.suttons.notification.resource;

import au.com.suttons.notification.data.dao.UserDao;
import au.com.suttons.notification.interceptor.annotation.ApiRequest;
import au.com.suttons.notification.interceptor.annotation.AuditLog;
import au.com.suttons.notification.interceptor.annotation.PublicResource;
import au.com.suttons.notification.resource.bean.PinAccessBean;
import au.com.suttons.notification.resource.bean.RequestBean;
import au.com.suttons.notification.resource.bean.ResponseInstanceBean;
import au.com.suttons.notification.resource.bean.UserBean;
import au.com.suttons.notification.security.BaseRequestWrapper;
import au.com.suttons.notification.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

@Path("/pinAccess")
@RequestScoped
public class PinAccessResource extends BaseResource
{
    private static final Logger logger = LoggerFactory.getLogger(PinAccessResource.class);

    private @EJB
    UserService userService;

    private @EJB
    UserDao userDao;

    @POST @Path("/login")
  	@Consumes(MediaType.APPLICATION_JSON) @Produces(MediaType.APPLICATION_JSON)
    @ApiRequest @PublicResource
    @AuditLog(template="{result.item.name} logged in by pin.", activityType=AuditLog.Type.LOGIN)
    public Object postInstance(PinAccessBean pinAccessBean, @Context HttpServletRequest httpRequest, @Context HttpServletResponse httpResponse)
    {
        RequestBean requestBean = ((BaseRequestWrapper)httpRequest).getRequestBean();
        ResponseInstanceBean result = new ResponseInstanceBean();
        result.setItem(
        			this.userService.getUserByPinLogin(
        						pinAccessBean.getTrainingId(), 
        						pinAccessBean.getPin(),
        						httpRequest.getSession().getId(),
        						requestBean));
        return result;
    }

	@GET @Path("/users")
	@Produces(MediaType.APPLICATION_JSON)
    @ApiRequest @RolesAllowed({"USER_VIEW","MYPROFILE_VIEW"})
    @AuditLog(template="{user.name} viewed an user {result.item.name}.", activityType=AuditLog.Type.VIEW)
    public Object getInstance(@Context HttpServletRequest httpRequest, @Context HttpServletResponse httpResponse)
    {
        ResponseInstanceBean result = new ResponseInstanceBean();
        RequestBean requestBean = ((BaseRequestWrapper)httpRequest).getRequestBean();

        UserBean item = this.userService.getUser(requestBean.getUserId(), requestBean);

        result.setRequestBean(requestBean);
        result.setItem(item);

        return result;
    }

}
