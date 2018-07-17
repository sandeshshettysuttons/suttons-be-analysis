package au.com.suttons.notification.service;


import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.com.suttons.notification.data.dao.UserAccessDao.UserAccessCriteria;
import au.com.suttons.notification.data.entity.UserAccessEntity;
import au.com.suttons.notification.mapper.UserAccessMapper;
import au.com.suttons.notification.model.UserAccessSearchParameters;
import au.com.suttons.notification.resource.bean.BaseCollectionBean;
import au.com.suttons.notification.resource.bean.RequestBean;
import au.com.suttons.notification.resource.bean.UserAccessBean;
import au.com.suttons.notification.resource.page.Page;
import au.com.suttons.notification.resource.page.Pageable;
import au.com.suttons.notification.resource.page.Specification;
import au.com.suttons.notification.validator.UserAccessValidator;
import au.com.suttons.notification.validator.ValidationHelper;

@Stateless
public class UserAccessService extends BaseService
{
    protected @EJB UserAccessValidator userAccessValidator;

    private static final Logger logger = LoggerFactory.getLogger(UserAccessService.class);

    public BaseCollectionBean<UserAccessBean> getUserAccesss(Pageable pageable, UserAccessSearchParameters searchParameters, RequestBean requestBean) throws Exception
    {
        Specification<UserAccessEntity> spec = ((UserAccessCriteria)this.userAccessDao.getCriteria()).byParameters(searchParameters);
        Page<UserAccessEntity> page = this.userAccessDao.getPage(pageable, spec, UserAccessEntity.class);

        return new UserAccessMapper(requestBean).toCollectionBean(page);
    }

    public UserAccessBean getUserAccess(long id, RequestBean requestBean)
    {
        UserAccessEntity entity = this.userAccessDao.findById(id);
        ValidationHelper.getInstance().validateNotFound(entity, "UserAccess ID: " + id);
        ValidationHelper.getInstance().validateAccessToDepartment(entity.getDepartment().getCode(), requestBean);

        return new UserAccessMapper(requestBean).toBean(entity);
    }

    public UserAccessBean saveUserAccess(UserAccessBean userAccessBean, RequestBean requestBean, boolean editProfile) {

    	//validator returns entity if validation succeeded
        UserAccessEntity entity = 
        		this.userAccessValidator.onSave(userAccessBean, requestBean);

        entity = new UserAccessMapper(requestBean).toEntity(userAccessBean, entity);

    	//map referenced objects to persist or merge
        entity.setUser(this.userDao.getReference(entity.getUser().getId()));
        if(entity.getDepartment() != null) {
        	entity.setDepartment(this.departmentDao.getReference(entity.getDepartment().getId()));
        }
        entity.setTemplate(this.templateDao.getReference(entity.getTemplate().getId()));

        entity = this.userAccessDao.saveAndFlush(entity);
        return new UserAccessMapper(requestBean).toBean(entity);
    }

    public void deleteUserAccess(Long id, RequestBean requestBean) {

    	//validator returns entity if validation succeeded
        UserAccessEntity userAccessEntity = 
        		this.userAccessValidator.onDelete(id, requestBean);

    	this.userAccessDao.delete(userAccessEntity);
    }
}
