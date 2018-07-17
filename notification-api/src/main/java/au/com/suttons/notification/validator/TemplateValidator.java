package au.com.suttons.notification.validator;

import java.util.List;

import javax.ejb.Stateless;

import au.com.suttons.notification.data.dao.TemplateDao;
import au.com.suttons.notification.data.entity.TemplateEntity;
import au.com.suttons.notification.mapper.TemplateMapper;
import au.com.suttons.notification.model.ErrorCode;
import au.com.suttons.notification.resource.bean.RequestBean;
import au.com.suttons.notification.resource.bean.TemplateBean;
import au.com.suttons.notification.resource.error.RestApiException;

@Stateless
public class TemplateValidator extends BaseValidator {
	public void onSave(TemplateBean templateBean, TemplateDao templateDao, RequestBean requestBean) {
        long existingId = templateBean.getId() != null ? templateBean.getId() : -1;
        List<TemplateEntity> conflictingEntities = templateDao.findByNameExcludingId(templateBean.getName(), existingId);
        if (conflictingEntities.size() > 0) {
            throw RestApiException.getBusinessRuleException(
            			ErrorCode.DT_B_002, 
            			new Object[] {templateBean.getName()}, 
            			new Object[] {templateBean.getName()}, 
            			new TemplateMapper(requestBean).toBean(conflictingEntities.get(0)));
        }
	}
}
