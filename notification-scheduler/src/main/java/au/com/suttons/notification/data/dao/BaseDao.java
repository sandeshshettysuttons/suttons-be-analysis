package au.com.suttons.notification.data.dao;

import javax.ejb.SessionContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class BaseDao<T> {

    @PersistenceContext(unitName = "application")
    protected EntityManager entityManager;
    protected SessionContext sessionContext;

}
