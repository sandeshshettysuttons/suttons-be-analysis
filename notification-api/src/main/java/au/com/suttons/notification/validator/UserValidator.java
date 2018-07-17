package au.com.suttons.notification.validator;

import au.com.suttons.notification.data.entity.UserEntity;
import au.com.suttons.notification.mapper.UserMapper;
import au.com.suttons.notification.model.Constants;
import au.com.suttons.notification.model.ErrorCode;
import au.com.suttons.notification.resource.bean.RequestBean;
import au.com.suttons.notification.resource.bean.UserBean;
import au.com.suttons.notification.resource.error.RestApiException;
import org.apache.commons.lang.StringUtils;

import javax.ejb.Stateless;
import java.util.List;

@Stateless
public class UserValidator extends BaseValidator {

	public UserEntity onSave(UserBean userBean, RequestBean requestBean) {

        UserEntity userEntity = null;

        //1. check if entity exists on save
        if (userBean.getId() != null) {
        	userEntity = this.userDao.findOne(userBean.getId());
            ValidationHelper.getInstance().validateNotFound(userEntity, "User ID: " + userBean.getId());
        }

		//2. Username is unique (including disabled ones)
        long existingId = userBean.getId() != null ? userBean.getId() : -1;
        if(userBean.getUserName() != null) {
            List<UserEntity> conflictingEntities = this.userDao.findByUserIdExcludingId(userBean.getUserName(), existingId);
            if (conflictingEntities.size() > 0) {
                throw RestApiException.getBusinessRuleException(
                		ErrorCode.DT_B_002, 
                		new Object[] {userBean.getUserName()},
                		new Object[] {userBean.getUserName()},
                		new UserMapper(requestBean).toBean(conflictingEntities.get(0)));
            }
        }

        //3. Login id is unique
        if(userBean.getLoginId() != null) {
            List<UserEntity> conflictingEntities = this.userDao.findByLoginIdExcludingId(userBean.getLoginId(), existingId);
            if (conflictingEntities.size() > 0) {
                throw RestApiException.getBusinessRuleException(
                		ErrorCode.USR_B_001,
                		new Object[] {userBean.getLoginId()},
                		new Object[] {userBean.getLoginId()},
                		new UserMapper(requestBean).toBean(conflictingEntities.get(0)));
            }
        }

        //4. pin must be 4 digits number
        if(userBean.getPassword() != null) {
            if(!StringUtils.isNumeric(userBean.getPassword())) {
                throw RestApiException.getBusinessRuleException(ErrorCode.USR_B_002);
            }
        }

        return userEntity;
	}

	public UserEntity onDisable(Long id, RequestBean requestBean) {

		UserEntity userEntity = null;
        if (id != null) {
        	userEntity = this.userDao.findOne(id);
        }

        //1. check if entity exists
        ValidationHelper.getInstance().validateNotFound(userEntity, "User ID: " + id);

		//2. cannot disable if currently inactive
        if(Constants.USER_STATUS_DISABLED.equals(userEntity.getStatus())) {
            throw RestApiException.getBusinessRuleException(ErrorCode.API_C_002);
        }

        return userEntity;
	}

	public UserEntity onEnable(Long id, RequestBean requestBean) {

		UserEntity userEntity = null;
        if (id != null) {
        	userEntity = this.userDao.findOne(id);
        }

        //1. check if entity exists
        ValidationHelper.getInstance().validateNotFound(userEntity, "User ID: " + id);

		//2. cannot enable if currently active
        if(Constants.USER_STATUS_ACTIVE.equals(userEntity.getStatus())) {
            throw RestApiException.getBusinessRuleException(ErrorCode.API_C_003);
        }

        return userEntity;
	}
}
