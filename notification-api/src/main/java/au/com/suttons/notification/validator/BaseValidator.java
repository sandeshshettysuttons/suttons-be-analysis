package au.com.suttons.notification.validator;

import au.com.suttons.notification.data.dao.*;

import javax.ejb.EJB;

public class BaseValidator {
    protected @EJB DepartmentDao departmentDao;
    protected @EJB TemplateDao templateDao;
    protected @EJB UserAccessDao userAccessDao;
    protected @EJB UserDao userDao;

    protected @EJB CompanyDao companyDao;
    protected @EJB MailRecipientDao mailRecipientDao;

}
