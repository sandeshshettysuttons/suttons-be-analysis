package au.com.suttons.notification.data.dao;

import au.com.suttons.notification.data.entity.UserEntity;
import au.com.suttons.notification.data.entity.UserAccessEntity;
import au.com.suttons.notification.model.Constants;
import au.com.suttons.notification.model.UserSearchParameters;
import au.com.suttons.notification.resource.page.Specification;

import javax.ejb.Stateless;
import javax.persistence.EntityGraph;
import javax.persistence.NoResultException;
import javax.persistence.criteria.*;
import java.util.List;

@Stateless
public class UserDao extends BaseDao<UserEntity>
{
    public UserDao() {
        this.criteria = new UserCriteria();
    }

    public UserEntity getReference(Long id) {
    	return this.entityManager.getReference(UserEntity.class, id);
    }

    //to retrieve user information to allow resource access
    public UserEntity findByLoginAndIsActive(String userName) {
        String sql = "SELECT DISTINCT e FROM UserEntity e WHERE e.userName = :userName AND e.status = :activeStatus";

        try {
            EntityGraph graph = entityManager.getEntityGraph("graph.user.login");
            return (UserEntity) this.entityManager.createQuery(sql, UserEntity.class)
                    .setParameter("userName", userName)
                    .setParameter("activeStatus", "ACTIVE")
                    .setHint("javax.persistence.fetchgraph", graph)
                    .getSingleResult();
        } catch(NoResultException e) {
            return null;
        }
    }

    //to retrieve user information to login (pin)
    public UserEntity findByLoginIdAndPassword(String loginId, String password) {
        String sql = "SELECT DISTINCT e FROM UserEntity e WHERE e.loginId = :loginId AND e.password = :password AND e.status = :activeStatus";

        try {
            return (UserEntity) this.entityManager.createQuery(sql, UserEntity.class)
                    .setParameter("loginId", loginId)
                    .setParameter("password", password)
                    .setParameter("activeStatus", "ACTIVE")
                    .getSingleResult();
        } catch(NoResultException e) {
            return null;
        }
    }

