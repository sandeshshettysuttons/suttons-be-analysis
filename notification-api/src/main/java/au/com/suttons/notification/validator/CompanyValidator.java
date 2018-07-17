package au.com.suttons.notification.validator;

import au.com.suttons.notification.data.entity.CompanyEntity;
import au.com.suttons.notification.resource.bean.CompanyBean;
import au.com.suttons.notification.resource.bean.RequestBean;

import javax.ejb.Stateless;

@Stateless
public class CompanyValidator extends BaseValidator {

	public CompanyEntity onSave(CompanyBean companyBean, RequestBean requestBean) {
		
        CompanyEntity companyEntity = null;

        //1. check if entity exists on save
        if (companyBean.getId() != null) {
        	companyEntity = this.companyDao.findOne(companyBean.getId());
            ValidationHelper.getInstance().validateNotFound(companyEntity, "Company ID: " + companyBean.getId());
        }

		return companyEntity;
	}
}
