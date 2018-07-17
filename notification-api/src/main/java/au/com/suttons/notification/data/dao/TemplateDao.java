package au.com.suttons.notification.data.dao;

import au.com.suttons.notification.data.entity.DepartmentEntity;
import au.com.suttons.notification.data.entity.UserEntity;
import au.com.suttons.notification.data.entity.TemplateEntity;
import au.com.suttons.notification.data.entity.UserAccessEntity;
import au.com.suttons.notification.model.TemplateSearchParameters;
import au.com.suttons.notification.resource.page.Specification;

import javax.ejb.Stateless;
import javax.persistence.EntityGraph;
import javax.persistence.NoResultException;
import javax.persistence.criteria.*;
import java.util.List;

@Stateless
public class TemplateDao extends BaseDao<TemplateEntity>
{
    public TemplateDao() {
        this.criteria = new TemplateDao.TemplateCriteria();
    }

    public TemplateEntity findOne(Long id) {
        return this.entityManager.find(TemplateEntity.class, id);
    }

    public TemplateEntity getReference(Long id) {
    	return this.entityManager.getReference(TemplateEntity.class, id);
    }

    public TemplateEntity findById(Long id) {
        String sql = "SELECT t FROM TemplateEntity t WHERE t.id = :id";
        try {
            return (TemplateEntity) this.entityManager.createQuery(sql)
                    .setParameter("id", id)
                    .setHint("javax.persistence.fetchgraph", entityManager.getEntityGraph("graph.template.roles"))
                    .getSingleResult();
        } catch(NoResultException e) {
            return null;
        }
    }

    public TemplateEntity findByName(String name) {
        String sql = "SELECT t FROM TemplateEntity t WHERE t.name = :name";
        try {
            return (TemplateEntity) this.entityManager.createQuery(sql)
                    .setParameter("name", name)
                    .setHint("javax.persistence.fetchgraph", entityManager.getEntityGraph("graph.template.roles"))
                    .getSingleResult();
        } catch(NoResultException e) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
	public List<TemplateEntity> findByNameExcludingId(String name, long excludedId) {
        String sql = "SELECT t FROM TemplateEntity t WHERE t.name = :name AND t.id <> :excludedId";
        return this.entityManager.createQuery(sql)
                .setParameter("name", name.toUpperCase())
                .setParameter("excludedId", excludedId)
                .getResultList();
    }

    //retrieve by depot and department role
    public List<TemplateEntity> findByDepartmentCode(String userName, String departmentCode) {
        String sql = "SELECT DISTINCT t FROM TemplateEntity t "
        		+ " INNER JOIN t.userAccess ua "
                + " INNER JOIN ua.user s "
                + " INNER JOIN ua.department d "
        		+ " WHERE s.userName = :userName AND d.code = :code";
        
        return this.entityManager.createQuery(sql, TemplateEntity.class)
                .setParameter("userName", userName)
                .setParameter("code", departmentCode)
                .setHint("javax.persistence.fetchgraph", entityManager.getEntityGraph("graph.template.roles"))
                .getResultList();
    }

    public List<TemplateEntity> findByAccessLevel(String userName, Long departmentId, Long accessLevel) {
        String sql = "SELECT DISTINCT t FROM TemplateEntity t INNER JOIN t.userAccess ua INNER JOIN ua.user s INNER JOIN ua.department d"
                + "    WHERE s.userName = :userName AND d.id = :departmentId AND t.lowerAccessLevelFrom <= :accessLevel AND t.lowerAccessLevelTo >= :accessLevel";

        return this.entityManager.createQuery(sql, TemplateEntity.class)
                .setParameter("userName", userName)
                .setParameter("departmentId", departmentId)
                .setParameter("accessLevel", accessLevel)
                .getResultList();
    }

    public TemplateEntity saveAndFlush(TemplateEntity entity) {
        if(entity.getId() != null) {
            this.entityManager.merge(entity);
        } else {
            this.entityManager.persist(entity);
        }

        this.entityManager.flush();
        return entity;
    }

    public class TemplateCriteria implements Criteria {
        public Specification<TemplateEntity> byParameters(final TemplateSearchParameters params)
        {
            return new Specification<TemplateEntity>()
            {
                @Override
                public Predicate toPredicate(Root<TemplateEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder)
                {
                    Predicate predicate = criteriaBuilder.conjunction();

                    Join<TemplateEntity, UserAccessEntity> userAccessPath = root.join("userAccess");
                    if(params.getDepartmentCode() != null) {
                        Path<DepartmentEntity> departmentPath = userAccessPath.join("department");
                        predicate = criteriaBuilder.and(
                                predicate, criteriaBuilder.equal(departmentPath.get("code"), params.getDepartmentCode())
                    		);
                    }

                    if(params.getUserName() != null || params.getUserId() != null) {
                        Path<UserEntity> userPath = userAccessPath.join("user");
                        if(params.getUserName() != null) {
                            predicate = criteriaBuilder.and(
                                predicate, criteriaBuilder.equal(userPath.get("userName"), params.getUserName())
                            );
                        }
                        if(params.getUserId() != null) {
                            predicate = criteriaBuilder.and(
                                predicate, criteriaBuilder.equal(userPath.get("id"), params.getUserId())
                            );
                        }
                    }

                    return predicate;
                }

				@Override
				public EntityGraph<TemplateEntity> getEntityGraph() {
					EntityGraph graph = entityManager.getEntityGraph("graph.template.roles");
					return graph;
				}
            };
        }

        public Specification<TemplateEntity> byLowerAccessLevel(final List<TemplateSearchParameters.LowerAccessLevelRange> ranges)
        {
            return new Specification<TemplateEntity>()
            {
                @Override
                public Predicate toPredicate(Root<TemplateEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder)
                {
                    Predicate predicate = criteriaBuilder.conjunction();

                    boolean isFirst = true;
                    for(TemplateSearchParameters.LowerAccessLevelRange range : ranges) {
                        if(isFirst) {
                            isFirst = false;
                            predicate = criteriaBuilder.between(root.<Long>get("accessLevel"), range.getLowerAccessLevelFrom() , range.getLowerAccessLevelTo());

                        } else {
                            predicate = criteriaBuilder.or(
                                predicate, criteriaBuilder.between(root.<Long>get("accessLevel"), range.getLowerAccessLevelFrom() , range.getLowerAccessLevelTo())
                            );
                        }
                    }

                    return predicate;
                }

				@Override
				public EntityGraph<TemplateEntity> getEntityGraph() {
					return null;
				}
            };
        }
    }
}
