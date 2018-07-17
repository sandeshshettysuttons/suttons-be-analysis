package au.com.suttons.notification.mapper;

import au.com.suttons.notification.data.entity.DepartmentEntity;
import au.com.suttons.notification.data.entity.UserEntity;
import au.com.suttons.notification.data.entity.TemplateEntity;
import au.com.suttons.notification.data.entity.UserAccessEntity;
import au.com.suttons.notification.resource.bean.*;
import au.com.suttons.notification.resource.page.Page;

import java.util.ArrayList;
import java.util.List;

public class UserAccessMapper extends BaseMapper
{

    public UserAccessMapper(RequestBean requestBean) {
        super(requestBean);
    }

    public UserAccessMapper(RequestBean requestBean, int depth) {
        super(requestBean, depth);
    }

    public UserAccessBean toBean(UserAccessEntity entity)
    {
        if (entity == null) { return null; }

        UserAccessBean bean = new UserAccessBean();

        bean.setId(entity.getId());
        bean.setVersion(entity.getVersion());

        if(this.isValidLevel() && entity.getUser() != null) {
            UserBean user = new UserMapper(this.getRequestBean(), this.getDepth()).toBean(entity.getUser());
            bean.setUser(user);
        }

        if(this.isValidLevel() && entity.getDepartment() != null) {
            DepartmentBean departmentEntity = new DepartmentMapper(this.getRequestBean(), this.getDepth()).toBean(entity.getDepartment());
            bean.setDepartment(departmentEntity);
        }

        if(this.isValidLevel() && entity.getTemplate() != null) {
            TemplateBean template = new TemplateMapper(this.getRequestBean(), this.getDepth()).toBean(entity.getTemplate());
            bean.setTemplate(template);
        }

        bean.setDepartmentCode(this.getRequestBean().getDepartmentCode());
        return bean;
    }

    public List<UserAccessBean> toBeans(List<UserAccessEntity> entities)
    {
        List<UserAccessBean> beans = new ArrayList<UserAccessBean>();

        for (UserAccessEntity e : entities) {
            beans.add(this.toBean(e));
        }

        return beans;
    }

    public BaseCollectionBean<UserAccessBean> toCollectionBean(Page<UserAccessEntity> page)
    {
        BaseCollectionBean<UserAccessBean> baseCollectionBean = new BaseCollectionBean(page);

        List<UserAccessBean> beans = this.toBeans(page.getContent());
        baseCollectionBean.setContent(beans);

        return baseCollectionBean;
    }

    public UserAccessEntity toEntity(UserAccessBean bean, UserAccessEntity entity)
    {

        if (entity == null) {
            bean.setIsUpdating(false);
            entity = new UserAccessEntity();
        } else {
            bean.setIsUpdating(true);
        }

        entity.setVersion(bean.getVersion());

        if(bean.isUpdatable("user")) {
            if(bean.getUser() != null) {
                UserEntity userEntity = new UserEntity();
                userEntity.setId(bean.getUser().getId());
                entity.setUser(userEntity);
            }
        }

        if(bean.isUpdatable("department")) {
            if(bean.getDepartment() != null) {
                DepartmentEntity departmentEntity = new DepartmentEntity();
                departmentEntity.setId(bean.getDepartment().getId());
                entity.setDepartment(departmentEntity);
            }
        }

        if(bean.isUpdatable("template")) {
            if(bean.getTemplate() != null) {
                TemplateEntity templateEntity = new TemplateEntity();
                templateEntity.setId(bean.getTemplate().getId());
                entity.setTemplate(templateEntity);
            }
        }

        entity.setLastUpdatedBy(this.getRequestBean().getUserId());

        return entity;
    }
}
