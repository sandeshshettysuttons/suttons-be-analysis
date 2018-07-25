package au.com.suttons.notification.jobs;

import au.com.suttons.notification.config.AppConfig;
import au.com.suttons.notification.jobs.util.CronUtil;
import au.com.suttons.notification.service.EmployeeFileService;
import au.com.suttons.notification.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.ScheduleExpression;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.nio.file.Path;
import java.util.List;

@Singleton
@Startup
public class EmployeeFileReadJob extends BaseJob
{
    private static final Logger logger = LoggerFactory.getLogger(EmployeeFileReadJob.class);

    @EJB
    private EmployeeFileService employeeFileService;

    @Override
	protected void setupScheduler() {
        if (AppConfig.employeeFileReadJob != null)
        {
            ScheduleExpression jobExpr = CronUtil.createScheduleExpressionFromString(AppConfig.employeeFileReadJob);
            timerService.createCalendarTimer(jobExpr);
        }
    }

    @Override
    protected void run() {
        try
        {
            logger.info(String.format("%s job STARTED", JobConstants.KEY_EMPLOYEE_FILE_READ_JOB));

            // Read all CSV files from documents folder
            List<Path> files = FileUtil.getCSVFilesInDirectory(
                    FileUtil.getEmployeeFileLocation());

            for (Path file : files) {
                // Process each file
                this.employeeFileService.readEmployeeFiles(file);
            }

            logger.info(String.format("%s job ENDED", JobConstants.KEY_EMPLOYEE_FILE_READ_JOB));

        }
        catch(Exception e)
        {
            e.printStackTrace();
            logger.error("Error reading employee files : "+ e.getMessage());
        }
        
    }

}
