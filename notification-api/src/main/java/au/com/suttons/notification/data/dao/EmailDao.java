package au.com.suttons.notification.data.dao;

import au.com.suttons.notification.data.entity.EmailLogEntity;

import javax.ejb.Stateless;
import java.util.List;

@Stateless
public class EmailDao extends BaseDao<EmailLogEntity>
{
    public List<EmailLogEntity> findByUnsentRecords() {

        StringBuilder sql = new StringBuilder("SELECT e FROM EmailLogEntity e ");
        sql.append(" WHERE e.status IS NULL OR e.status != 'SENT' ");

        return this.entityManager.createQuery(sql.toString()).getResultList();
    }

    public EmailLogEntity saveAndFlush(EmailLogEntity entity) {
        if(entity.getId() != null) {
            this.entityManager.merge(entity);
        } else {
            this.entityManager.persist(entity);
        }

        this.entityManager.flush();
        return entity;
    }
}
 