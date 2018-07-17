package au.com.suttons.notification.interceptor;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.security.RolesAllowed;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import au.com.suttons.notification.interceptor.annotation.ApiRequest;
import au.com.suttons.notification.interceptor.annotation.AuditLog;
import au.com.suttons.notification.interceptor.annotation.DownloadResource;
import au.com.suttons.notification.interceptor.annotation.PublicResource;
import au.com.suttons.notification.mapper.ResourceAlias;
import au.com.suttons.notification.model.ErrorCode;
import au.com.suttons.notification.resource.BaseResource;
import au.com.suttons.notification.resource.bean.APIResource;
import au.com.suttons.notification.resource.bean.APIResponse;
import au.com.suttons.notification.resource.bean.RequestBean;
import au.com.suttons.notification.resource.bean.ResponseCollectionBean;
import au.com.suttons.notification.resource.bean.ResponseDeleteBean;
import au.com.suttons.notification.resource.bean.ResponseErrorBean;
import au.com.suttons.notification.resource.bean.ResponseInstanceBean;
import au.com.suttons.notification.resource.bean.RootResourceBean;
import au.com.suttons.notification.resource.error.RestApiException;
import au.com.suttons.notification.security.BaseRequestWrapper;
import au.com.suttons.notification.security.StarUser;
import au.com.suttons.notification.util.GsonFactory;
import au.com.suttons.notification.util.HttpUtils;
import au.com.suttons.notification.util.ListUtil;
import au.com.suttons.notification.util.ObjectUtil;
import au.com.suttons.notification.util.RequestUtil;
import au.com.suttons.notification.validator.RequestValidator;

@ApiRequest
@Interceptor
public class ApiInterceptor implements Serializable {

	private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(ApiInterceptor.class);

	public ApiInterceptor() {
	}

    @AroundInvoke
    public Object process(InvocationContext invocationContext) throws Exception  {

        PublicResource publicResourceAnnotation = invocationContext.getMethod().getAnnotation(PublicResource.class);
        ApiParams params = new ApiParams(invocationContext, (publicResourceAnnotation != null));

        try {

        	logger.debug(
        				"Incoming request: " 
        					+ params.getHttpRequest().getRequestURI() 
        					+ (params.getHttpRequest().getQueryString() != null ? "?"+params.getHttpRequest().getQueryString() : ""));

            RequestBean requestBean = new RequestBean();
            params.mapRequestParameters(requestBean, (publicResourceAnnotation != null));

            if(publicResourceAnnotation == null) {
                this.checkAuthorisation(params, invocationContext, requestBean);
            }

            ((BaseRequestWrapper)params.getHttpRequest()).setRequestBean(requestBean);

            APIResponse result;

            // If the user is requesting a custom status then skip the execution of the
            // advised method, and inject the custom response code
            if (requestBean.getStatus() != null)
            {
                int httpStatus = requestBean.getStatus();
                List<Integer> httpStatuses = Arrays.asList(400, 401, 403, 404, 405, 409, 500, 503);
                String developerMessage = "Forced HTTP Status requested by client. Not a real error in API. To be used only for testing.";
                if (httpStatuses.contains(httpStatus))
                {
                    String message = "Oops, something went wrong!";
                    throw new RestApiException(httpStatus, "", message, developerMessage);
                }
                else
                {
                    throw new RestApiException(Status.BAD_REQUEST.getStatusCode(), "", "Requested forced status '" + httpStatus + "' is not supported.", developerMessage);
                }

            }
            // Otherwise, execute the advised method as normal
            else {
                // Get the expand query before we execute the resource method to prevent picking up application-added expand queries
                String usersExpandQuery = RequestValidator.validateExpandQuery(requestBean.getExpand(), params.getHttpRequest());

                DownloadResource downloadResourceAnnotation = invocationContext.getMethod().getAnnotation(DownloadResource.class);
                if(downloadResourceAnnotation != null) {
                    return this.processDownloadResource(invocationContext, params, requestBean);
                }

                Object rawResponse = invocationContext.proceed();


                if(rawResponse == null){
                    //audit log
                    new AuditLogger().log(invocationContext.getMethod().getAnnotation(AuditLog.class), params, null, requestBean);
                    return "{}";
                }

                if (!(rawResponse instanceof APIResponse)) {
                    throw new RestApiException(Status.INTERNAL_SERVER_ERROR.getStatusCode(), null, null, String.format("The method return type must be APIResource: %s", invocationContext.getMethod().getReturnType().toString()));
                }
                result = (APIResponse) rawResponse;

                //audit log
                new AuditLogger().log(invocationContext.getMethod().getAnnotation(AuditLog.class), params, result, requestBean);

                // If the result was an instance bean, then attempt to include the expand param in the HREF of the item
                if (result instanceof ResponseInstanceBean)
                {
                    APIResource item = ((ResponseInstanceBean) result).getItem();

                    if (item != null)
                    {
                        if (item instanceof RootResourceBean)
                        {
                            RootResourceBean rootItem = (RootResourceBean) item;
                            if (usersExpandQuery != null && !usersExpandQuery.equals(""))
                            {
                                if (rootItem.getHref() != null)
                                {
                                    rootItem.setHref(rootItem.getHref() + "?expand=" + usersExpandQuery);
                                }
                            }
                        }
                    }
                }
            }

            //Inject URLs into the response
            if (result instanceof ResponseCollectionBean)
            {
                this.injectURLsToResponse(invocationContext, (ResponseCollectionBean) result);
            }

            BaseResource resourceTarget = (BaseResource) invocationContext.getTarget();

            if (requestBean.getDonExcludedResourceFields() == null) {
                requestBean.setDonExcludedResourceFields(resourceTarget.getExcludedFields());
            }

            //Filter fields that need to be displayed in json result
            String json = this.filterJsonObjectFields(result, requestBean);

            if(json == null) {
                throw new RestApiException(Status.NOT_FOUND.getStatusCode(), null, ResponseErrorBean.NOT_FOUND_MSG, ResponseErrorBean.NOT_FOUND_DEV_MSG, null);
            }

            this.setResponseHeaders(params.getHttpResponse(), requestBean.getCacheDuration());

            return json;
        } catch (Throwable e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            ResponseErrorBean responseErrorBean = new ExceptionHandler().handleException(e, params.getHttpRequest(), params.getHttpResponse());

	    	params.getHttpResponse().setContentType(MediaType.APPLICATION_JSON);
            params.getHttpResponse().setStatus(responseErrorBean.getHttpStatus());

			new ObjectMapper().writeValue(params.getHttpResponse().getWriter(), responseErrorBean);
	    	params.getHttpResponse().flushBuffer();
            return null;
        }
    }

