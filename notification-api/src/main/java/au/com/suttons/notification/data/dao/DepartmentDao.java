package au.com.suttons.notification.data.dao;

import au.com.suttons.notification.data.entity.DepartmentEntity;
import au.com.suttons.notification.model.DepartmentSearchParameters;
import au.com.suttons.notification.resource.page.Specification;

import javax.ejb.Stateless;
import javax.persistence.EntityGraph;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Stateless
public class DepartmentDao extends BaseDao<DepartmentEntity>
{
    public DepartmentDao() {
        this.criteria = new DepartmentCriteria();
    }

    public DepartmentEntity getReference(Long id) {
    	return this.entityManager.getReference(DepartmentEntity.class, id);
    }

    public DepartmentEntity findOne(Long id) {
        return this.entityManager.find(DepartmentEntity.class, id);
    }

    public DepartmentEntity findById(Long id) {
        String sql = "SELECT d FROM DepartmentEntity d WHERE d.id = :id";

        try {
            return (DepartmentEntity) this.entityManager.createQuery(sql)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch(NoResultException e) {
            return null;
        }
    }

    public List<DepartmentEntity> getAllDepartments(){
        String sql = "SELECT d FROM DepartmentEntity d";
        return (List<DepartmentEntity>) this.entityManager.createQuery(sql)
                .getResultList();
    }

    public DepartmentEntity findByCode(String code) {
        String sql = "SELECT d FROM DepartmentEntity d WHERE d.code = :code";

        try {
            return (DepartmentEntity) this.entityManager.createQuery(sql)
                    .setParameter("code", code)
                    .getSingleResult();
        } catch(NoResultException e) {
            return null;
        }
    }


    public DepartmentEntity saveAndFlush(DepartmentEntity entity) {
        if(entity.getId() != null) {
            this.entityManager.merge(entity);
        } else {
            this.entityManager.persist(entity);
        }

        this.entityManager.flush();
        return entity;
    }

    @SuppressWarnings("unchecked")
	public List<DepartmentEntity> findByCodeExcludingId(String code, long excludedId) {
        String sql = "SELECT d FROM DepartmentEntity d WHERE d.code = :code AND d.id <> :excludedId";
        return this.entityManager.createQuery(sql)
                .setParameter("code", code.toUpperCase())
                .setParameter("excludedId", excludedId)
                .getResultList();
    }

    public void delete(DepartmentEntity entity) {
        this.entityManager.remove(entity);
    }

    public class DepartmentCriteria implements Criteria {
        public Specification<DepartmentEntity> byParameters(final DepartmentSearchParameters params)
        {
            return new Specification<DepartmentEntity>()
            {
                @Override
                public Predicate toPredicate(Root<DepartmentEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder)
                {
                    Predicate predicate = criteriaBuilder.conjunction();

                    if(params.getCode() != null) {
                        predicate = criteriaBuilder.and(
                                predicate, criteriaBuilder.like(root.<String>get("code"), "%"+params.getCode().toUpperCase()+"%")
                        );
                    }
                    if(params.getName() != null) {
                        predicate = criteriaBuilder.and(
                                predicate, criteriaBuilder.like(
                                        criteriaBuilder.lower(root.get("name")), "%"+params.getName().toLowerCase()+"%")
                        );
                    }
                    if(params.isActive() != null) {
                        predicate = criteriaBuilder.and(
                                predicate, criteriaBuilder.equal(root.get("isActive"), params.isActive())
                        );
                    }

                    if(params.getTypeahead() != null) {
                        predicate = criteriaBuilder.and(
                                predicate, criteriaBuilder.or(
                                        criteriaBuilder.like(root.get("code"), "%"+params.getTypeahead().toUpperCase()+"%"),
                                        criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%"+params.getTypeahead().toLowerCase()+"%")
                                )
                        );
                    }

                    return predicate;
                }

				@Override
				public EntityGraph<DepartmentEntity> getEntityGraph() {
                    return null;
				}
            };
        }
    }
}