package au.com.suttons.notification.mapper;

import java.util.ArrayList;
import java.util.List;

import au.com.suttons.notification.data.entity.RoleEntity;
import au.com.suttons.notification.resource.bean.BaseCollectionBean;
import au.com.suttons.notification.resource.bean.RequestBean;
import au.com.suttons.notification.resource.bean.RoleBean;
import au.com.suttons.notification.resource.page.Page;

public class RoleMapper extends BaseMapper
{
    public RoleMapper(RequestBean requestBean) {
        super(requestBean);
    }

    public RoleMapper(RequestBean requestBean, int depth) {
        super(requestBean, depth);
    }

    public RoleBean toBean(RoleEntity entity)
    {
        if(entity == null){
            return null;
        }

        RoleBean bean = new RoleBean();

        bean.setName(entity.getName());
        bean.setDescription(entity.getDescription());
        bean.setId(entity.getId());

        return bean;
    }

    public List<RoleBean> toBeans(List<RoleEntity> entities)
    {
        List<RoleBean> beans = new ArrayList<RoleBean>();

        for (RoleEntity e : entities) {
            beans.add(this.toBean(e));
        }

        return beans;
    }

    public BaseCollectionBean<RoleBean> toCollectionBean(Page<RoleEntity> page){
        BaseCollectionBean<RoleBean> baseCollectionBean = new BaseCollectionBean(page);

        List<RoleBean> beans = this.toBeans(page.getContent());
        baseCollectionBean.setContent(beans);

        return baseCollectionBean;
    }

    public RoleEntity toEntity(RoleBean bean)
    {
        if (bean == null) {
            return null;
        }

        return this.toEntity(bean, null);
    }

    public RoleEntity toEntity(RoleBean bean, RoleEntity entity)
    {
        if (bean == null) {
            return null;
        }

        if (entity == null) {
            entity = new RoleEntity();
        }

        entity.setId(bean.getId());
        entity.setName(bean.getName());
        entity.setDescription(bean.getDescription());

        entity.setLastUpdatedBy(this.getRequestBean().getUserId());
        return entity;
    }
}
