package au.com.suttons.notification.resource;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.com.suttons.notification.interceptor.annotation.ApiRequest;
import au.com.suttons.notification.interceptor.annotation.AuditLog;
import au.com.suttons.notification.resource.bean.RequestBean;
import au.com.suttons.notification.resource.bean.ResponseCollectionBean;
import au.com.suttons.notification.resource.error.RestApiException;
import au.com.suttons.notification.resource.page.PageRequest;
import au.com.suttons.notification.resource.page.Pageable;
import au.com.suttons.notification.security.BaseRequestWrapper;
import au.com.suttons.notification.service.LookupService;

@Path("/{departmentCode}/lookups")
@RequestScoped
public class LookupResource extends BaseResource
{
    private static final Logger logger = LoggerFactory.getLogger(LookupResource.class);

    private @EJB LookupService lookupService;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
    @ApiRequest @RolesAllowed({"LOOKUP_LIST"})
    @AuditLog(template="{user.name} viewed a list of lookup for type {httpRequest.type}.", activityType=AuditLog.Type.LOOKUP)
    public Object getCollection(@Context HttpServletRequest httpRequest, @Context HttpServletResponse httpResponse) throws Exception
    {
        ResponseCollectionBean result = new ResponseCollectionBean();

        RequestBean requestBean = ((BaseRequestWrapper)httpRequest).getRequestBean();

        String type = requestBean.getQueryParam("type");
        if(type == null) {
            throw RestApiException.getBadRequestException("type is mandatory");
        }

        //Construct Pageable instance for collection pagination
        Pageable pageable = new PageRequest(requestBean.getPage(), requestBean.getLimit(), requestBean.getSort());
        result.setPageInfo(this.lookupService.getLookups(pageable, type, requestBean));

        return result;
    }
}
