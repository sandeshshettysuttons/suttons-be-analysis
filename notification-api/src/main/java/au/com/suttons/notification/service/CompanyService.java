package au.com.suttons.notification.service;


import au.com.suttons.notification.data.dao.CompanyDao.CompanyCriteria;
import au.com.suttons.notification.data.entity.CompanyEntity;
import au.com.suttons.notification.mapper.CompanyMapper;
import au.com.suttons.notification.model.CompanySearchParameters;
import au.com.suttons.notification.resource.bean.BaseCollectionBean;
import au.com.suttons.notification.resource.bean.CompanyBean;
import au.com.suttons.notification.resource.bean.RequestBean;
import au.com.suttons.notification.resource.page.Page;
import au.com.suttons.notification.resource.page.Pageable;
import au.com.suttons.notification.resource.page.Specification;
import au.com.suttons.notification.validator.CompanyValidator;
import au.com.suttons.notification.validator.ValidationHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless
public class CompanyService extends BaseService
{
    protected @EJB CompanyValidator companyValidator;

    private static final Logger logger = LoggerFactory.getLogger(CompanyService.class);

    public BaseCollectionBean<CompanyBean> getCompanys(Pageable pageable, CompanySearchParameters searchParameters, RequestBean requestBean) throws Exception
    {
        Specification<CompanyEntity> spec = ((CompanyCriteria)this.companyDao.getCriteria()).byParameters(searchParameters);
        Page<CompanyEntity> page = this.companyDao.getPage(pageable, spec, CompanyEntity.class);

        return new CompanyMapper(requestBean).toCollectionBean(page);
    }

    public CompanyBean getCompany(long id, RequestBean requestBean)
    {
        CompanyEntity companyEntity = this.companyDao.findOne(id);
        ValidationHelper.getInstance().validateNotFound(companyEntity, "Company ID: " + id);

        return new CompanyMapper(requestBean).toBean(companyEntity);
    }

    public CompanyBean saveCompany(CompanyBean companyBean, RequestBean requestBean) {

    	//validator returns entity if validation succeeded
    	CompanyEntity companyEntity =
        		this.companyValidator.onSave(companyBean, requestBean);

    	companyEntity = new CompanyMapper(requestBean).toEntity(companyBean, companyEntity);

        companyEntity = this.companyDao.saveAndFlush(companyEntity);
        return new CompanyMapper(requestBean).toBean(companyEntity);
    }
}
