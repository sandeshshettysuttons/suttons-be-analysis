package au.com.suttons.notification.resource;

import au.com.suttons.notification.interceptor.annotation.ApiRequest;
import au.com.suttons.notification.interceptor.annotation.AuditLog;
import au.com.suttons.notification.resource.bean.*;
import au.com.suttons.notification.security.BaseRequestWrapper;
import au.com.suttons.notification.service.SequenceGeneratorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

@Path("/{departmentCode}/sequencegenerator")
@RequestScoped
public class SequenceGeneratorResource extends BaseResource
{
    private static final Logger logger = LoggerFactory.getLogger(SequenceGeneratorResource.class);

    @EJB
    private SequenceGeneratorService sequenceGeneratorService;

	@GET @Path("/{name}")
	@Produces(MediaType.APPLICATION_JSON)
    @ApiRequest
    @AuditLog(template="{user.name} viewed generated sequence number for {result.item.name}.", activityType=AuditLog.Type.VIEW)
    public Object getInstance(@PathParam("name") String name, @Context HttpServletRequest httpRequest, @Context HttpServletResponse httpResponse)
    {
        ResponseInstanceBean result = new ResponseInstanceBean();
        RequestBean requestBean = ((BaseRequestWrapper)httpRequest).getRequestBean();

        SequenceGeneratorBean item = this.sequenceGeneratorService.generateSequenceNumberByName(name);

        result.setRequestBean(requestBean);
        result.setItem(item);

        return result;
    }

}
