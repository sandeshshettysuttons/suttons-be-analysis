package au.com.suttons.notification.data.dao;

import au.com.suttons.notification.data.entity.CompanyEntity;
import au.com.suttons.notification.jobs.JobConstants;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import java.util.List;

@Stateless
public class CompanyDao extends BaseDao<CompanyEntity>
{

    public CompanyEntity getReference(Long id) {
        return this.entityManager.getReference(CompanyEntity.class, id);
    }

    public List<CompanyEntity> findAllActive() {
        String sql = "SELECT c FROM CompanyEntity c WHERE c.status = :activeStatus";

        try {

            return this.entityManager.createQuery(sql, CompanyEntity.class)
                    .setParameter("activeStatus", JobConstants.STATUS_ACTIVE)
                    .getResultList();

        } catch(NoResultException e) {
            return null;
        }
    }

    public CompanyEntity findByName(String name) {
        String sql = "SELECT c FROM CompanyEntity c WHERE UPPER(c.name) = UPPER(:name) AND c.status = :activeStatus";

        try {

            return this.entityManager.createQuery(sql, CompanyEntity.class)
                    .setParameter("name", name)
                    .setParameter("activeStatus", JobConstants.STATUS_ACTIVE)
                    .getSingleResult();

        } catch(NoResultException e) {
            return null;
        }
    }

    public CompanyEntity findByCode(String code) {
        String sql = "SELECT c FROM CompanyEntity c WHERE c.code = :code AND c.status = :activeStatus";

        try {

            return this.entityManager.createQuery(sql, CompanyEntity.class)
                    .setParameter("code", code)
                    .setParameter("activeStatus", JobConstants.STATUS_ACTIVE)
                    .getSingleResult();

        } catch(NoResultException e) {
            return null;
        }
    }

    public CompanyEntity saveAndFlush(CompanyEntity entity) {
        if(entity.getId() != null) {
            this.entityManager.merge(entity);
        } else {
            this.entityManager.persist(entity);
        }

        this.entityManager.flush();
        return entity;
    }

}