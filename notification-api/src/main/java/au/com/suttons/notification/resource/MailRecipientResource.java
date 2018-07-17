package au.com.suttons.notification.resource;

import au.com.suttons.notification.interceptor.annotation.ApiRequest;
import au.com.suttons.notification.interceptor.annotation.AuditLog;
import au.com.suttons.notification.interceptor.annotation.PublicResource;
import au.com.suttons.notification.model.MailRecipientSearchParameters;
import au.com.suttons.notification.resource.bean.MailRecipientBean;
import au.com.suttons.notification.resource.bean.RequestBean;
import au.com.suttons.notification.resource.bean.ResponseCollectionBean;
import au.com.suttons.notification.resource.bean.ResponseInstanceBean;
import au.com.suttons.notification.resource.page.PageRequest;
import au.com.suttons.notification.resource.page.Pageable;
import au.com.suttons.notification.security.BaseRequestWrapper;
import au.com.suttons.notification.service.MailRecipientService;
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

@Path("/mailRecipients")
@RequestScoped
public class MailRecipientResource extends BaseResource
{
    private static final Logger logger = LoggerFactory.getLogger(MailRecipientResource.class);

    private @EJB
    MailRecipientService mailRecipientService;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
    @ApiRequest @PublicResource
    @AuditLog(template="{user.name} viewed a list of mailRecipients.", activityType=AuditLog.Type.LIST)
    public Object getCollection(@Context HttpServletRequest httpRequest, @Context HttpServletResponse httpResponse) throws Exception
    {
        ResponseCollectionBean result = new ResponseCollectionBean();

        RequestBean requestBean = ((BaseRequestWrapper)httpRequest).getRequestBean();
        //Construct Pageable instance for collection pagination
        Pageable pageable = new PageRequest(requestBean.getPage(), requestBean.getLimit(), requestBean.getSort());

        MailRecipientSearchParameters searchParameters = MailRecipientSearchParameters.parse(requestBean);

        result.setPageInfo(this.mailRecipientService.getMailRecipients(pageable, searchParameters, requestBean));

        return result;
    }

	@GET @Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
    @ApiRequest @RolesAllowed({"MAILRECIPIENT_VIEW"})
    @AuditLog(template="{user.name} viewed a mailRecipient {result.item.name}.", activityType=AuditLog.Type.VIEW)
    public Object getInstance(@PathParam("id") long id, @Context HttpServletRequest httpRequest, @Context HttpServletResponse httpResponse)
    {
        ResponseInstanceBean result = new ResponseInstanceBean();
        RequestBean requestBean = ((BaseRequestWrapper)httpRequest).getRequestBean();

        MailRecipientBean item = this.mailRecipientService.getMailRecipient(id, requestBean);

        result.setRequestBean(requestBean);
        result.setItem(item);

        return result;
    }

    @POST
  	@Consumes(MediaType.APPLICATION_JSON) @Produces(MediaType.APPLICATION_JSON)
    @ApiRequest @RolesAllowed({"MAILRECIPIENT_ADD"})
    @AuditLog(template="{user.name} added a new mailRecipient {bean.name}.", activityType=AuditLog.Type.ADD)
    public Object postInstance(MailRecipientBean mailRecipientBean, @Context HttpServletRequest httpRequest, @Context HttpServletResponse httpResponse)
    {
        RequestBean requestBean = ((BaseRequestWrapper)httpRequest).getRequestBean();
        ResponseInstanceBean result = this.saveMailRecipient(null, mailRecipientBean, requestBean);
        httpResponse.setStatus(Status.CREATED.getStatusCode());
        return result;
    }

    @PUT @Path("/{id}")
  	@Consumes(MediaType.APPLICATION_JSON) @Produces(MediaType.APPLICATION_JSON)
    @ApiRequest @RolesAllowed({"MAILRECIPIENT_EDIT"})
    @AuditLog(template="{user.name} updated a mailRecipient {bean.name}.", activityType=AuditLog.Type.UPDATE)
    public Object putInstance(@PathParam("id") long id, MailRecipientBean mailRecipientBean, @Context HttpServletRequest httpRequest, @Context HttpServletResponse httpResponse)
    {
        RequestBean requestBean = ((BaseRequestWrapper)httpRequest).getRequestBean();
        ResponseInstanceBean result = this.saveMailRecipient(id, mailRecipientBean, requestBean);
        return result;
    }

    private ResponseInstanceBean saveMailRecipient(Long id, MailRecipientBean mailRecipientBean, RequestBean requestBean)
    {
        ResponseInstanceBean result = new ResponseInstanceBean();
        result.setRequestBean(requestBean);
        mailRecipientBean.setId(id);
        result.setItem(this.mailRecipientService.saveMailRecipient(mailRecipientBean, requestBean));
        return result;
    }
}
