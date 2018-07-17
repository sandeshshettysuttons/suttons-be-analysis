package au.com.suttons.notification.validator;

import au.com.suttons.notification.data.entity.MailRecipientEntity;
import au.com.suttons.notification.resource.bean.MailRecipientBean;
import au.com.suttons.notification.resource.bean.RequestBean;

import javax.ejb.Stateless;

@Stateless
public class MailRecipientValidator extends BaseValidator {

	public MailRecipientEntity onSave(MailRecipientBean mailRecipientBean, RequestBean requestBean) {
		
        MailRecipientEntity mailRecipientEntity = null;

        //1. check if entity exists on save
        if (mailRecipientBean.getId() != null) {
        	mailRecipientEntity = this.mailRecipientDao.findOne(mailRecipientBean.getId());
            ValidationHelper.getInstance().validateNotFound(mailRecipientEntity, "MailRecipient ID: " + mailRecipientBean.getId());
        }

		return mailRecipientEntity;
	}
}
