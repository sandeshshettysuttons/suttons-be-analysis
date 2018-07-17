package au.com.suttons.notification.data.dao;

import au.com.suttons.notification.data.entity.CompanyEntity;
import au.com.suttons.notification.model.CompanySearchParameters;
import au.com.suttons.notification.resource.page.Specification;

import javax.ejb.Stateless;
import javax.persistence.EntityGraph;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Stateless
public class CompanyDao extends BaseDao<CompanyEntity>
{

    public CompanyDao() {
        this.criteria = new CompanyCriteria();
    }

    public CompanyEntity getReference(Long id) {
        return this.entityManager.getReference(CompanyEntity.class, id);
    }

    public CompanyEntity findOne(Long id) {
        return this.entityManager.find(CompanyEntity.class, id);
    }

    public CompanyEntity saveAndFlush(CompanyEntity entity) {
        if(entity.getId() != null) {
            this.entityManager.merge(entity);
        } else {
            this.entityManager.persist(entity);
        }

        this.entityManager.flush();
        return entity;
    }

    public class CompanyCriteria implements Criteria {
        public Specification<CompanyEntity> byParameters(final CompanySearchParameters params)
        {
            return new Specification<CompanyEntity>()
            {
                @Override
                public Predicate toPredicate(Root<CompanyEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder)
                {
                    Predicate predicate = criteriaBuilder.conjunction();

                    if(params.getName() != null) {
                        predicate = criteriaBuilder.and(
                                predicate, criteriaBuilder.like(
                                        criteriaBuilder.lower(root.get("name")), "%"+params.getName().toLowerCase()+"%")
                        );
                    }
                    if(params.getStatus() != null) {
                        predicate = criteriaBuilder.and(
                                predicate, criteriaBuilder.equal(root.get("status"), params.getStatus())
                        );
                    }

                    return predicate;
                }

                @Override
                public EntityGraph<CompanyEntity> getEntityGraph() {
                    return null;
                }
            };
        }
    }

}