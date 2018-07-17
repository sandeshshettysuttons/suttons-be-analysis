package au.com.suttons.notification.validator;

import au.com.suttons.notification.data.entity.UserAccessEntity;
import au.com.suttons.notification.resource.bean.RequestBean;
import au.com.suttons.notification.resource.bean.UserAccessBean;

import javax.ejb.Stateless;

@Stateless
public class UserAccessValidator extends BaseValidator {

	public UserAccessEntity onSave(UserAccessBean userAccessBean, RequestBean requestBean) {

		UserAccessEntity userAccessEntity = null;

        //1. check if entity exists on save
        if (userAccessBean.getId() != null) {
        	userAccessEntity = this.userAccessDao.findOne(userAccessBean.getId());
            ValidationHelper.getInstance().validateNotFound(userAccessEntity, "UserAccess ID: " + userAccessBean.getId());
        }

        return userAccessEntity;
	}

	public UserAccessEntity onDelete(Long id, RequestBean requestBean) {

		UserAccessEntity userAccessEntity = null;
        if (id != null) {
        	userAccessEntity = this.userAccessDao.findOne(id);
        }

        //1. check if entity exists
        ValidationHelper.getInstance().validateNotFound(userAccessEntity, "UserAccess ID: " + id);

        return userAccessEntity;
	}
}