    public byte[] processDownloadResource(InvocationContext invocationContext, ApiParams params, RequestBean requestBean) throws Exception
    {
        HttpServletResponse response = params.getHttpResponse();

        try
        {
            Object rawResponse = invocationContext.proceed();
            
            byte[] item = (byte[]) rawResponse;

            ServletOutputStream outputStream = response.getOutputStream();

            this.setResponseHeaders(response, requestBean.getCacheDuration());

            outputStream.write(item);
            outputStream.flush();
            response.flushBuffer();

            new AuditLogger().log(invocationContext.getMethod().getAnnotation(AuditLog.class), params, null, requestBean);
        }
        catch (IOException ioException)
        {
            logger.error("Failed to retrieve person photo.", ioException);
        }
        catch(RestApiException apiException)
        {
            if(apiException.getHttpStatus() == Status.NOT_FOUND.getStatusCode()) {
                return null;
            }
            
            throw apiException;
        }

        return null;
    }

    public void injectURLsToResponse(InvocationContext invocationContext, ResponseCollectionBean responseCollectionBean)
    {
        HttpServletRequest request = ObjectUtil.getFirstObjectOfType(invocationContext.getParameters(), HttpServletRequest.class);

        //If we couldn't find a request object to work with, then log it and return
        if (request == null) {
            logger.debug("No HttpServletRequest argument was found when attempting to add information to a ResponseCollectionBean. " +
                    "The HREF will not be available in the response as there is no request object to retrieve the URL from");

            return;
        }

        String href = RequestUtil.getFullRequestURL(request);

        //Inject the URLs
        responseCollectionBean.setHref(href);
    }

    private String filterJsonObjectFields(Object responseBean, RequestBean requestBean) throws IOException
    {
        String result;

        Class rootResourceClass = this.getRootResourceClass(responseBean);

        /* custom fields (provided by API consumer) */
        List<String> includedFields = this.fromFieldNameMapToList(rootResourceClass, requestBean.getExpandResourceFields());
        List<String> excludedFields = this.fromFieldNameMapToList(null, requestBean.getExcludedResourceFields()); //Get the fields that need to be displayed from resource and fields

        /* default fields (always included) */
        includedFields.add("*.href");
        includedFields.add("*.id");
        includedFields.add("ResponseCollectionBean.*");
        includedFields.add("ResponseInstanceBean.*");
        includedFields.add("ResponseDeleteBean.*");
        includedFields.add("ResponseTimeEntryReportBean.*");

        //Serialize object to json
        Gson gson = GsonFactory.build(includedFields, excludedFields, rootResourceClass);
        JsonElement responseJSONElement;

        if (responseBean instanceof ResponseInstanceBean)
        {
            APIResource item = ((ResponseInstanceBean) responseBean).getItem();

            if (item == null) {
                return null;
            }

            responseJSONElement = gson.toJsonTree(item);
            result = responseJSONElement.toString();
        }
        else if (responseBean instanceof ResponseDeleteBean)
        {
            responseJSONElement = gson.toJsonTree(responseBean);
            result = responseJSONElement.toString();
        }
        else //responseCollectionBean instanceof ResponseCollectionBean
        {
            responseJSONElement = gson.toJsonTree(responseBean);
            result = responseJSONElement.toString();
        }

        return result;
    }

