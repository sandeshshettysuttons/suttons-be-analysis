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
import au.com.suttons.notification.model.TemplateSearchParameters;
import au.com.suttons.notification.resource.bean.RequestBean;
import au.com.suttons.notification.resource.bean.ResponseCollectionBean;
import au.com.suttons.notification.resource.bean.ResponseInstanceBean;
import au.com.suttons.notification.resource.bean.RoleBean;
import au.com.suttons.notification.resource.page.PageRequest;
import au.com.suttons.notification.resource.page.Pageable;
import au.com.suttons.notification.security.BaseRequestWrapper;
import au.com.suttons.notification.service.RoleService;

@Path("/{departmentCode}/roles")
@RequestScoped
public class RoleResource extends BaseResource
{
    private static final Logger logger = LoggerFactory.getLogger(RoleResource.class);

    @EJB
    private RoleService roleService;

	@GET @Path("/availableRole")
	@Produces(MediaType.APPLICATION_JSON)
    @ApiRequest
    @RolesAllowed({"ROLE_VIEW"})
    public Object getAvailableRoles(@Context HttpServletRequest httpRequest, @Context HttpServletResponse httpResponse) throws Exception
    {
        ResponseCollectionBean result = new ResponseCollectionBean();
        RequestBean requestBean = ((BaseRequestWrapper)httpRequest).getRequestBean();

        //Construct Pageable instance for collection pagination
        Pageable pageable = new PageRequest(requestBean.getPage(), requestBean.getLimit());

        TemplateSearchParameters searchParameters = TemplateSearchParameters.parse(requestBean);

        result.setPageInfo(this.roleService.getRolesByUser(pageable, searchParameters, requestBean));

        return result;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON) @Produces(MediaType.APPLICATION_JSON)
    @ApiRequest @RolesAllowed({"ROLE_ADD"})       
    public Object postInstance(RoleBean roleBean, @Context HttpServletRequest httpRequest, @Context HttpServletResponse httpResponse)
    {
        RequestBean requestBean = ((BaseRequestWrapper)httpRequest).getRequestBean();

        ResponseInstanceBean result = this.saveRole(null, roleBean, requestBean);
        httpResponse.setStatus(Status.CREATED.getStatusCode());
        return result;
    }

    @PUT @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON) @Produces(MediaType.APPLICATION_JSON)
    @ApiRequest @RolesAllowed({"ROLE_EDIT"})       
    public Object putInstance(@PathParam("id") long id, RoleBean roleBean, @Context HttpServletRequest httpRequest, @Context HttpServletResponse httpResponse)
    {
        RequestBean requestBean = ((BaseRequestWrapper)httpRequest).getRequestBean();

        ResponseInstanceBean result = this.saveRole(id, roleBean, requestBean);
        return result;
    }

    private ResponseInstanceBean saveRole(Long id, RoleBean roleBean, RequestBean requestBean)
    {
        ResponseInstanceBean result = new ResponseInstanceBean();
        result.setRequestBean(requestBean);
        roleBean.setId(id);
        result.setItem(this.roleService.saveRole(roleBean, requestBean));
        return result;
    }

}
