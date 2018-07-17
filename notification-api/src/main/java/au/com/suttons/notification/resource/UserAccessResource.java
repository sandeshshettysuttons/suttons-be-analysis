package au.com.suttons.notification.resource;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.com.suttons.notification.interceptor.annotation.ApiRequest;
import au.com.suttons.notification.interceptor.annotation.AuditLog;
import au.com.suttons.notification.model.UserAccessSearchParameters;
import au.com.suttons.notification.resource.bean.RequestBean;
import au.com.suttons.notification.resource.bean.ResponseCollectionBean;
import au.com.suttons.notification.resource.bean.ResponseInstanceBean;
import au.com.suttons.notification.resource.bean.UserAccessBean;
import au.com.suttons.notification.resource.page.PageRequest;
import au.com.suttons.notification.resource.page.Pageable;
import au.com.suttons.notification.security.BaseRequestWrapper;
import au.com.suttons.notification.service.UserAccessService;

@Path("/{departmentCode}/userAccesses")
@RequestScoped
public class UserAccessResource extends BaseResource
{
    private static final Logger logger = LoggerFactory.getLogger(UserAccessResource.class);

    private @EJB UserAccessService userAccessService;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
    @ApiRequest @RolesAllowed({"USER_ACCESS_LIST","USER_ACCESS_VIEW"})
    @AuditLog(template="{user.name} viewed a list of userAccesses.", activityType=AuditLog.Type.LIST)
    public Object getCollection(@Context HttpServletRequest httpRequest, @Context HttpServletResponse httpResponse) throws Exception
    {
        ResponseCollectionBean result = new ResponseCollectionBean();

        RequestBean requestBean = ((BaseRequestWrapper)httpRequest).getRequestBean();
        //Construct Pageable instance for collection pagination
        Pageable pageable = new PageRequest(requestBean.getPage(), requestBean.getLimit(), requestBean.getSort());

        UserAccessSearchParameters searchParameters = UserAccessSearchParameters.parse(requestBean);

        result.setPageInfo(this.userAccessService.getUserAccesss(pageable, searchParameters, requestBean));

        return result;
    }

	@GET @Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
    @ApiRequest @RolesAllowed({"USER_ACCESS_VIEW"})       
    @AuditLog(template="{user.name} viewed an userAccess {result.item.template.name}.", activityType=AuditLog.Type.VIEW)
    public Object getInstance(@PathParam("id") long id, @Context HttpServletRequest httpRequest, @Context HttpServletResponse httpResponse)
    {
        ResponseInstanceBean result = new ResponseInstanceBean();
        RequestBean requestBean = ((BaseRequestWrapper)httpRequest).getRequestBean();

        UserAccessBean item = this.userAccessService.getUserAccess(id, requestBean);

        result.setRequestBean(requestBean);
        result.setItem(item);

        return result;
    }

    @POST
  	@Consumes(MediaType.APPLICATION_JSON) @Produces(MediaType.APPLICATION_JSON)
    @ApiRequest @RolesAllowed({"USER_ACCESS_ADD"})       
    @AuditLog(template="{user.name} added a new userAccess {result.item.template.name} for {result.item.orgName}.", activityType=AuditLog.Type.ADD)
    public Object postInstance(UserAccessBean userAccessBean, @Context HttpServletRequest httpRequest, @Context HttpServletResponse httpResponse)
    {
        RequestBean requestBean = ((BaseRequestWrapper)httpRequest).getRequestBean();
        ResponseInstanceBean result = this.saveUserAccess(null, userAccessBean, requestBean, false);
        httpResponse.setStatus(Status.CREATED.getStatusCode());
        return result;
    }

    private ResponseInstanceBean saveUserAccess(Long id, UserAccessBean userAccessBean, RequestBean requestBean, boolean editProfile)
    {
        ResponseInstanceBean result = new ResponseInstanceBean();
        result.setRequestBean(requestBean);
        userAccessBean.setId(id);
        result.setItem(this.userAccessService.saveUserAccess(userAccessBean, requestBean, editProfile));
        return result;
    }

    @PUT @Path("/{id}/disable")
    @ApiRequest @RolesAllowed({"USER_ACCESS_DELETE"})       
    @AuditLog(template="{user.name} deleted an userAccess {bean.template.name} for {bean.orgName}.", activityType=AuditLog.Type.DELETE)
    public void disableInstance(@PathParam("id") long id, UserAccessBean userAccessBean, @Context HttpServletRequest httpRequest, @Context HttpServletResponse httpResponse)
    {
        RequestBean requestBean = ((BaseRequestWrapper)httpRequest).getRequestBean();
        this.userAccessService.deleteUserAccess(id, requestBean);
    }
}