    private void setResponseHeaders(HttpServletResponse response, int cacheMinutes)
    {
        if (cacheMinutes <= 0)
        {
            // Avoid caching
            response.setHeader("Cache-control", "no-cache, no-store");
            response.setHeader("Pragma", "no-cache");
        }
        else
        {
            // Specify expiry of caching
            response.setHeader("Cache-control", "public, max-age=" + (cacheMinutes * 60));
            response.setHeader("Pragma", "cache");
            response.setDateHeader("Expires", new Date().getTime() + (cacheMinutes * 60 * 1000));
        }
    }

    /**
     * Gets an instance of the response resource from a given response object
     *
     * @param responseObject Must be of type {@link ResponseInstanceBean} or {@link ResponseCollectionBean}
     *
     * @return An instance of the response resource
     */
    public Class getRootResourceClass(Object responseObject)
    {
        APIResource item = null;

        // Get the type of the return object
        if (responseObject instanceof ResponseInstanceBean) {
            item = ((ResponseInstanceBean) responseObject).getItem();
        }
        else if (responseObject instanceof ResponseCollectionBean) {
            List<RootResourceBean> items = ((ResponseCollectionBean) responseObject).getItems();

            if (items != null && items.size() > 0) {
                item = items.get(0);
            }
        }
        
        if (item != null) {
            return item.getClass();
        }

        return null;
    }

    /**
     * Converts a given Map of (resourceName, fieldName) values to a list of fields. This is used to convert a Map of
     * expand or exclude fields into a List to be used with the GSON Exclude Strategy.
     *
     * You can specify the root resource class to enable adding a list of implied included fields.
     * For Example: The client sends this query on an Appointment resource: expand=customer(*), this method will automatically
     * insert included fields as if the query looked like this: expand=repairOrder(customer)+customer(*).
     *
     * @param fieldMap - The Map of expand/exclude resourceName/fields
     * @param rootResourceClass - A {@link Class} object representing the root resource item
     *
     * @return List
     */
    private List<String> fromFieldNameMapToList(Class rootResourceClass, Map<String, String> fieldMap)
    {
        List<String> fieldList = new ArrayList<String>();

        if (fieldMap != null) {
            for (Map.Entry<String, String> entry : fieldMap.entrySet()) {
                String className = ResourceAlias.getClassName(entry.getKey());
                String fieldNames = entry.getValue();

                if (rootResourceClass != null) {
                    String responseClassName = rootResourceClass.getSimpleName();

                    for (Field responseField : rootResourceClass.getDeclaredFields()) {
                        String fieldType = (responseField.getType().equals(List.class)) ? ListUtil.getGenericTypeOfList(responseField) : responseField.getType().getSimpleName();

                        if (fieldType.equals(className)) {
                            String impliedIncludedField = String.format("%s.%s", responseClassName, responseField.getName());

                            if (!fieldList.contains(impliedIncludedField)) {
                                fieldList.add(impliedIncludedField);
                            }
                        }
                    }
                }

                for (String fieldName : fieldNames.split(",")) {
                    fieldList.add(className + "." + fieldName.trim());
                }
            }
        }

        return fieldList;
    }

    public void checkAuthorisation(ApiParams params, InvocationContext invocationContext, RequestBean requestBean) throws Exception {
    	
        //SYSTEM_ADMIN can access any resources
        if(params.getHttpRequest().isUserInRole(BaseRequestWrapper.ROLE_SYSTEM_ADMIN)) {
            return;
        }
   	
        RolesAllowed rolesAllowed = invocationContext.getMethod().getAnnotation(RolesAllowed.class);
        if(rolesAllowed == null){
            Class<?> clazz = invocationContext.getMethod().getDeclaringClass();
            rolesAllowed = (RolesAllowed)clazz.getAnnotation(RolesAllowed.class);
        }

        String[] roles = rolesAllowed.value();

        //check with the logged user permissions
        for(String role : roles){
            if(params.getHttpRequest().isUserInRole(role)){
                return;
            }
        }

        throw RestApiException.getAccessRuleException(ErrorCode.DT_B_003, new Object[]{}, new Object[]{params.getUserName()});
    }

    protected class ApiParams {
    	private HttpServletRequest httpRequest;
    	private HttpServletResponse httpResponse;
    	private RootResourceBean bean;
    	private String userName;
    	private String departmentCode;

