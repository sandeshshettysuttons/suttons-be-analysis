package au.com.suttons.notification.data.dao;

import au.com.suttons.notification.data.entity.CompanyEntity;
import au.com.suttons.notification.data.entity.MailRecipientEntity;
import au.com.suttons.notification.model.Constants;
import au.com.suttons.notification.model.MailRecipientSearchParameters;
import au.com.suttons.notification.resource.page.Specification;

import javax.ejb.Stateless;
import javax.persistence.EntityGraph;
import javax.persistence.NoResultException;
import javax.persistence.criteria.*;
import java.util.List;

@Stateless
public class MailRecipientDao extends BaseDao<MailRecipientEntity>
{
    public MailRecipientDao() {
        this.criteria = new MailRecipientCriteria();
    }

    public MailRecipientEntity getReference(Long id) {
    	return this.entityManager.getReference(MailRecipientEntity.class, id);
    }

    public MailRecipientEntity findOne(Long id) {
        return this.entityManager.find(MailRecipientEntity.class, id);
    }

    public MailRecipientEntity saveAndFlush(MailRecipientEntity entity) {
        if(entity.getId() != null) {
            this.entityManager.merge(entity);
        } else {
            this.entityManager.persist(entity);
        }

        this.entityManager.flush();
        return entity;
    }

    public void delete(MailRecipientEntity entity) {
        this.entityManager.remove(entity);
    }

    public class MailRecipientCriteria implements Criteria {
        public Specification<MailRecipientEntity> byParameters(final MailRecipientSearchParameters params)
        {
            return new Specification<MailRecipientEntity>()
            {
                @Override
                public Predicate toPredicate(Root<MailRecipientEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder)
                {
                    Predicate predicate = criteriaBuilder.conjunction();

                    if(params.getName() != null) {
                        predicate = criteriaBuilder.and(
                                predicate, criteriaBuilder.like(
                                        criteriaBuilder.lower(root.<String>get("name")), "%"+params.getName().toLowerCase()+"%")
                        );
                    }

                    if(params.getType() != null) {
                        predicate = criteriaBuilder.and(
                                predicate, criteriaBuilder.like(
                                        criteriaBuilder.lower(root.<String>get("type")), "%"+params.getType().toLowerCase()+"%")
                        );
                    }

                    if(params.getStatus() != null) {
                        predicate = criteriaBuilder.and(
                            predicate, criteriaBuilder.equal(root.get("status"), params.getStatus())
                        );
                    }

                    if (params.getCompanyId() != null){
                        Join<MailRecipientEntity, CompanyEntity> companyJoin = root.join("company");
                        predicate = criteriaBuilder.and(
                                predicate, criteriaBuilder.equal(companyJoin.get("id"), params.getCompanyId()));
                    }

                    return predicate;
                }

				@Override
				public EntityGraph<MailRecipientEntity> getEntityGraph() {
					return null;
				}
            };
        }
    }
}