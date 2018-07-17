package au.com.suttons.notification.mapper;

import au.com.suttons.notification.data.entity.CompanyEntity;
import au.com.suttons.notification.data.entity.MailRecipientEntity;
import au.com.suttons.notification.resource.bean.BaseCollectionBean;
import au.com.suttons.notification.resource.bean.MailRecipientBean;
import au.com.suttons.notification.resource.bean.RequestBean;
import au.com.suttons.notification.resource.page.Page;

import java.util.ArrayList;
import java.util.List;

public class MailRecipientMapper extends BaseMapper
{

    public MailRecipientMapper(RequestBean requestBean) {
        super(requestBean);
    }

    public MailRecipientMapper(RequestBean requestBean, int depth) {
        super(requestBean, depth);
    }

    public MailRecipientBean toBean(MailRecipientEntity entity)
    {
        if (entity == null) { return null; }

        MailRecipientBean bean = new MailRecipientBean();

        bean.setId(entity.getId());
        bean.setVersion(entity.getVersion());
        bean.setName(entity.getName());
        bean.setEmail(entity.getEmail());
        bean.setType(entity.getType());
        bean.setStatus(entity.getStatus());

        if (this.isValidLevel() && entity.getCompany() != null) {
            bean.setCompany(
                    new CompanyMapper(this.getRequestBean(), this.getDepth()).toBean(entity.getCompany()));
        }

        bean.setDepartmentCode(this.getRequestBean().getDepartmentCode());
        return bean;
    }

    public List<MailRecipientBean> toBeans(List<MailRecipientEntity> entities)
    {
        List<MailRecipientBean> beans = new ArrayList<MailRecipientBean>();

        for (MailRecipientEntity e : entities) {
            beans.add(this.toBean(e));
        }

        return beans;
    }

    public BaseCollectionBean<MailRecipientBean> toCollectionBean(Page<MailRecipientEntity> page)
    {
        BaseCollectionBean<MailRecipientBean> baseCollectionBean = new BaseCollectionBean(page);

        List<MailRecipientBean> beans = this.toBeans(page.getContent());
        baseCollectionBean.setContent(beans);

        return baseCollectionBean;
    }

    public MailRecipientEntity toEntity(MailRecipientBean bean, MailRecipientEntity entity)
    {

        if (entity == null) {
            bean.setIsUpdating(false);
            entity = new MailRecipientEntity();
        } else {
            bean.setIsUpdating(true);
        }

        entity.setVersion(bean.getVersion());

        if(bean.isUpdatable("name")) {
            entity.setName(bean.getName());
        }
        if(bean.isUpdatable("email")) {
            entity.setEmail(bean.getEmail());
        }
        if(bean.isUpdatable("type")) {
            entity.setType(bean.getType());
        }
        if(bean.isUpdatable("status")) {
            entity.setStatus(bean.getStatus());
        }
        if (bean.isUpdatable("company")) {
            if(bean.getCompany() != null && bean.getCompany().getId() != null) {
                CompanyEntity companyEntity = new CompanyEntity();
                companyEntity.setId(bean.getCompany().getId());
                entity.setCompany(companyEntity);
            } else {
                entity.setCompany(null);
            }
        }

        entity.setLastUpdatedBy(this.getRequestBean().getUserId());

        return entity;
    }
}
