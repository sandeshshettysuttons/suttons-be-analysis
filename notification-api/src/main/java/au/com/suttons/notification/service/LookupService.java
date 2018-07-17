package au.com.suttons.notification.service;


import javax.ejb.Stateless;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.com.suttons.notification.data.dao.LookupDao.LookupCriteria;
import au.com.suttons.notification.data.entity.LookupEntity;
import au.com.suttons.notification.mapper.LookupMapper;
import au.com.suttons.notification.resource.bean.BaseCollectionBean;
import au.com.suttons.notification.resource.bean.LookupBean;
import au.com.suttons.notification.resource.bean.RequestBean;
import au.com.suttons.notification.resource.page.Page;
import au.com.suttons.notification.resource.page.Pageable;
import au.com.suttons.notification.resource.page.Specification;

@Stateless
public class LookupService extends BaseService
{
    private static final Logger logger = LoggerFactory.getLogger(LookupService.class);

    public BaseCollectionBean<LookupBean> getLookups(Pageable pageable, String type, RequestBean requestBean) throws Exception
    {
        Specification<LookupEntity> spec = ((LookupCriteria)this.lookupDao.getCriteria()).byType(type);
        Page<LookupEntity> page = this.lookupDao.getPage(pageable, spec, LookupEntity.class);

        return new LookupMapper(requestBean).toCollectionBean(page);
    }
}
