package au.com.suttons.notification.data.dao;

import au.com.suttons.notification.data.entity.MailRecipientEntity;
import au.com.suttons.notification.jobs.JobConstants;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import java.util.List;

@Stateless
public class MailRecipientDao extends BaseDao<MailRecipientEntity>
{

    public MailRecipientEntity getReference(Long id) {
        return this.entityManager.getReference(MailRecipientEntity.class, id);
    }

    public List<MailRecipientEntity> findAllActive() {
        String sql = "SELECT mr FROM MailRecipientEntity mr WHERE mr.status = :activeStatus";

        try {

            return this.entityManager.createQuery(sql, MailRecipientEntity.class)
                    .setParameter("activeStatus", JobConstants.STATUS_ACTIVE)
                    .getResultList();

        } catch(NoResultException e) {
            return null;
        }
    }

}