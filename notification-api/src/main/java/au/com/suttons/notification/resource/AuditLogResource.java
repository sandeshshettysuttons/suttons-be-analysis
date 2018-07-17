package au.com.suttons.notification.resource;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.com.suttons.notification.interceptor.annotation.ApiRequest;
import au.com.suttons.notification.interceptor.annotation.AuditLog;
import au.com.suttons.notification.model.AuditLogSearchParameters;
import au.com.suttons.notification.resource.bean.AuditLogBean;
import au.com.suttons.notification.resource.bean.RequestBean;
import au.com.suttons.notification.resource.bean.ResponseCollectionBean;
import au.com.suttons.notification.resource.bean.ResponseInstanceBean;
import au.com.suttons.notification.resource.page.PageRequest;
import au.com.suttons.notification.resource.page.Pageable;
import au.com.suttons.notification.security.BaseRequestWrapper;
import au.com.suttons.notification.service.AuditLogService;

@Path("/{departmentCode}/auditLogs")
@RequestScoped
public class AuditLogResource extends BaseResource
{
    private static final Logger logger = LoggerFactory.getLogger(AuditLogResource.class);

    private @EJB AuditLogService auditLogService;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
    @ApiRequest @RolesAllowed({"AUDIT_LOG_LIST","AUDIT_LOG_VIEW"})
    @AuditLog(template="{user.name} viewed a list of audit logs.", activityType=AuditLog.Type.AUDIT)
    public Object getCollection(@Context HttpServletRequest httpRequest, @Context HttpServletResponse httpResponse) throws Exception
    {
        ResponseCollectionBean result = new ResponseCollectionBean();

        RequestBean requestBean = ((BaseRequestWrapper)httpRequest).getRequestBean();
        //Construct Pageable instance for collection pagination
        Pageable pageable = new PageRequest(requestBean.getPage(), requestBean.getLimit(), requestBean.getSort());

        AuditLogSearchParameters searchParameters = AuditLogSearchParameters.parse(requestBean);

        result.setPageInfo(this.auditLogService.getAuditLogs(pageable, searchParameters, requestBean));

        return result;
    }

	@GET @Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
    @ApiRequest @RolesAllowed({"AUDIT_LOG_VIEW"})       
    @AuditLog(template="{user.name} viewed a auditLog {result.item.name}.", activityType=AuditLog.Type.AUDIT)
    public Object getInstance(@PathParam("id") long id, @Context HttpServletRequest httpRequest, @Context HttpServletResponse httpResponse)
    {
        ResponseInstanceBean result = new ResponseInstanceBean();
        RequestBean requestBean = ((BaseRequestWrapper)httpRequest).getRequestBean();

        AuditLogBean item = this.auditLogService.getAuditLog(id, requestBean);

        result.setRequestBean(requestBean);
        result.setItem(item);

        return result;
    }
}
