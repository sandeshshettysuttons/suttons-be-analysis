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
        List<EmployeeEntity> allEmployees;
        if (JobConstants.NOTIFICATION_TYPE_WEEKLY.equals(notificationType)) {
            allEmployees = this.employeeDao.findActiveUnsent();
        } else {
            allEmployees = this.employeeDao.findAllActive();
        }

        if (mailRecipients != null) {

            for (MailRecipientEntity mailRecipient: mailRecipients) {

                // Step 3 : Filter employees based on mail recipient - GLOBAL / COMPANY
                List<EmployeeEntity> employees = null;

                if (JobConstants.MAIL_RECIPIENT_TYPE_GLOBAL.equals(mailRecipient.getType())) {

                    employees = allEmployees;

                } else  if (mailRecipient.getCompany() != null) {

                    employees = filterEmployeesByCompany(allEmployees, mailRecipient.getCompany().getId());
                }

                if (CollectionUtils.isNotEmpty(employees)) {

                    TerminationNotificationBean terminationNotification = new TerminationNotificationBean();

                    // Step 4 : Group employees by time to termination date
                    populateTerminationEmployeeDetails(notificationType, terminationNotification, employees);

                    // Step 5 : Send email
                    if (CollectionUtils.isNotEmpty(terminationNotification.getEmployeeGroups())) {

                        terminationNotification.setMailRecipientName(mailRecipient.getName());
                        terminationNotification.setMailRecipientEmail(mailRecipient.getEmail());

                        try {
                            this.emailService.sendTerminationNotification(terminationNotification);
                            this.emailService.saveEmailLogs(mailRecipient, terminationNotification, JobConstants.STATUS_SENT);

                            // Set each Employee SENT flag to TRUE
                            if (JobConstants.NOTIFICATION_TYPE_WEEKLY.equals(notificationType)) {
                                setEmployeeSentFlag(terminationNotification);
                            }


                        } catch (Exception ex) {
                            logger.error("Exception while attempting to send termination notification.", ex);
                            this.emailService.saveEmailLogs(mailRecipient, terminationNotification, JobConstants.STATUS_ERROR);
                        }
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
        bean.setTerminationReason(entity.getTerminationReason());
        bean.setTerminationDescription(entity.getTerminationDescription());
        bean.setComment(entity.getComment());
        bean.setStatus(entity.getStatus());

        return bean;
    }

    private List<EmployeeEntity> filterEmployeesByCompany(List<EmployeeEntity> employees, Long companyId) {

        List<EmployeeEntity> filteredList = new ArrayList<>();

        for (EmployeeEntity employee : employees) {

            if (employee.getCompany() != null
                    && companyId.equals(employee.getCompany().getId())) {

                filteredList.add(employee);
            }
        }

        return filteredList;
    }

    private void setEmployeeSentFlag(TerminationNotificationBean terminationNotification) {

        if (terminationNotification.getEmployeeGroups() != null) {

            for (EmployeeGroupBean employeeGroup : terminationNotification.getEmployeeGroups()) {

                if (employeeGroup.getEmployees() != null) {

                    for (EmployeeBean employee : employeeGroup.getEmployees()) {
                        EmployeeEntity employeeEntity = this.employeeDao.findOne(employee.getId());
                        employeeEntity.setNotificationSent(true);
                    }

                }

            }

        }

    }

}
