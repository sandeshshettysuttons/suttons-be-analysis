package au.com.suttons.notification.service;


import javax.ejb.EJB;
import javax.ejb.Stateless;

import au.com.suttons.notification.interceptor.annotation.AuditLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.com.suttons.notification.data.dao.DepartmentDao.DepartmentCriteria;
import au.com.suttons.notification.data.entity.DepartmentEntity;
import au.com.suttons.notification.mapper.DepartmentMapper;
import au.com.suttons.notification.model.DepartmentSearchParameters;
import au.com.suttons.notification.model.Constants;
import au.com.suttons.notification.resource.bean.BaseCollectionBean;
import au.com.suttons.notification.resource.bean.DepartmentBean;
import au.com.suttons.notification.resource.bean.RequestBean;
import au.com.suttons.notification.resource.page.Page;
import au.com.suttons.notification.resource.page.Pageable;
import au.com.suttons.notification.resource.page.Specification;
import au.com.suttons.notification.validator.DepartmentValidator;
import au.com.suttons.notification.validator.ValidationHelper;

import java.util.List;

@Stateless
public class DepartmentService extends BaseService
{
    protected @EJB DepartmentValidator departmentValidator;

    private static final Logger logger = LoggerFactory.getLogger(DepartmentService.class);

    public List<DepartmentBean> getAllDepartments(RequestBean requestBean){
        return new DepartmentMapper(requestBean).toBeans(this.departmentDao.getAllDepartments());
    }

    public BaseCollectionBean<DepartmentBean> getDepartments(Pageable pageable, DepartmentSearchParameters searchParameters, RequestBean requestBean) throws Exception
    {
        Specification<DepartmentEntity> spec = ((DepartmentCriteria)this.departmentDao.getCriteria()).byParameters(searchParameters);
        Page<DepartmentEntity> page = this.departmentDao.getPage(pageable, spec, DepartmentEntity.class);

        return new DepartmentMapper(requestBean).toCollectionBean(page);
    }

    public DepartmentBean getDepartment(long id, RequestBean requestBean)
    {
        DepartmentEntity departmentEntity = this.departmentDao.findOne(id);
        ValidationHelper.getInstance().validateNotFound(departmentEntity, "Department ID: " + id);

        return new DepartmentMapper(requestBean).toBean(departmentEntity);
    }

    public DepartmentBean saveDepartment(DepartmentBean departmentBean, RequestBean requestBean) {

    	//validator returns entity if validation succeeded
    	DepartmentEntity departmentEntity =
        		this.departmentValidator.onSave(departmentBean, requestBean);

    	departmentEntity = new DepartmentMapper(requestBean).toEntity(departmentBean, departmentEntity);

        departmentEntity = this.departmentDao.saveAndFlush(departmentEntity);
        return new DepartmentMapper(requestBean).toBean(departmentEntity);
    }
}
