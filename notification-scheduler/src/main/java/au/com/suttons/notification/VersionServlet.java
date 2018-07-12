package au.com.suttons.notification;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class VersionServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	final Properties properties = new Properties();
    	properties.load(this.getClass().getResourceAsStream("/version.properties"));
    	String version = properties.getProperty("version");

        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(version.getBytes());
        outputStream.flush();
    }
}
