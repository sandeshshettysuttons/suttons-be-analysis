package au.com.suttons.notification.mapper;

import java.util.ArrayList;
import java.util.List;

import au.com.suttons.notification.data.entity.DepartmentEntity;
import au.com.suttons.notification.resource.bean.BaseCollectionBean;
import au.com.suttons.notification.resource.bean.DepartmentBean;
import au.com.suttons.notification.resource.bean.RequestBean;
import au.com.suttons.notification.resource.page.Page;

public class DepartmentMapper extends BaseMapper
{

    public DepartmentMapper(RequestBean requestBean) {
        super(requestBean);
    }

    public DepartmentMapper(RequestBean requestBean, int depth) {
        super(requestBean, depth);
    }

    public DepartmentBean toBean(DepartmentEntity entity)
    {
        if (entity == null) { return null; }

        DepartmentBean bean = new DepartmentBean();

        bean.setId(entity.getId());
        bean.setVersion(entity.getVersion());
        bean.setCode(entity.getCode());
        bean.setName(entity.getName());
        bean.setIsActive(entity.getIsActive());

        bean.setDepartmentCode(this.getRequestBean().getDepartmentCode());
        return bean;
    }

    public List<DepartmentBean> toBeans(List<DepartmentEntity> entities)
    {
        List<DepartmentBean> beans = new ArrayList<DepartmentBean>();

        for (DepartmentEntity e : entities) {
            beans.add(this.toBean(e));
        }

        return beans;
    }

    public BaseCollectionBean<DepartmentBean> toCollectionBean(Page<DepartmentEntity> page)
    {
        BaseCollectionBean<DepartmentBean> baseCollectionBean = new BaseCollectionBean(page);

        List<DepartmentBean> beans = this.toBeans(page.getContent());
        baseCollectionBean.setContent(beans);

        return baseCollectionBean;
    }

    public DepartmentEntity toEntity(DepartmentBean bean, DepartmentEntity entity)
    {

        if (entity == null) {
            bean.setIsUpdating(false);
            entity = new DepartmentEntity();
        } else {
            bean.setIsUpdating(true);
        }

        entity.setVersion(bean.getVersion());

        if(bean.isUpdatable("code")) {
            entity.setCode(bean.getCode());
        }
        if(bean.isUpdatable("name")) {
            entity.setName(bean.getName());
        }
        if(bean.isUpdatable("isActive")) {
            entity.setIsActive(bean.getIsActive());
        }

        entity.setLastUpdatedBy(this.getRequestBean().getUserId());

        return entity;
    }
}
