package au.com.suttons.notification.mapper;

import java.util.ArrayList;
import java.util.List;

import au.com.suttons.notification.data.entity.LookupEntity;
import au.com.suttons.notification.resource.bean.BaseCollectionBean;
import au.com.suttons.notification.resource.bean.LookupBean;
import au.com.suttons.notification.resource.bean.RequestBean;
import au.com.suttons.notification.resource.page.Page;

public class LookupMapper extends BaseMapper
{

    public LookupMapper(RequestBean requestBean) {
        super(requestBean);
    }

    public LookupMapper(RequestBean requestBean, int depth) {
        super(requestBean, depth);
    }

    public LookupBean toBean(LookupEntity entity)
    {
        if (entity == null) { return null; }

        LookupBean bean = new LookupBean();

        bean.setId(entity.getId());
        bean.setVersion(entity.getVersion());
        bean.setType(entity.getType());
        bean.setSequence(entity.getSequence());
        bean.setLabel(entity.getLabel());
        bean.setValue(entity.getValue());

        return bean;
    }

    public List<LookupBean> toBeans(List<LookupEntity> entities)
    {
        List<LookupBean> beans = new ArrayList<LookupBean>();

        for (LookupEntity e : entities) {
            beans.add(this.toBean(e));
        }

        return beans;
    }

    public BaseCollectionBean<LookupBean> toCollectionBean(Page<LookupEntity> page)
    {
        BaseCollectionBean<LookupBean> baseCollectionBean = new BaseCollectionBean(page);

        List<LookupBean> beans = this.toBeans(page.getContent());
        baseCollectionBean.setContent(beans);

        return baseCollectionBean;
    }
}
