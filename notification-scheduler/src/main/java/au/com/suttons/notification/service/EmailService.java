package au.com.suttons.notification.service;

import au.com.suttons.notification.bean.EmployeeBean;
import au.com.suttons.notification.bean.EmployeeGroupBean;
import au.com.suttons.notification.bean.TerminationNotificationBean;
import au.com.suttons.notification.config.AppConfig;
import au.com.suttons.notification.data.dao.EmailDao;
import au.com.suttons.notification.data.dao.EmployeeDao;
import au.com.suttons.notification.data.dao.MailRecipientDao;
import au.com.suttons.notification.data.entity.EmailLogEmployeeEntity;
import au.com.suttons.notification.data.entity.EmailLogEntity;
import au.com.suttons.notification.data.entity.MailRecipientEntity;
import au.com.suttons.notification.jobs.JobConstants;
import au.com.suttons.notification.util.DateUtil;
import au.com.suttons.notification.util.MailerUtil;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.io.StringWriter;
import java.util.ArrayList;

@Stateless
public class EmailService
{
	protected VelocityEngine ve;
    public static final String MAIL_TEMPLATES_FOLDER = "MailTemplates";
	private static final String TERMINATION_NOTIFN_TEMPLATE = "terminationNotification.vm";

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @EJB
    private MailRecipientDao mailRecipientDao;

    @EJB
    private EmployeeDao employeeDao;

    @EJB
    private EmailDao emailDao;

    public EmailService() {
    	
        ve = new VelocityEngine();
        ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        ve.init();
    }

    public void sendTerminationNotification(TerminationNotificationBean terminationNotification) throws Exception {

        VelocityContext context = new VelocityContext();
        String emailFrom = AppConfig.mailSystemFromAddress;

        if (terminationNotification != null) {

            String subject = "Employees Termination / Transfer Notification";
            String folder = getMailTemplatesFolder();
            Reader templateReader = new BufferedReader(new FileReader(folder + TERMINATION_NOTIFN_TEMPLATE));

            if ( templateReader != null) {

                context.put("terminationNotification", terminationNotification);

                StringWriter writer = new StringWriter();
                Velocity.evaluate(context, writer, "Termination notification", templateReader);

                String emailTo = terminationNotification.getMailRecipientEmail();
                String[] emails = emailTo.split(",");

                MailerUtil.sendMail(emailFrom, emails, new String[]{}, new String[]{}, subject, writer.toString(), null, null, false);

            } else {
                logger.debug("Can't send termination notification as email template not generated");
            }

        }
    }

    public EmailLogEntity saveEmailLogs(MailRecipientEntity mailRecipient, TerminationNotificationBean terminationNotification, String status) {

        EmailLogEntity emailEntity = new EmailLogEntity();
        emailEntity.setLastUpdatedBy(JobConstants.USER_SYSTEM);

        emailEntity.setMailRecipient(
                this.mailRecipientDao.getReference(mailRecipient.getId()));
        emailEntity.setStatus(status);
        emailEntity.setEmailLogEmployees(new ArrayList<>());

        if (terminationNotification.getEmployeeGroups() != null) {

            for (EmployeeGroupBean employeeGroup : terminationNotification.getEmployeeGroups()) {

                if (employeeGroup.getEmployees() != null) {

                    for (EmployeeBean employee : employeeGroup.getEmployees()) {

                        EmailLogEmployeeEntity employeeEntity = mapEmployeeBeanToLogEntity(emailEntity, employee);
                        emailEntity.getEmailLogEmployees().add(employeeEntity);
                    }

                }

            }

        }

        return this.emailDao.saveAndFlush(emailEntity);
    }

    public static String getMailTemplatesFolder() {
        return AppConfig.emailTemplateLocation + "/";
    }

    private EmailLogEmployeeEntity mapEmployeeBeanToLogEntity(EmailLogEntity emailLogEntity, EmployeeBean bean) {

        EmailLogEmployeeEntity entity = new EmailLogEmployeeEntity();
        entity.setLastUpdatedBy(emailLogEntity.getLastUpdatedBy());

        entity.setEmailLog(emailLogEntity);
        entity.setEmployee(
                this.employeeDao.getReference(bean.getId()));
        entity.setTerminationDate(
                DateUtil.parseStringToDate(bean.getTerminationDate()));

        return entity;
    }

}
