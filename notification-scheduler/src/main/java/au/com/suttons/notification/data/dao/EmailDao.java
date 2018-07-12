package au.com.suttons.notification.data.dao;

import au.com.suttons.notification.data.entity.EmailLogEntity;

import javax.ejb.Stateless;

@Stateless
public class EmailDao extends BaseDao<EmailLogEntity>
{

    public EmailLogEntity getReference(Long id) {
        return this.entityManager.getReference(EmailLogEntity.class, id);
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