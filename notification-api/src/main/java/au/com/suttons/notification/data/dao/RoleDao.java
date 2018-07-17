package au.com.suttons.notification.data.dao;

import au.com.suttons.notification.data.entity.RoleEntity;
import au.com.suttons.notification.data.entity.TemplateEntity;
import au.com.suttons.notification.model.TemplateSearchParameters;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Stateless
public class RoleDao extends BaseDao<TemplateEntity>
{

    public RoleEntity findOne(Long id) {
        return this.entityManager.find(RoleEntity.class, id);
    }

    public RoleEntity findById(Long id) {
        String sql = "SELECT r FROM RoleEntity r WHERE r.id = :id";
        try {
            return (RoleEntity) this.entityManager.createQuery(sql)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch(NoResultException e) {
            return null;
        }
    }
    
    @SuppressWarnings("unchecked")
	public List<RoleEntity> findByNameExcludingId(String name, long excludedId) {
        String sql = "SELECT r FROM RoleEntity r WHERE r.name = :name AND r.id <> :excludedId";
        return this.entityManager.createQuery(sql)
                .setParameter("name", name.toUpperCase())
                .setParameter("excludedId", excludedId)
                .getResultList();
    }

    public List<RoleEntity> findAllRoles() {
    	StringBuilder sql = new StringBuilder("SELECT DISTINCT r FROM RoleEntity r ");
			sql.append(" ORDER BY r.name ");
        
        return this.entityManager.createQuery(sql.toString(), RoleEntity.class)
                .getResultList();
    }

    public List<RoleEntity> findAllRolesByUser(Long userId, Long departmentId) {
    	StringBuilder sql = new StringBuilder("SELECT DISTINCT r FROM TemplateEntity t INNER JOIN t.roles r ");
			sql.append(" INNER JOIN t.userAccess ua INNER JOIN ua.user s INNER JOIN ua.department d ");
			sql.append(" WHERE s.id = :userId AND d.id = :departmentId ");
			sql.append(" ORDER BY r.name ");

        return this.entityManager.createQuery(sql.toString(), RoleEntity.class)
                .setParameter("userId", userId)
                .setParameter("departmentId", departmentId)
                .getResultList();
    }
    
    @SuppressWarnings("unchecked")
	public List<RoleEntity> findAllRolesByLowerAccessLevel(List<TemplateSearchParameters.LowerAccessLevelRange> ranges) {

        Map<String,Object> paramMap = new HashMap<String, Object>();
    	StringBuilder sql = new StringBuilder("SELECT DISTINCT r FROM TemplateEntity t INNER JOIN t.roles r ");

		boolean isFirst = true;
		int paramCount = 0;
		
        for(TemplateSearchParameters.LowerAccessLevelRange range : ranges) {
            if(isFirst) {
                isFirst = false;
    			sql.append(" WHERE t.accessLevel between :param").append(++paramCount);
                paramMap.put("param"+paramCount, range.getLowerAccessLevelFrom());

                sql.append(" AND :param").append(++paramCount);
                paramMap.put("param"+paramCount, range.getLowerAccessLevelTo());

            } else {
    			sql.append(" OR t.accessLevel between :param").append(++paramCount);
                paramMap.put("param"+paramCount, range.getLowerAccessLevelFrom());

                sql.append(" AND :param").append(++paramCount);
                paramMap.put("param"+paramCount, range.getLowerAccessLevelTo());
            }
        }
		sql.append(" ORDER BY r.name ");

        Query query = this.entityManager.createQuery(sql.toString(), RoleEntity.class);
        
        for(String key : paramMap.keySet()){
            query.setParameter(key, paramMap.get(key));
        }
        
        return query.getResultList();
    }

    public RoleEntity saveAndFlush(RoleEntity entity) {
        if(entity.getId() != null) {
            this.entityManager.merge(entity);
        } else {
            this.entityManager.persist(entity);
        }

        this.entityManager.flush();
        return entity;
    }

}
