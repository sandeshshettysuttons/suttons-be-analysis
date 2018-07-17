package au.com.suttons.notification.data.dao;

import au.com.suttons.notification.data.entity.UserAccessEntity;
import au.com.suttons.notification.model.UserAccessSearchParameters;
import au.com.suttons.notification.resource.page.Specification;

import javax.ejb.Stateless;
import javax.persistence.EntityGraph;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Stateless
public class UserAccessDao extends BaseDao<UserAccessEntity>
{
    public UserAccessDao() {
        this.criteria = new UserAccessCriteria();
    }

    public UserAccessEntity findOne(Long id) {
        return this.entityManager.find(UserAccessEntity.class, id);
    }

    public UserAccessEntity findById(Long id) {
        String sql = "SELECT DISTINCT u FROM UserAccessEntity u WHERE u.id = :id";

        try {
        	EntityGraph graph = entityManager.getEntityGraph("graph.user.login");
            return (UserAccessEntity) this.entityManager.createQuery(sql)
                    .setParameter("id", id)
	                .setHint("graph.userAccess.details", graph)
                    .getSingleResult();
        } catch(NoResultException e) {
            return null;
        }
    }

    public UserAccessEntity saveAndFlush(UserAccessEntity entity) {
        if(entity.getId() != null) {
            this.entityManager.merge(entity);
        } else {
            this.entityManager.persist(entity);
        }

        this.entityManager.flush();
        return entity;
    }

    public void delete(UserAccessEntity entity) {
        this.entityManager.remove(entity);
    }

    public class UserAccessCriteria implements Criteria {
        public Specification<UserAccessEntity> byParameters(final UserAccessSearchParameters params)
        {
            return new Specification<UserAccessEntity>()
            {
                @Override
                public Predicate toPredicate(Root<UserAccessEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder)
                {
                    Predicate predicate = criteriaBuilder.conjunction();

                    if(params.getCompanyId() != null) {
                        predicate = criteriaBuilder.and(
                            predicate, criteriaBuilder.equal(root.join("company").get("id"), params.getCompanyId())
                        );
                    } else if(params.getSiteId() != null) {
                        predicate = criteriaBuilder.and(
                            predicate, criteriaBuilder.equal(root.join("site").get("id"), params.getSiteId())
                        );
                    }

                    if(params.getUserId() != null) {
                        predicate = criteriaBuilder.and(
                            predicate, criteriaBuilder.equal(root.join("staff").get("id"), params.getUserId())
                        );
                    }
                    if(params.getTemplateId() != null) {
                        predicate = criteriaBuilder.and(
                            predicate, criteriaBuilder.equal(root.join("template").get("id"), params.getTemplateId())
                        );
                    }

                    return predicate;
                }

				@Override
				public EntityGraph<UserAccessEntity> getEntityGraph() {
					EntityGraph graph = entityManager.getEntityGraph("graph.userAccess.details");
					return graph;
				}
            };
        }
    }
}
