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
public class TerminationNotificationWeeklyJob extends BaseJob
{
    private static final Logger logger = LoggerFactory.getLogger(TerminationNotificationWeeklyJob.class);

    @EJB
    private TerminationNotificationService terminationNotificationService;

    private static final String NOTIFICATION_CONFIG_TYPE_WEEKLY = "WEEKLY";

    @Override
	protected void setupScheduler() {
        if (AppConfig.terminationNotificationWeeklyJob != null)
        {
            ScheduleExpression jobExpr = CronUtil.createScheduleExpressionFromString(AppConfig.terminationNotificationWeeklyJob);
            timerService.createCalendarTimer(jobExpr);
        }
    }

    @Override
    protected void run() {
        try
        {
            logger.info(String.format("%s job STARTED", JobConstants.KEY_TERMINATION_NOTIFICATION_WEEKLY_JOB));

            this.terminationNotificationService.sendTerminationNotifications(NOTIFICATION_CONFIG_TYPE_WEEKLY);

            logger.info(String.format("%s job ENDED", JobConstants.KEY_TERMINATION_NOTIFICATION_WEEKLY_JOB));

        }
        catch(Exception e)
        {
            e.printStackTrace();
            logger.error("Error sending weekly employee termination notifications : "+ e.getMessage());
        }

    }

}
