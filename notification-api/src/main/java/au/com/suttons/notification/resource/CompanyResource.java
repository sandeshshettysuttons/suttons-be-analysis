package au.com.suttons.notification.resource;

import au.com.suttons.notification.interceptor.annotation.ApiRequest;
import au.com.suttons.notification.interceptor.annotation.AuditLog;
import au.com.suttons.notification.interceptor.annotation.PublicResource;
import au.com.suttons.notification.model.CompanySearchParameters;
import au.com.suttons.notification.resource.bean.CompanyBean;
import au.com.suttons.notification.resource.bean.RequestBean;
import au.com.suttons.notification.resource.bean.ResponseCollectionBean;
import au.com.suttons.notification.resource.bean.ResponseInstanceBean;
import au.com.suttons.notification.resource.page.PageRequest;
import au.com.suttons.notification.resource.page.Pageable;
import au.com.suttons.notification.security.BaseRequestWrapper;
import au.com.suttons.notification.service.CompanyService;
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

@Path("/companies")
@RequestScoped
public class CompanyResource extends BaseResource
{
    private static final Logger logger = LoggerFactory.getLogger(CompanyResource.class);

    private @EJB
    CompanyService companyService;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
    @ApiRequest @PublicResource
    @AuditLog(template="{user.name} viewed a list of companys.", activityType=AuditLog.Type.LIST)
    public Object getCollection(@Context HttpServletRequest httpRequest, @Context HttpServletResponse httpResponse) throws Exception
    {
        ResponseCollectionBean result = new ResponseCollectionBean();

        RequestBean requestBean = ((BaseRequestWrapper)httpRequest).getRequestBean();
        //Construct Pageable instance for collection pagination
        Pageable pageable = new PageRequest(requestBean.getPage(), requestBean.getLimit(), requestBean.getSort());

        CompanySearchParameters searchParameters = CompanySearchParameters.parse(requestBean);

        result.setPageInfo(this.companyService.getCompanys(pageable, searchParameters, requestBean));

        return result;
    }

	@GET @Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
    @ApiRequest @RolesAllowed({"COMPANY_VIEW"})
    @AuditLog(template="{user.name} viewed a company {result.item.name}.", activityType=AuditLog.Type.VIEW)
    public Object getInstance(@PathParam("id") long id, @Context HttpServletRequest httpRequest, @Context HttpServletResponse httpResponse)
    {
        ResponseInstanceBean result = new ResponseInstanceBean();
        RequestBean requestBean = ((BaseRequestWrapper)httpRequest).getRequestBean();

        CompanyBean item = this.companyService.getCompany(id, requestBean);

        result.setRequestBean(requestBean);
        result.setItem(item);

        return result;
    }

    @POST
  	@Consumes(MediaType.APPLICATION_JSON) @Produces(MediaType.APPLICATION_JSON)
    @ApiRequest @RolesAllowed({"COMPANY_ADD"})
    @AuditLog(template="{user.name} added a new company {bean.name}.", activityType=AuditLog.Type.ADD)
    public Object postInstance(CompanyBean companyBean, @Context HttpServletRequest httpRequest, @Context HttpServletResponse httpResponse)
    {
        RequestBean requestBean = ((BaseRequestWrapper)httpRequest).getRequestBean();
        ResponseInstanceBean result = this.saveCompany(null, companyBean, requestBean);
        httpResponse.setStatus(Status.CREATED.getStatusCode());
        return result;
    }

    @PUT @Path("/{id}")
  	@Consumes(MediaType.APPLICATION_JSON) @Produces(MediaType.APPLICATION_JSON)
    @ApiRequest @RolesAllowed({"COMPANY_EDIT"})
    @AuditLog(template="{user.name} updated a company {bean.name}.", activityType=AuditLog.Type.UPDATE)
    public Object putInstance(@PathParam("id") long id, CompanyBean companyBean, @Context HttpServletRequest httpRequest, @Context HttpServletResponse httpResponse)
    {
        RequestBean requestBean = ((BaseRequestWrapper)httpRequest).getRequestBean();
        ResponseInstanceBean result = this.saveCompany(id, companyBean, requestBean);
        return result;
    }

    private ResponseInstanceBean saveCompany(Long id, CompanyBean companyBean, RequestBean requestBean)
    {
        ResponseInstanceBean result = new ResponseInstanceBean();
        result.setRequestBean(requestBean);
        companyBean.setId(id);
        result.setItem(this.companyService.saveCompany(companyBean, requestBean));
        return result;
    }
}
