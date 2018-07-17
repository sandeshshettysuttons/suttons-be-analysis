package au.com.suttons.notification;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.com.suttons.model.RequestMethod;
import au.com.suttons.notification.config.AppConfig;
import au.com.suttons.notification.security.ActiveDirectoryRequestWrapper;
import au.com.suttons.notification.util.HttpUtils;

public class ProxyServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(ProxyServlet.class);

    private static final String AD_COOKIE_DOMAIN = "adCookieDomain";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doProxy(request, response, RequestMethod.GET);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doProxy(request, response, RequestMethod.POST);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doProxy(request, response, RequestMethod.PUT);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doProxy(request, response, RequestMethod.DELETE);
    }

    private void doProxy(HttpServletRequest request, HttpServletResponse response, RequestMethod method) throws IOException {
        try{
        	
            String pathInfo = request.getPathInfo();
            String query = request.getQueryString();

            String url = AppConfig.baseAPIURL + pathInfo;
            HttpResponse httpResponse = sendHttpRequest(request, method, query, url);

            /**
             * This is used to set the content type to text/plain for file uploads.
             * IE9 doesn't know how to handle application/json and tries to download the response.
             */
            if (ServletFileUpload.isMultipartContent(request)) {
                httpResponse.setHeader("content-type", "text/plain");
            }

            /**
             * Add all the headers from the second response.
             */
            for (Header header : httpResponse.getAllHeaders()) {
                response.setHeader(header.getName(), header.getValue());
            }

            response.setStatus(httpResponse.getStatusLine().getStatusCode());

            HttpEntity entity = httpResponse.getEntity();

            if (entity != null) {

                InputStream in = httpResponse.getEntity().getContent();
                ServletOutputStream outputStream = response.getOutputStream();
                outputStream.write(IOUtils.toByteArray(in));
                outputStream.flush();
            }
        } catch (Exception e){
            logger.error("Error happened in service web proxy.", e);
        }
    }

    public static HttpResponse sendHttpRequest(HttpServletRequest request, RequestMethod method, String query, String url) throws IOException {
        HttpRequestBase httpRequest;
        if (query != null) {
            url = url + "?" + query;
        }

        logger.info(" *** #### ==== Send request to " + url);

        switch (method) {
            case GET:
                httpRequest = new HttpGet(url);

                String eTagRequestHeaderName = "If-None-Match";
                String eTag = request.getHeader(eTagRequestHeaderName);

                if(eTag != null && eTag.length() > 0) {
                    httpRequest.setHeader(eTagRequestHeaderName, eTag);
                }
                break;
            case POST:
                HttpPost httpPost = new HttpPost(url);
                setRequestBody(request, httpPost);
                httpRequest = httpPost;
                break;
            case PUT:
                HttpPut httpPut = new HttpPut(url);
                setRequestBody(request, httpPut);
                httpRequest = httpPut;
                break;
            case DELETE:
                httpRequest = new HttpDelete(url);
                break;
            default:
                throw new RuntimeException("Unsupported Request method");
        }

        httpRequest.setHeader("x-client-ipaddress", HttpUtils.getOriginatingIpAddress(request));

        DefaultHttpClient httpClientDefault = new DefaultHttpClient();

        CookieStore cookieStore = new BasicCookieStore();

        Cookie[] cookies = request.getCookies();

        if (cookies != null) {

            for (Cookie cookie : cookies) {

                boolean isRequired = false;
                String cookieName = cookie.getName();
                if ("JSESSIONID".equals(cookieName) || "JSESSIONIDSSO".equals(cookieName) || "BALANCEID".equals(cookieName)) {
                    isRequired = true;
                }

                if (isRequired) {
                    String adCookieDomainName = request.getServletContext().getInitParameter(AD_COOKIE_DOMAIN);
                    BasicClientCookie basicClientCookie = new BasicClientCookie(cookie.getName(), cookie.getValue());
                    basicClientCookie.setDomain(adCookieDomainName);

                    cookieStore.addCookie(basicClientCookie);
                }
            }

        }

        httpClientDefault.setCookieStore(cookieStore);

        return httpClientDefault.execute(httpRequest);
    }

    private static void setRequestBody(HttpServletRequest request, HttpEntityEnclosingRequestBase requestBase) throws IOException {
        //Multipart request
        if(ServletFileUpload.isMultipartContent(request)){
            FileItem fileItem = ActiveDirectoryRequestWrapper.getFileItem(request);
            String requestMethod = ActiveDirectoryRequestWrapper.getRequestMethod(request);

            if(fileItem != null){
                MultipartEntity multiPartEntity = new MultipartEntity () ;
                multiPartEntity.addPart("fileBody",  new ByteArrayBody(fileItem.get(),fileItem.getName()));
                multiPartEntity.addPart("_method", new StringBody(requestMethod != null ? requestMethod : "")) ;

                requestBase.setEntity(multiPartEntity) ;
            }
        }else {
            HttpEntity httpEntity = new InputStreamEntity(request.getInputStream(), request.getContentLength());
            requestBase.setHeader("Content-Type", request.getContentType());
            requestBase.setEntity(httpEntity);
        }
    }
}
