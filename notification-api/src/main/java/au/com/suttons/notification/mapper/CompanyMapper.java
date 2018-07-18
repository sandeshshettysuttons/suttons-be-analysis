package au.com.suttons.notification.mapper;

import au.com.suttons.notification.data.entity.CompanyEntity;
import au.com.suttons.notification.resource.bean.BaseCollectionBean;
import au.com.suttons.notification.resource.bean.CompanyBean;
import au.com.suttons.notification.resource.bean.RequestBean;
import au.com.suttons.notification.resource.page.Page;

import java.util.ArrayList;
import java.util.List;

public class CompanyMapper extends BaseMapper
{

    public CompanyMapper(RequestBean requestBean) {
        super(requestBean);
    }

    public CompanyMapper(RequestBean requestBean, int depth) {
        super(requestBean, depth);
    }

    public CompanyBean toBean(CompanyEntity entity)
    {
        if (entity == null) { return null; }

        CompanyBean bean = new CompanyBean();

        bean.setId(entity.getId());
        bean.setVersion(entity.getVersion());

        bean.setCode(entity.getCode());
        bean.setName(entity.getName());
        bean.setStatus(entity.getStatus());

        bean.setDepartmentCode(this.getRequestBean().getDepartmentCode());
        return bean;
    }

    public List<CompanyBean> toBeans(List<CompanyEntity> entities)
    {
        List<CompanyBean> beans = new ArrayList<CompanyBean>();

        for (CompanyEntity e : entities) {
            beans.add(this.toBean(e));
        }

        return beans;
    }

    public BaseCollectionBean<CompanyBean> toCollectionBean(Page<CompanyEntity> page)
    {
        BaseCollectionBean<CompanyBean> baseCollectionBean = new BaseCollectionBean(page);

        List<CompanyBean> beans = this.toBeans(page.getContent());
        baseCollectionBean.setContent(beans);

        return baseCollectionBean;
    }

    public CompanyEntity toEntity(CompanyBean bean, CompanyEntity entity)
    {

        if (entity == null) {
            bean.setIsUpdating(false);
            entity = new CompanyEntity();
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
        if(bean.isUpdatable("status")) {
            entity.setStatus(bean.getStatus());
        }

        entity.setLastUpdatedBy(this.getRequestBean().getUserId());

        return entity;
    }
}
