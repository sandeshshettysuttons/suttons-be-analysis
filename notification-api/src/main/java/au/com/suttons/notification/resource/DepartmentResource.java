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

import au.com.suttons.notification.interceptor.annotation.PublicResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.com.suttons.notification.interceptor.annotation.ApiRequest;
import au.com.suttons.notification.interceptor.annotation.AuditLog;
import au.com.suttons.notification.model.DepartmentSearchParameters;
import au.com.suttons.notification.resource.bean.DepartmentBean;
import au.com.suttons.notification.resource.bean.RequestBean;
import au.com.suttons.notification.resource.bean.ResponseCollectionBean;
import au.com.suttons.notification.resource.bean.ResponseInstanceBean;
import au.com.suttons.notification.resource.page.PageRequest;
import au.com.suttons.notification.resource.page.Pageable;
import au.com.suttons.notification.security.BaseRequestWrapper;
import au.com.suttons.notification.service.DepartmentService;

@Path("/departments")
@RequestScoped
public class DepartmentResource extends BaseResource
{
    private static final Logger logger = LoggerFactory.getLogger(DepartmentResource.class);

    private @EJB
    DepartmentService departmentService;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
    @ApiRequest @PublicResource
    @AuditLog(template="{user.name} viewed a list of departments.", activityType=AuditLog.Type.LIST)
    public Object getCollection(@Context HttpServletRequest httpRequest, @Context HttpServletResponse httpResponse) throws Exception
    {
        ResponseCollectionBean result = new ResponseCollectionBean();

        RequestBean requestBean = ((BaseRequestWrapper)httpRequest).getRequestBean();
        //Construct Pageable instance for collection pagination
        Pageable pageable = new PageRequest(requestBean.getPage(), requestBean.getLimit(), requestBean.getSort());

        DepartmentSearchParameters searchParameters = DepartmentSearchParameters.parse(requestBean);

        result.setPageInfo(this.departmentService.getDepartments(pageable, searchParameters, requestBean));

        return result;
    }

	@GET @Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
    @ApiRequest @RolesAllowed({"DEPARTMENT_VIEW"})
    @AuditLog(template="{user.name} viewed a department {result.item.name}.", activityType=AuditLog.Type.VIEW)
    public Object getInstance(@PathParam("id") long id, @Context HttpServletRequest httpRequest, @Context HttpServletResponse httpResponse)
    {
        ResponseInstanceBean result = new ResponseInstanceBean();
        RequestBean requestBean = ((BaseRequestWrapper)httpRequest).getRequestBean();

        DepartmentBean item = this.departmentService.getDepartment(id, requestBean);

        result.setRequestBean(requestBean);
        result.setItem(item);

        return result;
    }

    @POST
  	@Consumes(MediaType.APPLICATION_JSON) @Produces(MediaType.APPLICATION_JSON)
    @ApiRequest @RolesAllowed({"DEPARTMENT_ADD"})
    @AuditLog(template="{user.name} added a new department {bean.name}.", activityType=AuditLog.Type.ADD)
    public Object postInstance(DepartmentBean departmentBean, @Context HttpServletRequest httpRequest, @Context HttpServletResponse httpResponse)
    {
        RequestBean requestBean = ((BaseRequestWrapper)httpRequest).getRequestBean();
        ResponseInstanceBean result = this.saveDepartment(null, departmentBean, requestBean);
        httpResponse.setStatus(Status.CREATED.getStatusCode());
        return result;
    }

    @PUT @Path("/{id}")
  	@Consumes(MediaType.APPLICATION_JSON) @Produces(MediaType.APPLICATION_JSON)
    @ApiRequest @RolesAllowed({"DEPARTMENT_EDIT"})
    @AuditLog(template="{user.name} updated a department {bean.name}.", activityType=AuditLog.Type.UPDATE)
    public Object putInstance(@PathParam("id") long id, DepartmentBean departmentBean, @Context HttpServletRequest httpRequest, @Context HttpServletResponse httpResponse)
    {
        RequestBean requestBean = ((BaseRequestWrapper)httpRequest).getRequestBean();
        ResponseInstanceBean result = this.saveDepartment(id, departmentBean, requestBean);
        return result;
    }

    private ResponseInstanceBean saveDepartment(Long id, DepartmentBean departmentBean, RequestBean requestBean)
    {
        ResponseInstanceBean result = new ResponseInstanceBean();
        result.setRequestBean(requestBean);
        departmentBean.setId(id);
        result.setItem(this.departmentService.saveDepartment(departmentBean, requestBean));
        return result;
    }
}