    //to retrieve user information to allow resource access (for pin login)
    public UserEntity findByPinLoginSessionIdAndIsActive(String pinLoginSessionId) {
        String sql = "SELECT DISTINCT e FROM UserEntity e WHERE e.pinLoginSessionId = :pinLoginSessionId AND e.status = :activeStatus";

        try {
            return (UserEntity) this.entityManager.createQuery(sql, UserEntity.class)
                    .setParameter("pinLoginSessionId", pinLoginSessionId)
                    .setParameter("activeStatus", "ACTIVE")
                    .getSingleResult();
        } catch(NoResultException e) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
	public List<UserEntity> findByUserIdExcludingId(String userId, long excludedId) {
        String sql = "SELECT DISTINCT e FROM UserEntity e WHERE LOWER(e.userName) = LOWER(:userId) AND e.id <> :excludedId";
        return this.entityManager.createQuery(sql)
                .setParameter("userId", userId)
                .setParameter("excludedId", excludedId)
                .getResultList();
    }

    @SuppressWarnings("unchecked")
	public List<UserEntity> findByLoginIdExcludingId(String loginId, long excludedId) {
        String sql = "SELECT DISTINCT e FROM UserEntity e WHERE LOWER(e.loginId) = LOWER(:loginId) AND e.id <> :excludedId";
        return this.entityManager.createQuery(sql)
                .setParameter("loginId", loginId)
                .setParameter("excludedId", excludedId)
                .getResultList();
    }

    @SuppressWarnings("unchecked")
	public List<UserEntity> findByLoginId(String loginId) {
        String sql = "SELECT DISTINCT e FROM UserEntity e WHERE LOWER(e.loginId) = LOWER(:loginId)";
        return this.entityManager.createQuery(sql)
                .setParameter("loginId", loginId)
                .getResultList();
    }

    @SuppressWarnings("unchecked")
	public List<UserEntity> findByDepartment(Long departmentId) {
        String sql = "SELECT DISTINCT e FROM UserEntity e INNER JOIN e.department c WHERE c.id = :departmentId AND e.status = :activeStatus";
        return this.entityManager.createQuery(sql)
                .setParameter("departmentId", departmentId)
                .setParameter("activeStatus", Constants.USER_STATUS_ACTIVE)
                .getResultList();
    }

    public UserEntity findByDepartmentCodeAndId(String departmentCode, long userId) {
        String sql = "SELECT DISTINCT u FROM UserEntity u INNER JOIN u.userAccess ua WHERE ua.department.code = :departmentCode AND u.id = :userId";

        try {
            EntityGraph graph = entityManager.getEntityGraph("graph.user.template");
            return (UserEntity) this.entityManager.createQuery(sql)
                    .setParameter("departmentCode", departmentCode)
                    .setParameter("userId", userId)
                    .setHint("javax.persistence.fetchgraph", graph)
                    .getSingleResult();
        } catch(NoResultException e) {
            return null;
        }
    }

    public UserEntity findById(Long id) {
        String sql = "SELECT DISTINCT e FROM UserEntity e WHERE e.id = :id";

        try {
            EntityGraph graph = entityManager.getEntityGraph("graph.user.details");
            return (UserEntity) this.entityManager.createQuery(sql)
                    .setParameter("id", id)
	                .setHint("javax.persistence.fetchgraph", graph)
                    .getSingleResult();
        } catch(NoResultException e) {
            return null;
        }
    }

	public int removeSessionIdExcludingId(String sessionId, long excludedId) {
        String sql = "UPDATE UserEntity SET pinLoginSessionId = NULL WHERE pinLoginSessionId = :sessionId AND id <> :excludedId";
        return this.entityManager.createQuery(sql)
                .setParameter("sessionId", sessionId)
                .setParameter("excludedId", excludedId)
                .executeUpdate();
    }

    public UserEntity findOne(Long id) {
        return this.entityManager.find(UserEntity.class, id);
    }

    public UserEntity saveAndFlush(UserEntity entity) {
        if(entity.getId() != null) {
            this.entityManager.merge(entity);
        } else {
            this.entityManager.persist(entity);
        }

        this.entityManager.flush();
        return entity;
    }

    public void delete(UserEntity entity) {
        this.entityManager.remove(entity);
    }

    public class UserCriteria implements Criteria {
        public Specification<UserEntity> byParameters(final UserSearchParameters params)
        {
            return new Specification<UserEntity>()
            {
                @Override
                public Predicate toPredicate(Root<UserEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder)
                {
                    Predicate predicate = criteriaBuilder.conjunction();
                    Join<UserEntity, UserAccessEntity> userAccessJoin = root.join("userAccess", JoinType.LEFT);

//                    if(params.getDepartmentCode() != null) {
//                        predicate = criteriaBuilder.and(
//                            predicate, criteriaBuilder.equal(userAccessJoin.get("department").get("code"), params.getDepartmentCode())
//                        );
//                    }

                    if(params.isIsSystemAdmin() != null) {
                        predicate = criteriaBuilder.and(
                            predicate, criteriaBuilder.equal(root.get("isSystemAdmin"), params.isIsSystemAdmin())
                        );
                    }
                    if(params.getStatus() != null) {
                        predicate = criteriaBuilder.and(
                            predicate, criteriaBuilder.equal(root.get("status"), params.getStatus())
                        );
                    }
                    if(params.getFirstName() != null) {
                        predicate = criteriaBuilder.and(
                            predicate, criteriaBuilder.like(
                                criteriaBuilder.lower(root.<String>get("firstName")), "%"+params.getFirstName().toLowerCase()+"%")
                        );
                    }
                    if(params.getLastName() != null) {
                        predicate = criteriaBuilder.and(
                            predicate, criteriaBuilder.like(
                                criteriaBuilder.lower(root.<String>get("lastName")), "%"+params.getLastName().toLowerCase()+"%")
                        );
                    }
                    if(params.getName() != null) {
                        predicate = criteriaBuilder.and(
                            predicate, criteriaBuilder.like(
                                criteriaBuilder.lower(
                                    criteriaBuilder.concat(
                                        root.<String>get("firstName") , root.<String>get("lastName"))
                                ), "%"+params.getName().toLowerCase().replace(" ", "")+"%"
                            )
                        );
                    }
                    if(params.getLowerAccessLevelRange() != null) {
                        predicate = criteriaBuilder.and(
                                predicate, criteriaBuilder.between(userAccessJoin.get("template").<Long>get("accessLevel"), params.getLowerAccessLevelRange().getLowerAccessLevelFrom(), params.getLowerAccessLevelRange().getLowerAccessLevelTo())
                        );
                    }
                    if(params.getTypeahead() != null) {
                        predicate = criteriaBuilder.and(
                                predicate, criteriaBuilder.like(
                                        criteriaBuilder.lower(
                                                criteriaBuilder.concat(
                                                        root.<String>get("firstName") , root.<String>get("lastName"))
                                        ), "%"+params.getTypeahead().toLowerCase().replace(" ", "")+"%"
                                )
                        );
                    }
                    if(params.isIsActive() != null) {

                        final String activeStatus = "ACTIVE";

                        if (params.isIsActive()) {
                            predicate = criteriaBuilder.and(
                                    predicate, criteriaBuilder.equal(
                                            criteriaBuilder.lower(root.get("status")), activeStatus.toLowerCase() )
                            );
                        } else {
                            predicate = criteriaBuilder.and(
                                    predicate, criteriaBuilder.notEqual(root.get("status"), activeStatus.toLowerCase())
                            );
                        }
                    }

                    return predicate;
                }

				@Override
				public EntityGraph<UserEntity> getEntityGraph() {
					EntityGraph graph = entityManager.getEntityGraph("graph.user.details");
					return graph;
				}
            };
        }
    }
}