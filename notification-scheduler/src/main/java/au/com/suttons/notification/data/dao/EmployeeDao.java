package au.com.suttons.notification.data.dao;

import au.com.suttons.notification.data.entity.EmployeeEntity;
import au.com.suttons.notification.jobs.JobConstants;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import java.util.List;

@Stateless
public class EmployeeDao extends BaseDao<EmployeeEntity>
{

    public EmployeeEntity getReference(Long id) {
        return this.entityManager.getReference(EmployeeEntity.class, id);
    }

    public EmployeeEntity findByEmployeeNumber(String employeeNumber) {
        String sql = "SELECT e FROM EmployeeEntity e WHERE e.employeeNumber = :employeeNumber AND e.status = :activeStatus";

        try {

            return this.entityManager.createQuery(sql, EmployeeEntity.class)
                    .setParameter("employeeNumber", employeeNumber)
                    .setParameter("activeStatus", JobConstants.STATUS_ACTIVE)
                    .setMaxResults(1)
                    .getSingleResult();

        } catch(NoResultException e) {
            return null;
        }
    }

    public List<EmployeeEntity> findAllActive() {
        String sql = "SELECT  e FROM EmployeeEntity e WHERE e.status = :activeStatus AND e.terminationDate IS NOT NULL ORDER BY e.terminationDate";

        try {

            return this.entityManager.createQuery(sql, EmployeeEntity.class)
                    .setParameter("activeStatus", JobConstants.STATUS_ACTIVE)
                    .getResultList();

        } catch(NoResultException e) {
            return null;
        }
    }

    public EmployeeEntity saveAndFlush(EmployeeEntity entity) {
        if(entity.getId() != null) {
            this.entityManager.merge(entity);
        } else {
            this.entityManager.persist(entity);
        }

        this.entityManager.flush();
        return entity;
    }

}