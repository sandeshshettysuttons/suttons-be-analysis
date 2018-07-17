package au.com.suttons.notification.util;

import au.com.suttons.notification.config.AppConfig;
import au.com.suttons.notification.exception.RestApiException;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SecurityUtils {

	public static void starApiADLogin(WebClient webClient) {

		DefaultHttpClient httpclient = new DefaultHttpClient();
		boolean isLogin = false;

		try {
			// post to AD login to start session and get cookie
			HttpPost httpPost = new HttpPost(AppConfig.starApiBaseUrl + "/j_security_check");

			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("j_username", AppConfig.starAPIUserId));
			nvps.add(new BasicNameValuePair("j_password", AppConfig.starAPIUserPassword));
			httpPost.setEntity(new UrlEncodedFormEntity(nvps));

			//abort this request to consume the contents
			HttpResponse httpResponse = httpclient.execute(httpPost);
			httpResponse.getStatusLine().getStatusCode();
			if (httpResponse.getEntity() != null) {
				httpPost.abort();
			}

			CookieStore cookieStore = httpclient.getCookieStore();
			for (Cookie cookie : cookieStore.getCookies()) {
				String cookieName = cookie.getName();
				webClient.cookie(new javax.ws.rs.core.Cookie(cookieName, cookie.getValue()));
				if ("JSESSIONID".equals(cookieName)) {
					isLogin = true;
				}
			}
		}catch (IOException e){
			throw new RestApiException(e);
		}

		if(!isLogin) {
			throw new RestApiException("Invalid star api login id and/or password");
		}
	}
}
