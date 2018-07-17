package au.com.suttons.notification.service;


import au.com.suttons.notification.data.dao.DepartmentDao;
import au.com.suttons.notification.data.dao.EmailDao;
import au.com.suttons.notification.data.entity.EmailLogEntity;
import au.com.suttons.notification.resource.bean.RequestBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless
public class EmailService
{
	//Email
    public static final String EMAIL_SOURCETYPE_INVOICE = "INVOICE";
    public static final String EMAIL_SOURCETYPE_STATEMENT = "STATEMENT";
    public static final String EMAIL_SOURCETYPE_INTAKE = "INTAKE";
    public static final String EMAIL_SOURCETYPE_DELIVERY = "DELIVERY";
    public static final String EMAIL_SOURCETYPE_DAILYDELIVERYRUN = "DAILYDELIVERYRUN";
    public static final String EMAIL_SOURCETYPE_ADDUSER = "ADDUSER";

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @EJB protected EmailDao emailDao;
    @EJB protected DepartmentDao departmentDao;

    public EmailLogEntity saveEmail(Long sourceId, String sourceType, Long departmentId, RequestBean requestBean)
    {
        EmailLogEntity emailEntity = new EmailLogEntity();
        emailEntity.setSourceId(sourceId);
        emailEntity.setSourceType(sourceType);

        if (departmentId != null) {
            emailEntity.setDepartment(this.departmentDao.getReference(departmentId));
        }

        emailEntity.setLastUpdatedBy(requestBean.getUserId());

        return this.emailDao.saveAndFlush(emailEntity);
    }

}
