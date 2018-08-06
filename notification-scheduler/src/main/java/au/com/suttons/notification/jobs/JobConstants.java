package au.com.suttons.notification.jobs;

public class JobConstants 
{

    public static final String KEY_EMPLOYEE_FILE_READ_JOB = "EMPLOYEE_FILE_READ_JOB";
    public static final String KEY_TERMINATION_NOTIFICATION_DAILY_JOB = "TERMINATION_NOTIFICATION_DAILY_JOB";
    public static final String KEY_TERMINATION_NOTIFICATION_WEEKLY_JOB = "TERMINATION_NOTIFICATION_WEEKLY_JOB";

    public static final String STATUS_PROCESSED = "PROCESSED";
    public static final String STATUS_ACTIVE = "ACTIVE";
    public static final String STATUS_SENT = "SENT";
    public static final String STATUS_ERROR = "ERROR";

    public static final String MAIL_RECIPIENT_TYPE_GLOBAL = "GLOBAL";
    public static final String MAIL_RECIPIENT_TYPE_COMPANY = "COMPANY";

    public static final String NOTIFICATION_TYPE_DAILY = "DAILY";
    public static final String NOTIFICATION_TYPE_WEEKLY = "WEEKLY";

    public static final Long USER_SYSTEM = new Long(1);

}
