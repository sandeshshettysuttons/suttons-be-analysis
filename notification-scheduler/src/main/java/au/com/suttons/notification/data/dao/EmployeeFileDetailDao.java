package au.com.suttons.notification.data.dao;

import au.com.suttons.notification.data.entity.EmployeeFileDetailEntity;
import au.com.suttons.notification.data.entity.EmployeeFileEntity;

import javax.ejb.Stateless;

@Stateless
public class EmployeeFileDetailDao extends BaseDao<EmployeeFileEntity>
{

    public EmployeeFileDetailEntity saveAndFlush(EmployeeFileDetailEntity entity) {
        if(entity.getId() != null) {
            this.entityManager.merge(entity);
        } else {
            this.entityManager.persist(entity);
        }

        this.entityManager.flush();
        return entity;
    }

}