package au.com.suttons.notification.jobs;

import au.com.suttons.notification.config.AppConfig;
import au.com.suttons.notification.jobs.util.CronUtil;
import au.com.suttons.notification.service.TerminationNotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.ScheduleExpression;
import javax.ejb.Singleton;
import javax.ejb.Startup;

@Singleton
@Startup
public class TerminationNotificationDailyJob extends BaseJob
{
    private static final Logger logger = LoggerFactory.getLogger(TerminationNotificationDailyJob.class);

    @EJB
    private TerminationNotificationService terminationNotificationService;

    @Override
	protected void setupScheduler() {
        if (AppConfig.terminationNotificationDailyJob != null)
        {
            ScheduleExpression jobExpr = CronUtil.createScheduleExpressionFromString(AppConfig.terminationNotificationDailyJob);
            timerService.createCalendarTimer(jobExpr);
        }
    }

    @Override
    protected void run() {
        try
        {
            logger.info(String.format("%s job STARTED", JobConstants.KEY_TERMINATION_NOTIFICATION_DAILY_JOB));

            this.terminationNotificationService.sendTerminationNotifications(JobConstants.NOTIFICATION_TYPE_DAILY);

            logger.info(String.format("%s job ENDED", JobConstants.KEY_TERMINATION_NOTIFICATION_DAILY_JOB));

        }
        catch(Exception e)
        {
            e.printStackTrace();
            logger.error("Error sending daily employee termination notifications : "+ e.getMessage());
        }
        
    }

}
