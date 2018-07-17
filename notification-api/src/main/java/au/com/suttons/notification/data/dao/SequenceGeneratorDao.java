package au.com.suttons.notification.data.dao;

import au.com.suttons.notification.data.entity.SequenceGeneratorEntity;
import javax.ejb.Stateless;
import javax.persistence.EntityGraph;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;

@Stateless
public class SequenceGeneratorDao extends BaseDao<SequenceGeneratorEntity>
{

    public SequenceGeneratorDao() { }

    public Long generateSequence(String name) {
        EntityGraph graph = entityManager.getEntityGraph("graph.sequencegenerator.details");
        StoredProcedureQuery query = entityManager
                .createStoredProcedureQuery("notification.GenerateSequenceNumber")
                .registerStoredProcedureParameter(
                        "keyName", String.class, ParameterMode.IN)
                .registerStoredProcedureParameter(
                        "sequenceNo", Long.class, ParameterMode.OUT)
                .setParameter("keyName", name)
                .setHint("javax.persistence.fetchgraph", graph);

        query.execute();

        return (Long) query
                .getOutputParameterValue("sequenceNo");
    }

}