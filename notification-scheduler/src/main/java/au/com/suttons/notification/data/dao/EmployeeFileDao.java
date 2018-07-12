package au.com.suttons.notification.data.dao;

import au.com.suttons.notification.data.entity.EmployeeFileEntity;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;

@Stateless
public class EmployeeFileDao extends BaseDao<EmployeeFileEntity>
{

    public EmployeeFileEntity findByName(String fileName) {
        String sql = "SELECT  e FROM EmployeeFileEntity e WHERE e.fileName = :fileName";

        try {

            return this.entityManager.createQuery(sql, EmployeeFileEntity.class)
                    .setParameter("fileName", fileName)
                    .setMaxResults(1)
                    .getSingleResult();

        } catch(NoResultException e) {
            return null;
        }
    }

    public EmployeeFileEntity saveAndFlush(EmployeeFileEntity entity) {
        if(entity.getId() != null) {
            this.entityManager.merge(entity);
        } else {
            this.entityManager.persist(entity);
        }

        this.entityManager.flush();
        return entity;
    }

}