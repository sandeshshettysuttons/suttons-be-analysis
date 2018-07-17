package au.com.suttons.notification.config;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class AppConfig
{
    public static final String CONTAINER_PROPERTIES_JNDI = "java:global/notification/properties";

    private static final String BASE_API_URL = "NOTIFICATION_WEB_BASE_API_URL";
    private static final String MAIL_TRANSPORT_PROTOCAL="NOTIFICATION_MAIL_TRANSPORT_PROTOCAL";
    private static final String MAIL_HOST_ADDRESS="NOTIFICATION_MAIL_HOST_ADDRESS";
    private static final String MAIL_SYSTEM_TO_ADDRESS="NOTIFICATION_MAIL_SYSTEM_TO_ADDRESS";
    private static final String MAIL_SYSTEM_FROM_ADDRESS="NOTIFICATION_MAIL_SYSTEM_FROM_ADDRESS";
    private static final String MAIL_SMTP_PORT="NOTIFICATION_MAIL_SMTP_PORT";
    private static final String MAIL_SMTP_STARTTLS_ENABLE="NOTIFICATION_MAIL_SMTP_STARTTLS_ENABLE";

    public static String baseAPIURL;
    public static String mailTransportProtocal;
    public static String mailHostAddress;
    public static String mailSystmeToAddress;
    public static String mailSystemFromAddress;
    public static String mailSmtpPort;
    public static String mailSmtpStarttlsEnable;

    public static final int ATTENDANCE_THRESHOLD_MINUTES = 180;
    /*
     NOTE: if this value changes, reflect the change in these files:
     - pom.xml
    */
    public final static String applicationContext = "notification";

    private static final Logger logger = LoggerFactory.getLogger(AppConfig.class);

    /**
     * Load application properties
     */
    static
    {
        try {
            /* Load properties object from container */
            Context ctx = new InitialContext();
			Properties properties= (Properties) ctx.lookup(CONTAINER_PROPERTIES_JNDI);

            if (properties.get(BASE_API_URL) != null) {
                baseAPIURL = properties.get(BASE_API_URL).toString();
            }
            else {
                logger.error("Could not retrieve '" + BASE_API_URL + "' property from properties file! All resources will contain an incorrect HREF.");
            }

            if (properties.get(MAIL_TRANSPORT_PROTOCAL) != null) {
                mailTransportProtocal = properties.get(MAIL_TRANSPORT_PROTOCAL).toString();
            }
            else {
                logger.error("Could not retrieve '" + MAIL_TRANSPORT_PROTOCAL + "' property from server's custom properties.");
            }
            
            if (properties.get(MAIL_HOST_ADDRESS) != null) {
                mailHostAddress = properties.get(MAIL_HOST_ADDRESS).toString();
            }
            else {
                logger.error("Could not retrieve '" + MAIL_HOST_ADDRESS + "' property from server's custom properties.");
            }
            
            if (properties.get(MAIL_SYSTEM_TO_ADDRESS) != null) {
                mailSystmeToAddress = properties.get(MAIL_SYSTEM_TO_ADDRESS).toString();
            }
            else {
                logger.error("Could not retrieve '" + MAIL_SYSTEM_TO_ADDRESS + "' property from server's custom properties.");
            }
 
            if (properties.get(MAIL_SYSTEM_FROM_ADDRESS) != null) {
                mailSystemFromAddress = properties.get(MAIL_SYSTEM_FROM_ADDRESS).toString();
            }
            else {
                logger.error("Could not retrieve '" + MAIL_SYSTEM_FROM_ADDRESS + "' property from server's custom properties.");
            }

            if (properties.get(MAIL_SMTP_PORT) != null) {
                mailSmtpPort = properties.get(MAIL_SMTP_PORT).toString();
            }
            else {
                logger.error("Could not retrieve '" + MAIL_SMTP_PORT + "' property from server's custom properties.");
            }

            if (properties.get(MAIL_SMTP_STARTTLS_ENABLE) != null) {
                mailSmtpStarttlsEnable = properties.get(MAIL_SMTP_STARTTLS_ENABLE).toString();
            }
            else {
                logger.error("Could not retrieve '" + MAIL_SMTP_STARTTLS_ENABLE + "' property from server's custom properties.");
            }

        }
        catch (Exception ex) {
            logger.error("Exception while attempting to load properties file.", ex);
        }
    }
}
