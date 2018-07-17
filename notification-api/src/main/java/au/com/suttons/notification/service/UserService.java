package au.com.suttons.notification.service;


import au.com.suttons.notification.data.dao.UserDao.UserCriteria;
import au.com.suttons.notification.data.entity.UserAccessEntity;
import au.com.suttons.notification.data.entity.UserEntity;
import au.com.suttons.notification.mapper.UserMapper;
import au.com.suttons.notification.model.TemplateSearchParameters;
import au.com.suttons.notification.model.UserSearchParameters;
import au.com.suttons.notification.resource.bean.BaseCollectionBean;
import au.com.suttons.notification.resource.bean.RequestBean;
import au.com.suttons.notification.resource.bean.UserBean;
import au.com.suttons.notification.resource.page.Page;
import au.com.suttons.notification.resource.page.Pageable;
import au.com.suttons.notification.resource.page.Specification;
import au.com.suttons.notification.util.DateUtil;
import au.com.suttons.notification.util.EncryptionUtils;
import au.com.suttons.notification.validator.UserValidator;
import au.com.suttons.notification.validator.ValidationHelper;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless
public class UserService extends BaseService
{
    protected @EJB
    UserValidator userValidator;
	private @EJB DepartmentService departmentService;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public BaseCollectionBean<UserBean> getUsers(Pageable pageable, UserSearchParameters searchParameters, RequestBean requestBean) throws Exception
    {

        if(!requestBean.isSystemAdmin()) {
            //Filter by accessLevel
            UserEntity user = this.userDao.findByDepartmentCodeAndId(requestBean.getDepartmentCode(), requestBean.getUserId());
            TemplateSearchParameters.LowerAccessLevelRange lowerAccessLevelRange = new TemplateSearchParameters().new LowerAccessLevelRange(Long.MAX_VALUE, Long.MIN_VALUE);
            for(UserAccessEntity userAccess : user.getUserAccess()) {
                if(lowerAccessLevelRange.getLowerAccessLevelFrom() > userAccess.getTemplate().getLowerAccessLevelFrom()) {
                    lowerAccessLevelRange.setLowerAccessLevelFrom(userAccess.getTemplate().getLowerAccessLevelFrom());
                }
                if(lowerAccessLevelRange.getLowerAccessLevelTo() < userAccess.getTemplate().getLowerAccessLevelTo()) {
                    lowerAccessLevelRange.setLowerAccessLevelTo(userAccess.getTemplate().getLowerAccessLevelTo());
                }
            }
            searchParameters.setLowerAccessLevelRange(lowerAccessLevelRange);

        }

        Specification spec = ((UserCriteria)this.userDao.getCriteria()).byParameters(searchParameters);
        Page<UserEntity> page = this.userDao.getPage(pageable, spec, UserEntity.class);

        return new UserMapper(requestBean).toCollectionBean(page);
    }

    public UserBean getUser(long id, RequestBean requestBean)
    {
        UserEntity entity = this.userDao.findById(id);
        ValidationHelper.getInstance().validateNotFound(entity, "User ID: " + id);

        return new UserMapper(requestBean).toBean(entity);
    }

    //This method is called on user login
    public UserBean getUserByLogin(String login, RequestBean requestBean)
    {
        UserEntity entity = this.userDao.findByLoginAndIsActive(login);
        ValidationHelper.getInstance().validateNotFound(entity, "User Login Username: " + login);

        entity.setLastLoginIp(requestBean.getIpAddress());
        entity.setLastLoginTS(DateUtil.getCurrentDate());
        entity.setLastUpdatedBy(requestBean.getUserId());
        entity = this.userDao.saveAndFlush(entity);

        UserBean userBean = new UserMapper(requestBean).toBean(entity);
        if(entity.isIsSystemAdmin()) {
            userBean.setDepartments(this.departmentService.getAllDepartments(requestBean));
        }

        return userBean;
    }

    //This method is called on user login (pin)
    public UserBean getUserByPinLogin(String trainingId, String pin, String sessionId, RequestBean requestBean)
    {
        UserEntity entity = this.userDao.findByLoginIdAndPassword(trainingId, EncryptionUtils.MD5Hex(pin));
        ValidationHelper.getInstance().validateNotFound(entity, "User Training Id: " + trainingId);

        entity.setNonADLoginSessionId(sessionId);
        entity.setLastLoginIp(requestBean.getIpAddress());
        entity.setLastLoginTS(DateUtil.getCurrentDate());
        entity.setLastUpdatedBy(entity.getId());
        entity = this.userDao.saveAndFlush(entity);

        this.userDao.removeSessionIdExcludingId(sessionId, entity.getId());
        
        requestBean.setUserId(entity.getId());
        requestBean.setDisplayName(entity.getPrintName());

        UserBean userBean = new UserMapper(requestBean).toBean(entity);
        if(entity.isIsSystemAdmin()) {
            userBean.setDepartments(this.departmentService.getAllDepartments(requestBean));
        }

        return userBean;
    }

    public UserBean saveUser(UserBean userBean, RequestBean requestBean, boolean editProfile) {

    	//validator returns entity if validation succeeded
        UserEntity userEntity =
        		this.userValidator.onSave(userBean, requestBean);

        if(userBean.getLoginId() == null) {
        	//TODO should be unique - SSH check
        }

        userEntity = new UserMapper(requestBean).toEntity(userBean, userEntity);

        if(CollectionUtils.isNotEmpty(userEntity.getUserAccess())) {
            for(UserAccessEntity userAccessEntity : userEntity.getUserAccess()) {
                userAccessEntity.setDepartment(this.departmentDao.getReference(userAccessEntity.getDepartment().getId()));
                userAccessEntity.setTemplate(this.templateDao.getReference(userAccessEntity.getTemplate().getId()));
                userAccessEntity.setUser(userEntity);
            }
        }

        userEntity = this.userDao.saveAndFlush(userEntity);
        return new UserMapper(requestBean).toBean(userEntity);
    }



//    public UserBean disableUser(Long id, RequestBean requestBean) {
//
//    	//validator returns entity if validation succeeded
//    	UserEntity userEntity =
//        		this.userValidator.onDisable(id, requestBean);
//
//    	//set disabled status
//    	userEntity.setStatus(Constants.USER_STATUS_DISABLED);
//
//    	userEntity = this.userDao.saveAndFlush(userEntity);
//        return new UserMapper(requestBean).toBean(userEntity);
//    }
//
//    public UserBean enableUser(Long id, RequestBean requestBean) {
//
//    	//validator returns entity if validation succeeded
//    	UserEntity userEntity =
//    			this.userValidator.onEnable(id, requestBean);
//
//    	//set active status
//    	userEntity.setStatus(Constants.USER_STATUS_ACTIVE);
//
//    	userEntity = this.userDao.saveAndFlush(userEntity);
//        return new UserMapper(requestBean).toBean(userEntity);
//    }
}
