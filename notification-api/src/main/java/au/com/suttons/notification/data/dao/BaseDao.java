package au.com.suttons.notification.data.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ejb.SessionContext;
import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;

import au.com.suttons.notification.resource.page.Page;
import au.com.suttons.notification.resource.page.PageImpl;
import au.com.suttons.notification.resource.page.Pageable;
import au.com.suttons.notification.resource.page.Sort;
import au.com.suttons.notification.resource.page.Sort.Order;
import au.com.suttons.notification.resource.page.Specification;

public class BaseDao<T> {

    @PersistenceContext(unitName = "notification")
    protected EntityManager entityManager;
    protected SessionContext sessionContext;

    protected Criteria criteria;

    public Criteria getCriteria() throws Exception {
        if(criteria == null) {	
            throw new Exception("Criteria has not been initialised. Please initailise this in the subclass");
        }
        return criteria;
    }

    public Page<T> getPage(Pageable pageable, Specification<T> specification, Class<T> entityClass) {

        CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
	        
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<T> countRoot = countQuery.from(entityClass);
        countQuery.select(criteriaBuilder.countDistinct(countRoot));
        countQuery.where(specification.toPredicate(countRoot, countQuery, criteriaBuilder));
        Long count = this.entityManager.createQuery(countQuery).getResultList().get(0);

        criteriaBuilder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(entityClass);
        Root<T> criteriaRoot = criteriaQuery.from(entityClass);
        criteriaQuery.select(criteriaRoot).distinct(true);
        criteriaQuery.where(specification.toPredicate(criteriaRoot, criteriaQuery, criteriaBuilder));

        //append order by clause
        if(pageable.getSort() != null) {
            Sort sort = pageable.getSort();
            Iterator<Order> orders = sort.iterator();
            
            List<javax.persistence.criteria.Order> criteriaOrder = new ArrayList<javax.persistence.criteria.Order>();
            while(orders.hasNext()) {
                Order order = orders.next();
                String property = order.getProperty();
                String[] columns = StringUtils.split(property,".");

                Path<T> path = criteriaRoot;
                for(String column : columns) {
                    path = path.get(column);
                }

                if(order.isAscending()) {
                    criteriaOrder.add(criteriaBuilder.asc(path));
                } else {
                    criteriaOrder.add(criteriaBuilder.desc(path));
                }
            }
            
            criteriaQuery.orderBy(criteriaOrder);
        }

        Query query = this.entityManager.createQuery(criteriaQuery);

        EntityGraph<T> entityGraph = specification.getEntityGraph();
        if(entityGraph != null) {
    		query.setHint("javax.persistence.fetchgraph", entityGraph);
        }

        List<T> content = query
                    .setFirstResult(pageable.getOffset())
                    .setMaxResults(pageable.getPageSize())
                    .getResultList();
        
        return new PageImpl<T>(content, pageable, count);
    }
}
