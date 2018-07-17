package au.com.suttons.notification.data.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityGraph;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import au.com.suttons.notification.data.entity.LookupEntity;
import au.com.suttons.notification.resource.page.Specification;

@Stateless
public class LookupDao extends BaseDao<LookupEntity>
{
    public LookupDao() {
        this.criteria = new LookupCriteria();
    }

    public LookupEntity findOne(Long id) {
        return this.entityManager.find(LookupEntity.class, id);
    }

    public LookupEntity saveAndFlush(LookupEntity entity) {
        if(entity.getId() != null) {
            this.entityManager.merge(entity);
        } else {
            this.entityManager.persist(entity);
        }

        this.entityManager.flush();
        return entity;
    }

    @SuppressWarnings("unchecked")
	public List<LookupEntity> findByType(String type) {
        String sql = "SELECT l FROM LookupEntity l WHERE type = :type ORDER BY sequence";
        return this.entityManager.createQuery(sql)
                .setParameter("type", type)
                .getResultList();
    }

     public class LookupCriteria implements Criteria {
        public Specification<LookupEntity> byType(final String type)
        {
            return new Specification<LookupEntity>()
            {
                @Override
                public Predicate toPredicate(Root<LookupEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder)
                {
                    Predicate predicate = criteriaBuilder.conjunction();
                    predicate = criteriaBuilder.and(
                        predicate, criteriaBuilder.equal(root.get("type"), type)
                    );

                    return predicate;
                }

				@Override
				public EntityGraph<LookupEntity> getEntityGraph() {
					return null;
				}
            };
        }
    }
}