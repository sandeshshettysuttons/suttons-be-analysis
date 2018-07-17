package au.com.suttons.notification.mapper;

import java.util.ArrayList;
import java.util.List;

import au.com.suttons.notification.data.entity.AuditLogEntity;
import au.com.suttons.notification.resource.bean.AuditLogBean;
import au.com.suttons.notification.resource.bean.BaseCollectionBean;
import au.com.suttons.notification.resource.bean.UserBean;
import au.com.suttons.notification.resource.bean.RequestBean;
import au.com.suttons.notification.resource.page.Page;
import au.com.suttons.notification.util.DateUtil;

public class AuditLogMapper extends BaseMapper
{

    public AuditLogMapper(RequestBean requestBean) {
        super(requestBean);
    }

    public AuditLogMapper(RequestBean requestBean, int depth) {
        super(requestBean, depth);
    }

    public AuditLogBean toBean(AuditLogEntity entity)
    {
        if (entity == null) { return null; }

        AuditLogBean bean = new AuditLogBean();
        bean.setId(entity.getId());
        bean.setVersion(entity.getVersion());
        bean.setActivityType(entity.getActivityType());
        bean.setActivityResource(entity.getActivityResource());
        bean.setActivityResourceId(entity.getActivityResourceId());
        bean.setActivityDate(entity.getActivityDate());
        bean.setChannel(entity.getChannel());
        bean.setDescription(entity.getDescription());
        bean.setActivityTimestamp(DateUtil.formatDateTimeToString(entity.getCreationTS()));

        if(this.isValidLevel() && entity.getUser() != null) {
        	UserBean userBean = new UserMapper(this.getRequestBean(), this.getDepth()).toBean(entity.getUser());
            bean.setUser(userBean);
        }

        bean.setDepartmentCode(this.getRequestBean().getDepartmentCode());
        return bean;
    }

    public List<AuditLogBean> toBeans(List<AuditLogEntity> entities)
    {
        List<AuditLogBean> beans = new ArrayList<AuditLogBean>();

        for (AuditLogEntity e : entities) {
            beans.add(this.toBean(e));
        }

        return beans;
    }

    public BaseCollectionBean<AuditLogBean> toCollectionBean(Page<AuditLogEntity> page)
    {
        BaseCollectionBean<AuditLogBean> baseCollectionBean = new BaseCollectionBean(page);

        List<AuditLogBean> beans = this.toBeans(page.getContent());
        baseCollectionBean.setContent(beans);

        return baseCollectionBean;
    }
}
