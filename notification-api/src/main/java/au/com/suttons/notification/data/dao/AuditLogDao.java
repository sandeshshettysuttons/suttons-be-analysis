package au.com.suttons.notification.data.dao;

import javax.ejb.Stateless;
import javax.persistence.EntityGraph;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import au.com.suttons.notification.data.entity.AuditLogEntity;
import au.com.suttons.notification.model.AuditLogSearchParameters;
import au.com.suttons.notification.resource.page.Specification;

@Stateless
public class AuditLogDao extends BaseDao<AuditLogEntity>
{
    public AuditLogDao() {
        this.criteria = new AuditLogCriteria();
    }

    public AuditLogEntity findOne(Long id) {
        return this.entityManager.find(AuditLogEntity.class, id);
    }

    public AuditLogEntity saveAndFlush(AuditLogEntity entity) {
        if(entity.getId() != null) {
            this.entityManager.merge(entity);
        } else {
            this.entityManager.persist(entity);
        }

        this.entityManager.flush();
        return entity;
    }

    public void delete(AuditLogEntity entity) {
        this.entityManager.remove(entity);
    }

    public class AuditLogCriteria implements Criteria {
        public Specification<AuditLogEntity> byParameters(final AuditLogSearchParameters params)
        {
            return new Specification<AuditLogEntity>()
            {
                @Override
                public Predicate toPredicate(Root<AuditLogEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder)
                {
                    Predicate predicate = criteriaBuilder.conjunction();

                    if (params.getUser() != null && !params.getUser().isSystemAdmin()){
                        predicate = criteriaBuilder.and(
                                predicate, criteriaBuilder.equal(root.get("isPublic"), true)
                        );
                    }
                    if(params.getActivityTypes() != null) {
                        predicate = criteriaBuilder.and(
                            	predicate, root.get("activityType").in(params.getActivityTypes())
                        );
                    }
                    if(params.getActivityResource() != null) {
                        predicate = criteriaBuilder.and(
                            predicate, criteriaBuilder.equal(root.get("activityResource"), params.getActivityResource())
                        );
                    }
                    if(params.getUserId() != null) {
                        predicate = criteriaBuilder.and(
                            predicate, criteriaBuilder.equal(root.join("user").get("id"), params.getUserId())
                        );
                    }
                    if(params.getActivityResourceId() != null) {
                        predicate = criteriaBuilder.and(
                            predicate, criteriaBuilder.equal(root.get("activityResourceId"), params.getActivityResourceId())
                        );
                    }
                    if(params.getChannel() != null) {
                        predicate = criteriaBuilder.and(
                            predicate, criteriaBuilder.equal(root.get("channel"), params.getChannel())
                        );
                    }
                    if(params.getActivityDateFrom() != null && params.getActivityDateTo() != null) {
	                	predicate = criteriaBuilder.and(
	                		predicate, criteriaBuilder.between(root.get("activityDate"), params.getActivityDateFrom(), params.getActivityDateTo())
	                	);
	                }

                    return predicate;
                }

				@Override
				public EntityGraph<AuditLogEntity> getEntityGraph() {
					EntityGraph graph = entityManager.getEntityGraph("graph.auditLog.user");
                    return graph;
				}
            };
        }
    }
}