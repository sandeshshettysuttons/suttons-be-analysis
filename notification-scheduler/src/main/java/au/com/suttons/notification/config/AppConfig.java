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

    private static final String MAIL_TRANSPORT_PROTOCAL="NOTIFICATION_MAIL_TRANSPORT_PROTOCAL";
    private static final String MAIL_HOST_ADDRESS="NOTIFICATION_MAIL_HOST_ADDRESS";
    private static final String MAIL_SYSTEM_TO_ADDRESS_ERROR_REPORT="NOTIFICATION_MAIL_TO_ADDRESS_ERROR_REPORT";
    private static final String MAIL_SYSTEM_FROM_ADDRESS="NOTIFICATION_MAIL_SYSTEM_FROM_ADDRESS";
    private static final String MAIL_SMTP_PORT="NOTIFICATION_MAIL_SMTP_PORT";
    private static final String MAIL_SMTP_STARTTLS_ENABLE="NOTIFICATION_MAIL_SMTP_STARTTLS_ENABLE";
    private static final String NETWORK_FILE_STORAGE_LOCATION = "NOTIFICATION_NETWORK_FILE_STORAGE_LOCATION";
    private static final String EMAIL_TEMPLATE_LOCATION = "NOTIFICATION_EMAIL_TEMPLATE_LOCATION";

    private static final String EMPLOYEE_FILE_READ_CRON_EXPRESSION_KEY = "EMPLOYEE_FILE_READ_CRON_EXPRESSION_KEY";
    private static final String TERMINATION_NOTIFICATION_DAILY_CRON_EXPRESSION_KEY = "TERMINATION_NOTIFICATION_DAILY_CRON_EXPRESSION_KEY";
    private static final String TERMINATION_NOTIFICATION_WEEKLY_CRON_EXPRESSION_KEY = "TERMINATION_NOTIFICATION_WEEKLY_CRON_EXPRESSION_KEY";

    public static String mailTransportProtocal;
    public static String mailHostAddress;
    public static String mailSystemFromAddress;
    public static String mailSmtpPort;
    public static String mailSmtpStarttlsEnable;

    public static String networkFileStorageLocation;
    public static String emailTemplateLocation;

    public static String employeeFileReadJob;
    public static String terminationNotificationDailyJob;
    public static String terminationNotificationWeeklyJob;

    /*
     NOTE: if this value changes, reflect the change in these files:
     - pom.xml
    */
    public static String applicationContext = "notification-scheduler";

    private static final Logger logger = LoggerFactory.getLogger(AppConfig.class);

    /**
     * Load application properties
     */
    static
    {
        try {
            /* Load properties object from container */
            Context ctx = new InitialContext();
            Properties properties = (Properties) ctx.lookup(CONTAINER_PROPERTIES_JNDI);

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
            if (properties.get(NETWORK_FILE_STORAGE_LOCATION) != null) {
                networkFileStorageLocation = properties.get(NETWORK_FILE_STORAGE_LOCATION).toString();
            }
            else {
                logger.error("Could not retrieve '" + NETWORK_FILE_STORAGE_LOCATION + "' property from server's custom properties.");
            }
            if (properties.get(EMAIL_TEMPLATE_LOCATION) != null) {
                emailTemplateLocation = properties.get(EMAIL_TEMPLATE_LOCATION).toString();
            }
            else {
                logger.error("Could not retrieve '" + EMAIL_TEMPLATE_LOCATION + "' property from server's custom properties.");
            }
            if (properties.get(EMPLOYEE_FILE_READ_CRON_EXPRESSION_KEY) != null) {
                employeeFileReadJob = properties.get(EMPLOYEE_FILE_READ_CRON_EXPRESSION_KEY).toString();
            }
            else {
                logger.error("Could not retrieve '" + EMPLOYEE_FILE_READ_CRON_EXPRESSION_KEY + "' property from server's custom properties.");
            }

            if (properties.get(TERMINATION_NOTIFICATION_DAILY_CRON_EXPRESSION_KEY) != null) {
                terminationNotificationDailyJob = properties.get(TERMINATION_NOTIFICATION_DAILY_CRON_EXPRESSION_KEY).toString();
            }
            else {
                logger.error("Could not retrieve '" + TERMINATION_NOTIFICATION_DAILY_CRON_EXPRESSION_KEY + "' property from server's custom properties.");
            }

            if (properties.get(TERMINATION_NOTIFICATION_WEEKLY_CRON_EXPRESSION_KEY) != null) {
                terminationNotificationWeeklyJob = properties.get(TERMINATION_NOTIFICATION_WEEKLY_CRON_EXPRESSION_KEY).toString();
            }
            else {
                logger.error("Could not retrieve '" + TERMINATION_NOTIFICATION_WEEKLY_CRON_EXPRESSION_KEY + "' property from server's custom properties.");
            }

        }
        catch (Exception ex) {
            logger.error("Exception while attempting to load properties file.", ex);
        }
    }
}