		public ApiParams(InvocationContext invocationContext, boolean isPublicResource) throws RestApiException {
			
            Object[] methodParams = invocationContext.getParameters();
			for(int index=0;index<methodParams.length;index++) {

				Object methodParam = methodParams[index];

				//1. httpRequest
				if(methodParam instanceof HttpServletRequest) {
					this.httpRequest = (BaseRequestWrapper) ((HttpServletRequest)methodParam).getAttribute(BaseRequestWrapper.class.getName());
					methodParams[index] = this.httpRequest;
				}
	
				//2. httpResponse
				if(methodParam instanceof HttpServletResponse) {
					this.httpResponse = (HttpServletResponse)methodParam;
				}

				//3. bean
				if(methodParam instanceof RootResourceBean) {
					this.bean = (RootResourceBean)methodParam;
				}
			}

            if (!(this.httpRequest != null || this.httpResponse != null)) {
                throw new RestApiException(Status.INTERNAL_SERVER_ERROR.getStatusCode(), null, null, String.format("The method should provide HttpRequest and HttpResponse as paremeters.: %s", invocationContext.getMethod().toString()));
            }

	        //3. userId
            this.userName = this.getHttpRequest().getRemoteUser();

	        //4. siteId (public resource does not have site id in the url)
            if(!isPublicResource) {
                this.departmentCode = RequestUtil.getDepartmentCode(httpRequest);
            }
		}

        public void mapRequestParameters(RequestBean requestBean, boolean isPublicResource) {
            if(this.getDepartmentCode() != null) {
                requestBean.setDepartmentCode(this.getDepartmentCode());
            }
            if(this.getUserName()!= null) {
                requestBean.setUserName(this.getUserName());
            }
            if(this.getHttpRequest().getParameter("expand") != null) {
                requestBean.setExpand(this.getHttpRequest().getParameter("expand"));
            }
            if(this.getHttpRequest().getParameter("q") != null) {
                requestBean.setQ(this.getHttpRequest().getParameter("q"));
            }
            if(this.getHttpRequest().getParameter("order") != null) {
                requestBean.setOrder(this.getHttpRequest().getParameter("order"));
            }
            if(this.getHttpRequest().getParameter("donExcludedResourceFields") != null) {
                requestBean.setDonExcludedResourceFields(this.getHttpRequest().getParameter("donExcludedResourceFields"));
            }
            
            if(this.getHttpRequest().getParameter("mandatoryExpand") != null) {
                requestBean.setMandatoryExpand(this.getHttpRequest().getParameter("mandatoryExpand"));
            }
            if(this.getHttpRequest().getParameter("page") != null) {
                requestBean.setPage(Integer.parseInt(this.getHttpRequest().getParameter("page")));
            }
            if(this.getHttpRequest().getParameter("limit") != null) {
                requestBean.setLimit(Integer.parseInt(this.getHttpRequest().getParameter("limit")));
            }
            if(this.getHttpRequest().getParameter("cacheDuration") != null) {
                requestBean.setCacheDuration(Integer.parseInt(this.getHttpRequest().getParameter("cacheDuration")));
            }
            if(this.getHttpRequest().getParameter("status") != null) {
                requestBean.setStatus(Integer.parseInt(this.getHttpRequest().getParameter("status")));
            }
            if(this.getHttpRequest().isUserInRole(BaseRequestWrapper.ROLE_SYSTEM_ADMIN)) {
                requestBean.setSystemAdmin(true);
            }
            if(this.getHttpRequest().getUserPrincipal() instanceof StarUser) {
                StarUser starUser = (StarUser)this.getHttpRequest().getUserPrincipal();
                requestBean.setUserId(starUser.getUserId());
                requestBean.setDisplayName(starUser.getDisplayName());
            }
            if(this.getHttpRequest() instanceof BaseRequestWrapper) {
                String channel = ((BaseRequestWrapper)this.getHttpRequest()).getChannel();
                requestBean.setChannel(channel);
            }
            requestBean.setIpAddress(HttpUtils.getOriginatingIpAddress(httpRequest));
        }

		public HttpServletRequest getHttpRequest() {
			return httpRequest;
		}
		public void setHttpRequest(HttpServletRequest httpRequest) {
			this.httpRequest = httpRequest;
		}
		public HttpServletResponse getHttpResponse() {
			return httpResponse;
		}
		public void setHttpResponse(HttpServletResponse httpResponse) {
			this.httpResponse = httpResponse;
		}
        public String getUserName() {
            return userName;
        }
        public void setUserName(String userName) {
            this.userName = userName;
        }
        public String getDepartmentCode() {
            return departmentCode;
        }
        public void setDepartmentCode(String departmentCode) {
            this.departmentCode = departmentCode;
        }
		public RootResourceBean getBean() {
			return bean;
		}
		public void setBean(RootResourceBean bean) {
			this.bean = bean;
		}
    }
}