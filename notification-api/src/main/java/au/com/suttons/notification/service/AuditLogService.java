package au.com.suttons.notification.service;


import au.com.suttons.notification.data.dao.AuditLogDao.AuditLogCriteria;
import au.com.suttons.notification.data.entity.AuditLogEntity;
import au.com.suttons.notification.data.entity.UserEntity;
import au.com.suttons.notification.interceptor.annotation.AuditLog;
import au.com.suttons.notification.mapper.AuditLogMapper;
import au.com.suttons.notification.model.AuditLogSearchParameters;
import au.com.suttons.notification.resource.bean.AuditLogBean;
import au.com.suttons.notification.resource.bean.BaseCollectionBean;
import au.com.suttons.notification.resource.bean.RequestBean;
import au.com.suttons.notification.resource.page.Page;
import au.com.suttons.notification.resource.page.Pageable;
import au.com.suttons.notification.resource.page.Specification;
import au.com.suttons.notification.util.DateUtil;
import au.com.suttons.notification.validator.ValidationHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;

@Stateless
public class AuditLogService extends BaseService
{
    private static final Logger logger = LoggerFactory.getLogger(AuditLogService.class);

    public BaseCollectionBean<AuditLogBean> getAuditLogs(Pageable pageable, AuditLogSearchParameters searchParameters, RequestBean requestBean) throws Exception
    {
        UserEntity user = this.userDao.findById(requestBean.getUserId());
        searchParameters.setUser(user);

        Specification<AuditLogEntity> spec = ((AuditLogCriteria)this.auditLogDao.getCriteria()).byParameters(searchParameters);
        Page<AuditLogEntity> page = this.auditLogDao.getPage(pageable, spec, AuditLogEntity.class);

        return new AuditLogMapper(requestBean).toCollectionBean(page);
    }

    public AuditLogBean getAuditLog(long id, RequestBean requestBean)
    {
        AuditLogEntity auditLogEntity = this.auditLogDao.findOne(id);
        ValidationHelper.getInstance().validateNotFound(auditLogEntity, "AuditLog ID: " + id);
        ValidationHelper.getInstance().validateAccessToDepartment(auditLogEntity.getDepartment().getCode(), requestBean);

        return new AuditLogMapper(requestBean).toBean(auditLogEntity);
    }

    public AuditLogEntity addAuditLog(AuditLog.Type activityType, String resource, Long resourceId, String description, boolean isPublic, RequestBean requestBean) {
    	AuditLogEntity auditLogEntity = new AuditLogEntity();
    	auditLogEntity.setActivityType(activityType.name());
    	auditLogEntity.setActivityDate(DateUtil.getCurrentSqlDate());
    	auditLogEntity.setChannel(requestBean.getChannel());
    	auditLogEntity.setIpAddress(requestBean.getIpAddress());
    	auditLogEntity.setDescription(description);
    	auditLogEntity.setIsPublic(isPublic);
		auditLogEntity.setActivityResource(resource);
		auditLogEntity.setActivityResourceId(resourceId);
    	auditLogEntity.setLastUpdatedBy(requestBean.getUserId());

        auditLogEntity.setUser(this.userDao.findOne(requestBean.getUserId()));
        auditLogEntity.setDepartment(this.departmentDao.findByCode(requestBean.getDepartmentCode()));

        auditLogEntity = this.auditLogDao.saveAndFlush(auditLogEntity);
        return auditLogEntity;
    }
}
