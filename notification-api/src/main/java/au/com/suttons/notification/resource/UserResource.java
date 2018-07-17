package au.com.suttons.notification.resource;

import au.com.suttons.notification.interceptor.annotation.ApiRequest;
import au.com.suttons.notification.interceptor.annotation.AuditLog;
import au.com.suttons.notification.model.UserSearchParameters;
import au.com.suttons.notification.resource.bean.UserBean;
import au.com.suttons.notification.resource.bean.RequestBean;
import au.com.suttons.notification.resource.bean.ResponseCollectionBean;
import au.com.suttons.notification.resource.bean.ResponseInstanceBean;
import au.com.suttons.notification.resource.page.PageRequest;
import au.com.suttons.notification.resource.page.Pageable;
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
import javax.ws.rs.core.Response.Status;

@Path("/{departmentCode}/users")
@RequestScoped
public class UserResource extends BaseResource
{
    private static final Logger logger = LoggerFactory.getLogger(UserResource.class);

    private @EJB UserService userService;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
    @ApiRequest @RolesAllowed({"USER_LIST","USER_VIEW"})
    @AuditLog(template="{user.name} viewed a list of users.", activityType=AuditLog.Type.LIST)
    public Object getCollection(@Context HttpServletRequest httpRequest, @Context HttpServletResponse httpResponse) throws Exception
    {
        ResponseCollectionBean result = new ResponseCollectionBean();

        RequestBean requestBean = ((BaseRequestWrapper)httpRequest).getRequestBean();
        //Construct Pageable instance for collection pagination
        Pageable pageable = new PageRequest(requestBean.getPage(), requestBean.getLimit(), requestBean.getSort());

        UserSearchParameters searchParameters = UserSearchParameters.parse(requestBean);

        result.setPageInfo(this.userService.getUsers(pageable, searchParameters, requestBean));

        return result;
    }

	@GET @Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
    @ApiRequest @RolesAllowed({"USER_VIEW"})
    @AuditLog(template="{user.name} viewed an user {result.item.firstname} {result.item.lastname}.", activityType=AuditLog.Type.VIEW)
    public Object getInstance(@PathParam("id") long id, @Context HttpServletRequest httpRequest, @Context HttpServletResponse httpResponse)
    {
        ResponseInstanceBean result = new ResponseInstanceBean();
        RequestBean requestBean = ((BaseRequestWrapper)httpRequest).getRequestBean();

        UserBean item = this.userService.getUser(id, requestBean);

        result.setRequestBean(requestBean);
        result.setItem(item);

        return result;
    }

    @POST
  	@Consumes(MediaType.APPLICATION_JSON) @Produces(MediaType.APPLICATION_JSON)
    @ApiRequest @RolesAllowed({"USER_ADD"})
    @AuditLog(template="{user.name} added a user {result.item.name}.", activityType=AuditLog.Type.ADD)
    public Object postInstance(UserBean userBean, @Context HttpServletRequest httpRequest, @Context HttpServletResponse httpResponse)
    {
        RequestBean requestBean = ((BaseRequestWrapper)httpRequest).getRequestBean();
        ResponseInstanceBean result = this.saveUser(null, userBean, requestBean, false);
        httpResponse.setStatus(Status.CREATED.getStatusCode());

        return result;
    }

    @PUT @Path("/{id}")
  	@Consumes(MediaType.APPLICATION_JSON) @Produces(MediaType.APPLICATION_JSON)
    @ApiRequest @RolesAllowed({"USER_EDIT"})
    @AuditLog(template="{user.name} updated an user {result.item.firstname} {result.item.lastname}.", activityType=AuditLog.Type.UPDATE)
    public Object putInstance(@PathParam("id") long id, UserBean userBean, @Context HttpServletRequest httpRequest, @Context HttpServletResponse httpResponse)
    {
        RequestBean requestBean = ((BaseRequestWrapper)httpRequest).getRequestBean();
        ResponseInstanceBean result = this.saveUser(id, userBean, requestBean, false);
        return result;
    }

    private ResponseInstanceBean saveUser(Long id, UserBean userBean, RequestBean requestBean, boolean editProfile)
    {
        ResponseInstanceBean result = new ResponseInstanceBean();
        result.setRequestBean(requestBean);
        userBean.setId(id);
        result.setItem(this.userService.saveUser(userBean, requestBean, editProfile));
        return result;
    }
}
