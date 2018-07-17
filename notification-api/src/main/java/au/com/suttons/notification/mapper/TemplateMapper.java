package au.com.suttons.notification.mapper;

import java.util.ArrayList;
import java.util.List;

import au.com.suttons.notification.data.entity.RoleEntity;
import au.com.suttons.notification.data.entity.TemplateEntity;
import au.com.suttons.notification.resource.bean.BaseCollectionBean;
import au.com.suttons.notification.resource.bean.RequestBean;
import au.com.suttons.notification.resource.bean.RoleBean;
import au.com.suttons.notification.resource.bean.TemplateBean;
import au.com.suttons.notification.resource.page.Page;

public class TemplateMapper extends BaseMapper
{

    public TemplateMapper(RequestBean requestBean) {
        super(requestBean);
    }

    public TemplateMapper(RequestBean requestBean, int depth) {
        super(requestBean, depth);
    }

    public TemplateBean toBean(TemplateEntity entity)
    {
        if(entity == null){
            return null;
        }

        TemplateBean bean = new TemplateBean();

        bean.setName(entity.getName());
        bean.setDescription(entity.getDescription());
        bean.setId(entity.getId());
        bean.setVersion(entity.getVersion());
        
        bean.setAccessLevel(entity.getAccessLevel());
        bean.setLowerAccessLevelFrom(entity.getLowerAccessLevelFrom());
        bean.setLowerAccessLevelTo(entity.getLowerAccessLevelTo());

        if(this.isValidLevel() && entity.getRoles() != null) {
            List<RoleBean> roleBeans = new ArrayList<RoleBean>();
            for(RoleEntity role : entity.getRoles()) {
                roleBeans.add(new RoleMapper(this.getRequestBean(), this.getDepth()).toBean(role));
            }
            bean.setRoles(roleBeans);
        }

        bean.setDepartmentCode(this.getRequestBean().getDepartmentCode());
        return bean;
    }

    public List<TemplateBean> toBeans(List<TemplateEntity> entities)
    {
        List<TemplateBean> beans = new ArrayList<TemplateBean>();

        for (TemplateEntity e : entities) {
            beans.add(this.toBean(e));
        }

        return beans;
    }

    public BaseCollectionBean<TemplateBean> toCollectionBean(Page<TemplateEntity> page){
        BaseCollectionBean<TemplateBean> baseCollectionBean = new BaseCollectionBean(page);

        List<TemplateBean> beans = this.toBeans(page.getContent());
        baseCollectionBean.setContent(beans);

        return baseCollectionBean;
    }

    public TemplateEntity toEntity(TemplateBean bean)
    {
        if (bean == null) {
            return null;
        }

        return this.toEntity(bean, null);
    }

    public TemplateEntity toEntity(TemplateBean bean, TemplateEntity entity)
    {
        if (bean == null) {
            return null;
        }

        if (entity == null) {
            bean.setIsUpdating(false);
            entity = new TemplateEntity();
        } else {
            bean.setIsUpdating(true);
        }

        entity.setId(bean.getId());
        
        if(bean.isUpdatable("name")) {
            entity.setName(bean.getName());
        }
        if(bean.isUpdatable("description")) {
        	entity.setDescription(bean.getDescription());
        }

        if(bean.isUpdatable("accessLevel")) {
        	entity.setAccessLevel(bean.getAccessLevel());
        }
        if(bean.isUpdatable("lowerAccessLevelFrom")) {
        	entity.setLowerAccessLevelFrom(bean.getLowerAccessLevelFrom());
        }
        if(bean.isUpdatable("lowerAccessLevelTo")) {
        	entity.setLowerAccessLevelTo(bean.getLowerAccessLevelTo());
        }
        
        if(bean.isUpdatable("roles")) {
            if(bean.getRoles() != null) {
                entity.setRoles(new ArrayList<RoleEntity>());
                for(RoleBean roleBean : bean.getRoles()) {
                    RoleEntity roleEntity = new RoleEntity();
                    roleEntity.setId(roleBean.getId());
                    if(roleBean.getId() == null){
                    	roleEntity.setName(roleBean.getName());
                    	roleEntity.setDescription(roleBean.getDescription());
                    }
                    entity.getRoles().add(roleEntity);
                }
            }
        }
        
        entity.setLastUpdatedBy(this.getRequestBean().getUserId());
        return entity;
    }
}
