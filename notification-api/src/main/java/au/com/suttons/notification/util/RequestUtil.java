package au.com.suttons.notification.util;

import au.com.suttons.notification.resource.error.RestApiException;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RequestUtil
{
    private static final Logger logger = LoggerFactory.getLogger(RequestUtil.class);

    public static String getFullRequestURL(HttpServletRequest request)
    {
        StringBuilder href = new StringBuilder();
        String url = request.getRequestURL().toString();
        String queryStr = request.getQueryString();

        //Append the URI
        href.append(url);

        if (queryStr != null) {
            //Attempt to decode and append the query string
            try {
                href.append("?");

                href.append(URLDecoder.decode(queryStr, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                logger.debug(String.format("An exception occurred when attempting to decode the query string of a request URL. URL: '%s', Query String (Raw): '%s'", url, queryStr), e);
            }
        }

        return href.toString();
    }

        /**
     * Gets the depot code from request URL
     * @param request
     * @return
     * @throws RestApiException if method fails to find the department code
     */
    public static String getDepartmentCode(HttpServletRequest request) throws RestApiException {
        // Match the department code part of the request URI
        Pattern deptRegex = Pattern.compile(request.getContextPath() + "/api/(\\w+)/.+");
        Matcher matcher = deptRegex.matcher(request.getRequestURI());

        if (matcher.matches()) {
            String result = matcher.group(1);

            return result;
        }
        //TODO: This is for testing only - Delete this else condition later
        else{
            deptRegex = Pattern.compile(request.getContextPath() + "/admin-api/(\\w+)/.+");
            matcher = deptRegex.matcher(request.getRequestURI());
            if (matcher.matches()) {
                String result = matcher.group(1);

                return result;
            }
        }
		return null;
    }
}
