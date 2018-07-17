package au.com.suttons.notification.validator;

import au.com.suttons.notification.data.entity.DepartmentEntity;
import au.com.suttons.notification.mapper.DepartmentMapper;
import au.com.suttons.notification.model.ErrorCode;
import au.com.suttons.notification.resource.bean.DepartmentBean;
import au.com.suttons.notification.resource.bean.RequestBean;
import au.com.suttons.notification.resource.error.RestApiException;
import org.apache.commons.collections.CollectionUtils;

import javax.ejb.Stateless;
import java.util.List;

@Stateless
public class DepartmentValidator extends BaseValidator {

	public DepartmentEntity onSave(DepartmentBean departmentBean, RequestBean requestBean) {
		
        DepartmentEntity departmentEntity = null;

        //1. check if entity exists on save
        if (departmentBean.getId() != null) {
        	departmentEntity = this.departmentDao.findOne(departmentBean.getId());
            ValidationHelper.getInstance().validateNotFound(departmentEntity, "Department ID: " + departmentBean.getId());
        }

		//2. name is unique (including disabled ones)
        long existingId = departmentBean.getId() != null ? departmentBean.getId() : -1;
		List<DepartmentEntity> conflictingEntities = departmentDao.findByCodeExcludingId(departmentBean.getCode(), existingId);
		if(!CollectionUtils.isEmpty(conflictingEntities)) {
            throw RestApiException.getBusinessRuleException(
            		ErrorCode.DT_B_008,
            		new Object[] {departmentBean.getCode()},
            		new Object[] {departmentBean.getCode()},
            		new DepartmentMapper(requestBean).toBean(conflictingEntities.get(0)));
		}

		return departmentEntity;
	}
}
