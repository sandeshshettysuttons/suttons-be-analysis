package au.com.suttons.notification.util;

import au.com.suttons.notification.config.AppConfig;
import au.com.suttons.notification.exception.NotFoundException;
import au.com.suttons.notification.exception.RestApiException;
import au.com.suttons.notification.model.Constants;
import au.com.suttons.notification.resource.bean.ResponseErrorBean;
import org.apache.commons.lang.StringUtils;
import org.apache.cxf.jaxrs.client.ServerWebApplicationException;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.jaxrs.impl.UriBuilderImpl;
import org.codehaus.jackson.jaxrs.JacksonJaxbJsonProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/***
 * Utility class that provides a single point for REST operations. This util class makes use of the <code>org.apache.cxf.jaxrs.client.WebClient</code> to perform requested REST actions
 */
public class RestUtil
{
    private static final Logger logger = LoggerFactory.getLogger(RestUtil.class);

    public static <T> T post(String baseAPI, String serviceURL, T requestObject, Class<T> requestObjectClass)
    {
        return RestUtil.request(baseAPI, null, HttpMethod.POST, serviceURL, requestObjectClass, requestObject);
    }

    public static <T> T put(String baseAPI, String serviceURL, T requestObject, Class<T> requestObjectClass)
    {
        return RestUtil.request(baseAPI, null, HttpMethod.PUT, serviceURL, requestObjectClass, requestObject);
    }

    public static <T> T get(String baseAPI, String serviceURL, Class<T> responseClass)
    {
        return RestUtil.request(baseAPI, null, HttpMethod.GET, serviceURL, responseClass, null);
    }

    private static <T> T request(String baseAPI, String originatingIpAddress,
            String httpMethod, String serviceURL, Class<T> responseClass, T requestBody)
    {
        T responseObject = null;

        List<Object> providers = new ArrayList<Object>();
        providers.add( new JacksonJaxbJsonProvider() );

        logger.info("Sending Request to "+serviceURL.trim());

        WebClient webClient = WebClient
                .create(serviceURL.trim(), providers)
                .type(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN);

        if(Constants.BASEAPI_STAR.equals(baseAPI)) {
            //set JSESSIONIDSSO
            SecurityUtils.starApiADLogin(webClient);
        }

        try {
            if (responseClass == null)
            {
                Response response = webClient.invoke(httpMethod, requestBody);
                if(response.getStatus() >= 400) {
                    throw new ServerWebApplicationException(response);
                }
            }
            else
            {
                responseObject = webClient.invoke(httpMethod, requestBody, responseClass);
            }
        } catch(ServerWebApplicationException e) {
            try {
                if(e.getStatus() == 404) {
                    throw new NotFoundException(e.getMessage());
                } else {
                    ResponseErrorBean responseErrorBean = e.toErrorObject(webClient, ResponseErrorBean.class);
                    throw new RestApiException(responseErrorBean);
                }
            } catch(Throwable t) {
                if(t instanceof NotFoundException) {
                    throw (NotFoundException)t;
                }
                else if(t instanceof RestApiException) {
                    throw (RestApiException)t;
                }

                t.printStackTrace();
                throw new RestApiException(t.getMessage());
            }
        }

        return responseObject;
    }

    public static String getApiURL(String url, String query) throws RuntimeException {
        return getApiURL(url, query, null, null);
    }

    public static String getApiURL(String url, String query, Integer page, Integer limit) throws RuntimeException {
        URI uri;
        try {
            uri = new URI(url);
        } catch (URISyntaxException ex) {
            logger.error("Invalid URL", ex);
            throw new RuntimeException("Invalid URL", ex);
        }

        UriBuilder uriBuilder = new UriBuilderImpl(uri);
        if(StringUtils.isNotEmpty(query)) {
            uriBuilder = uriBuilder.queryParam("query", query);
        }
        if(page != null) {
            uriBuilder = uriBuilder.queryParam("page", page + 1);
        }
        if(limit != null) {
            uriBuilder = uriBuilder.queryParam("limit", limit);
        }

        return uriBuilder.build().toString();
    }

    public static String getStarApiURL(String baseAPI, String path) {
        return (Constants.BASEAPI_STAR.equals(baseAPI) ? AppConfig.starApiBaseUrl : AppConfig.baseAPIURL)  + path;
    }
}
