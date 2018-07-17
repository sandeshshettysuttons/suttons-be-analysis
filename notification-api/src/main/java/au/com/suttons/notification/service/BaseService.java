package au.com.suttons.notification.service;

import au.com.suttons.notification.data.dao.*;

import javax.ejb.EJB;

public class BaseService {

	protected @EJB AuditLogDao auditLogDao;
	protected @EJB DepartmentDao departmentDao;
	protected @EJB LookupDao lookupDao;
    protected @EJB RoleDao roleDao;
	protected @EJB SequenceGeneratorDao sequenceGeneratorDao;
    protected @EJB TemplateDao templateDao;
    protected @EJB UserAccessDao userAccessDao;
	protected @EJB UserDao userDao;

	protected @EJB CompanyDao companyDao;
	protected @EJB MailRecipientDao mailRecipientDao;
}
