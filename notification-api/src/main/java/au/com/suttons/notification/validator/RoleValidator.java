package au.com.suttons.notification.validator;

import java.util.List;

import javax.ejb.Stateless;

import au.com.suttons.notification.data.dao.RoleDao;
import au.com.suttons.notification.data.entity.RoleEntity;
import au.com.suttons.notification.mapper.RoleMapper;
import au.com.suttons.notification.model.ErrorCode;
import au.com.suttons.notification.resource.bean.RequestBean;
import au.com.suttons.notification.resource.bean.RoleBean;
import au.com.suttons.notification.resource.error.RestApiException;

@Stateless
public class RoleValidator extends BaseValidator {
	public void onSave(RoleBean roleBean, RoleDao roleDao, RequestBean requestBean) {
        // Validate unique role name
        long existingId = roleBean.getId() != null ? roleBean.getId() : -1;
        List<RoleEntity> conflictingEntities = roleDao.findByNameExcludingId(roleBean.getName(), existingId);
        if (conflictingEntities.size() > 0) {
            throw RestApiException.getBusinessRuleException(
            			ErrorCode.DT_B_002, 
            			new Object[] {roleBean.getName()}, 
            			new Object[] {roleBean.getName()},
            			new RoleMapper(requestBean).toBean(conflictingEntities.get(0)));
        }
	}
}
