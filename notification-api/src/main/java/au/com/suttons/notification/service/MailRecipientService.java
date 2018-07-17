package au.com.suttons.notification.service;


import au.com.suttons.notification.data.dao.MailRecipientDao.MailRecipientCriteria;
import au.com.suttons.notification.data.entity.MailRecipientEntity;
import au.com.suttons.notification.mapper.MailRecipientMapper;
import au.com.suttons.notification.model.MailRecipientSearchParameters;
import au.com.suttons.notification.resource.bean.BaseCollectionBean;
import au.com.suttons.notification.resource.bean.MailRecipientBean;
import au.com.suttons.notification.resource.bean.RequestBean;
import au.com.suttons.notification.resource.page.Page;
import au.com.suttons.notification.resource.page.Pageable;
import au.com.suttons.notification.resource.page.Specification;
import au.com.suttons.notification.validator.MailRecipientValidator;
import au.com.suttons.notification.validator.ValidationHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.List;

@Stateless
public class MailRecipientService extends BaseService
{
    protected @EJB MailRecipientValidator mailRecipientValidator;

    private static final Logger logger = LoggerFactory.getLogger(MailRecipientService.class);

    public BaseCollectionBean<MailRecipientBean> getMailRecipients(Pageable pageable, MailRecipientSearchParameters searchParameters, RequestBean requestBean) throws Exception
    {
        Specification<MailRecipientEntity> spec = ((MailRecipientCriteria)this.mailRecipientDao.getCriteria()).byParameters(searchParameters);
        Page<MailRecipientEntity> page = this.mailRecipientDao.getPage(pageable, spec, MailRecipientEntity.class);

        return new MailRecipientMapper(requestBean).toCollectionBean(page);
    }

    public MailRecipientBean getMailRecipient(long id, RequestBean requestBean)
    {
        MailRecipientEntity mailRecipientEntity = this.mailRecipientDao.findOne(id);
        ValidationHelper.getInstance().validateNotFound(mailRecipientEntity, "MailRecipient ID: " + id);

        return new MailRecipientMapper(requestBean).toBean(mailRecipientEntity);
    }

    public MailRecipientBean saveMailRecipient(MailRecipientBean mailRecipientBean, RequestBean requestBean) {

    	//validator returns entity if validation succeeded
    	MailRecipientEntity mailRecipientEntity =
        		this.mailRecipientValidator.onSave(mailRecipientBean, requestBean);

    	mailRecipientEntity = new MailRecipientMapper(requestBean).toEntity(mailRecipientBean, mailRecipientEntity);

    	if (mailRecipientEntity.getCompany() != null && mailRecipientEntity.getCompany().getId() != null) {
            mailRecipientEntity.setCompany(this.companyDao.getReference(mailRecipientEntity.getCompany().getId()));
        }

        mailRecipientEntity = this.mailRecipientDao.saveAndFlush(mailRecipientEntity);
        return new MailRecipientMapper(requestBean).toBean(mailRecipientEntity);
    }
}
