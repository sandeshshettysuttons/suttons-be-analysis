package au.com.suttons.notification.util;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;


public class HttpUtils {

	public static String getOriginatingIpAddress(ServletRequest request) {
		HttpServletRequest httpRequest = (HttpServletRequest)request;

        if(httpRequest.getHeader("x-client-ipaddress") != null) {
            return httpRequest.getHeader("x-client-ipaddress");
        }

        if(httpRequest.getHeader("x-forwarded-for") != null) {
            return httpRequest.getHeader("x-forwarded-for");
        }

        return httpRequest.getRemoteAddr();
	}
}
