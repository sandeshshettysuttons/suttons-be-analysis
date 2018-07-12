package au.com.suttons.notification.service;

import au.com.suttons.notification.bean.EmployeeBean;
import au.com.suttons.notification.bean.EmployeeGroupBean;
import au.com.suttons.notification.bean.TerminationNotificationBean;
import au.com.suttons.notification.data.dao.EmployeeDao;
import au.com.suttons.notification.data.dao.MailRecipientDao;
import au.com.suttons.notification.data.dao.TerminationNotificationConfigDao;
import au.com.suttons.notification.data.entity.EmployeeEntity;
import au.com.suttons.notification.data.entity.MailRecipientEntity;
import au.com.suttons.notification.data.entity.TerminationNotificationConfigEntity;
import au.com.suttons.notification.jobs.JobConstants;
import au.com.suttons.notification.util.DateUtil;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Stateless
public class TerminationNotificationService
{

    private static final Logger logger = LoggerFactory.getLogger(TerminationNotificationService.class);

    @EJB
    private EmployeeDao employeeDao;

    @EJB
    private TerminationNotificationConfigDao terminationNotificationConfigDao;

    @EJB
    private MailRecipientDao mailRecipientDao;

    @EJB
    private EmailService emailService;

    public void sendTerminationNotifications(String notificationType) {

        // Step 1 : Find mail recipients to whom notifications need to be sent
        List<MailRecipientEntity> mailRecipients = this.mailRecipientDao.findAllActive();

        // Step 2 : Find employees who are being terminated
        List<EmployeeEntity> employees = this.employeeDao.findAllActive();

        if (CollectionUtils.isNotEmpty(mailRecipients) && CollectionUtils.isNotEmpty(employees)) {

            TerminationNotificationBean terminationNotification = new TerminationNotificationBean();

            // Step 3 : Group employees by time to termination date
            populateTerminationEmployeeDetails(notificationType, terminationNotification, employees);

            // Step 4 : Send email
            if (CollectionUtils.isNotEmpty(terminationNotification.getEmployeeGroups())) {

                for (MailRecipientEntity mailRecipient : mailRecipients) {

                    terminationNotification.setMailRecipientName(mailRecipient.getName());
                    terminationNotification.setMailRecipientEmail(mailRecipient.getEmail());

                    try {
                        this.emailService.sendTerminationNotification(terminationNotification);
                        this.emailService.saveEmailLogs(mailRecipient, terminationNotification, JobConstants.STATUS_SENT);

                    } catch (Exception ex) {
                        logger.error("Exception while attempting to send termination notification.", ex);
                        this.emailService.saveEmailLogs(mailRecipient, terminationNotification, JobConstants.STATUS_ERROR);
                    }

                }
            }
        }
    }

    private TerminationNotificationBean populateTerminationEmployeeDetails(
            String notificationType,
            TerminationNotificationBean terminationNotification,
            List<EmployeeEntity> employees) {

        List<TerminationNotificationConfigEntity> configs = this.terminationNotificationConfigDao.findByType(notificationType);
        List<EmployeeGroupBean> employeeGroups = new ArrayList<>();

        for (TerminationNotificationConfigEntity config : configs) {

            EmployeeGroupBean employeeGroup = new EmployeeGroupBean();
            employeeGroup.setDaysToTerminationRangeFrom(config.getDaysToTerminationFrom().intValue());
            employeeGroup.setDaysToTerminationRangeTo(config.getDaysToTerminationTo().intValue());
            employeeGroup.setDescription(config.getDescription());
            employeeGroup.setSequence(config.getSequence().intValue());

            List<EmployeeBean> groupEmployees = new ArrayList<>();

            for (EmployeeEntity employee : employees) {

                long daysToTermination = DateUtil.getDateDiff(
                        DateUtil.getCurrentDateWithoutTime(),
                        employee.getTerminationDate(),
                        TimeUnit.DAYS
                );

                if (Math.min(employeeGroup.getDaysToTerminationRangeFrom(), employeeGroup.getDaysToTerminationRangeTo()) <= daysToTermination
                        && daysToTermination <= Math.max(employeeGroup.getDaysToTerminationRangeFrom(), employeeGroup.getDaysToTerminationRangeTo())) {

                    groupEmployees.add(
                            mapEmployeeEntityToBean(employee));
                }

            }

            if (groupEmployees.size() > 0) {
                employeeGroup.setEmployees(groupEmployees);
                employeeGroups.add(employeeGroup);
            }
        }

        terminationNotification.setEmployeeGroups(employeeGroups);

        return terminationNotification;
    }

    private EmployeeBean mapEmployeeEntityToBean(EmployeeEntity entity) {

        EmployeeBean bean = new EmployeeBean();

        bean.setId(entity.getId());
        bean.setEmployeeNumber(entity.getEmployeeNumber());
        bean.setFirstName(entity.getFirstName());
        bean.setLastName(entity.getLastName());
        bean.setDescription(entity.getDescription());
        bean.setPosition(entity.getPosition());
        bean.setTerminationDate(
                DateUtil.formatDateToString(entity.getTerminationDate()));
        bean.setStatus(entity.getStatus());

        return bean;
    }

}
