package au.com.suttons.notification.resource;

import java.util.ArrayList;
import java.util.List;

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

import au.com.suttons.notification.data.dao.TemplateDao;
import au.com.suttons.notification.data.entity.TemplateEntity;
import au.com.suttons.notification.interceptor.annotation.ApiRequest;
import au.com.suttons.notification.interceptor.annotation.AuditLog;
import au.com.suttons.notification.model.TemplateSearchParameters;
import au.com.suttons.notification.resource.bean.RequestBean;
import au.com.suttons.notification.resource.bean.ResponseCollectionBean;
import au.com.suttons.notification.resource.bean.ResponseInstanceBean;
import au.com.suttons.notification.resource.bean.TemplateBean;
import au.com.suttons.notification.resource.page.PageRequest;
import au.com.suttons.notification.resource.page.Pageable;
import au.com.suttons.notification.security.BaseRequestWrapper;
import au.com.suttons.notification.service.TemplateService;

@Path("/{departmentCode}/templates")
@RequestScoped
public class TemplateResource extends BaseResource
{
    private static final Logger logger = LoggerFactory.getLogger(TemplateResource.class);

    @EJB
    private TemplateService templateService;
    @EJB
    private TemplateDao templateDao;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
    @ApiRequest
    @RolesAllowed({"TEMPLATE_LIST","TEMPLATE_VIEW"})
    @AuditLog(template="{user.name} viewed a list of templates.", activityType=AuditLog.Type.LIST)
    public Object getTemplates(@Context HttpServletRequest httpRequest, @Context HttpServletResponse httpResponse) throws Exception
    {
        ResponseCollectionBean result = new ResponseCollectionBean();
        RequestBean requestBean = ((BaseRequestWrapper)httpRequest).getRequestBean();

        //Construct Pageable instance for collection pagination
        Pageable pageable = new PageRequest(requestBean.getPage(), requestBean.getLimit());

        TemplateSearchParameters searchParameters = TemplateSearchParameters.parse(requestBean);

        result.setPageInfo(this.templateService.getTemplates(pageable, searchParameters, requestBean));

        return result;
    }

    @GET @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiRequest @RolesAllowed({"TEMPLATE_LIST","TEMPLATE_VIEW"})       
    @AuditLog(template="{user.name} viewed a template {result.item.name}.", activityType=AuditLog.Type.VIEW)
    public Object getInstance(@PathParam("id") long id, @Context HttpServletRequest httpRequest, @Context HttpServletResponse httpResponse)
    {
        ResponseInstanceBean result = new ResponseInstanceBean();
        RequestBean requestBean = ((BaseRequestWrapper)httpRequest).getRequestBean();

        TemplateBean item = this.templateService.getTemplateById(id, requestBean);

        result.setRequestBean(requestBean);
        result.setItem(item);

        return result;
    }

	@GET @Path("/available")
	@Produces(MediaType.APPLICATION_JSON)
    @ApiRequest
    @RolesAllowed({"TEMPLATE_LIST","TEMPLATE_VIEW"})
    @AuditLog(template="{user.name} viewed available templates.", activityType=AuditLog.Type.LIST)
    public Object getAvailableTemplates(@Context HttpServletRequest httpRequest, @Context HttpServletResponse httpResponse) throws Exception
    {
        ResponseCollectionBean result = new ResponseCollectionBean();
        RequestBean requestBean = ((BaseRequestWrapper)httpRequest).getRequestBean();

        //Construct Pageable instance for collection pagination
        Pageable pageable = new PageRequest(requestBean.getPage(), requestBean.getLimit(), requestBean.getSort());

        if(requestBean.isSystemAdmin()) {
            TemplateSearchParameters searchParameters = TemplateSearchParameters.parse(requestBean);
            searchParameters.setDepartmentCode(null);
            result.setPageInfo(this.templateService.getTemplates(pageable, searchParameters, requestBean));
        } else {
	        List<TemplateEntity> userTemplates = this.templateDao.findByDepartmentCode(requestBean.getUserName(), requestBean.getDepartmentCode());
	        List<TemplateSearchParameters.LowerAccessLevelRange> ranges = new ArrayList<TemplateSearchParameters.LowerAccessLevelRange>();
	        for(TemplateEntity userTemplate : userTemplates) {
	            TemplateSearchParameters.LowerAccessLevelRange range = new TemplateSearchParameters().new LowerAccessLevelRange(userTemplate.getLowerAccessLevelFrom(), userTemplate.getLowerAccessLevelTo());
	            ranges.add(range);
	        }
	
	        result.setPageInfo(this.templateService.getTemplatesByLowerAccessLevel(pageable, ranges, requestBean));
        }

        return result;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON) @Produces(MediaType.APPLICATION_JSON)
    @ApiRequest @RolesAllowed({"TEMPLATE_ADD"})       
    @AuditLog(template="{user.name} added a template {bean.name}.", activityType=AuditLog.Type.ADD)
    public Object postInstance(TemplateBean templateBean, @Context HttpServletRequest httpRequest, @Context HttpServletResponse httpResponse)
    {
        RequestBean requestBean = ((BaseRequestWrapper)httpRequest).getRequestBean();

        ResponseInstanceBean result = this.saveTemplate(null, templateBean, requestBean);
        httpResponse.setStatus(Status.CREATED.getStatusCode());
        return result;
    }

    @PUT @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON) @Produces(MediaType.APPLICATION_JSON)
    @ApiRequest @RolesAllowed({"TEMPLATE_EDIT"})       
    @AuditLog(template="{user.name} updated a template {bean.name}.", activityType=AuditLog.Type.UPDATE)
    public Object putInstance(@PathParam("id") long id, TemplateBean templateBean, @Context HttpServletRequest httpRequest, @Context HttpServletResponse httpResponse)
    {
        RequestBean requestBean = ((BaseRequestWrapper)httpRequest).getRequestBean();

        ResponseInstanceBean result = this.saveTemplate(id, templateBean, requestBean);
        return result;
    }

    private ResponseInstanceBean saveTemplate(Long id, TemplateBean templateBean, RequestBean requestBean)
    {
        ResponseInstanceBean result = new ResponseInstanceBean();
        result.setRequestBean(requestBean);
        templateBean.setId(id);
        result.setItem(this.templateService.saveTemplate(templateBean, requestBean));
        return result;
    }

}
