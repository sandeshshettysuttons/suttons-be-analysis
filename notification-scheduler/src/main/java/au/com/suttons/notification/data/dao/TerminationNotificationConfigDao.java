package au.com.suttons.notification.data.dao;

import au.com.suttons.notification.data.entity.TerminationNotificationConfigEntity;

import javax.ejb.Stateless;
import java.util.List;

@Stateless
public class TerminationNotificationConfigDao extends BaseDao<TerminationNotificationConfigEntity>
{

    public List<TerminationNotificationConfigEntity> findByType(String type) {
        String sql = "SELECT tnc FROM TerminationNotificationConfigEntity tnc WHERE tnc.type = :type ORDER BY tnc.sequence";

        return this.entityManager.createQuery(sql, TerminationNotificationConfigEntity.class)
                .setParameter("type", type)
                .getResultList();
    }

}