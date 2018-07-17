package au.com.suttons.notification.service;


import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import au.com.suttons.notification.data.entity.UserEntity;
import au.com.suttons.notification.data.entity.RoleEntity;
import au.com.suttons.notification.data.entity.TemplateEntity;
import au.com.suttons.notification.mapper.RoleMapper;
import au.com.suttons.notification.model.TemplateSearchParameters;
import au.com.suttons.notification.resource.bean.BaseCollectionBean;
import au.com.suttons.notification.resource.bean.RequestBean;
import au.com.suttons.notification.resource.bean.RoleBean;
import au.com.suttons.notification.resource.page.Page;
import au.com.suttons.notification.resource.page.PageImpl;
import au.com.suttons.notification.resource.page.Pageable;
import au.com.suttons.notification.validator.RoleValidator;
import au.com.suttons.notification.validator.ValidationHelper;

@Stateless
public class RoleService extends BaseService
{
	private @EJB RoleValidator roleValidator;

    public BaseCollectionBean<RoleBean> getRolesByUser(Pageable pageable, TemplateSearchParameters searchParameters, RequestBean requestBean) throws Exception
    {
    	List<RoleEntity> roleList = null;

        UserEntity userEntity = this.userDao.findByLoginAndIsActive(requestBean.getUserName());
        if(userEntity == null){
        	return null;
        }else if(userEntity.isIsSystemAdmin()){
        	roleList = this.roleDao.findAllRoles();
        }else{
        	List<TemplateEntity> userTemplates = this.templateDao.findByDepartmentCode(requestBean.getQueryParam("displayName"), requestBean.getDepartmentCode());
            List<TemplateSearchParameters.LowerAccessLevelRange> ranges = new ArrayList<TemplateSearchParameters.LowerAccessLevelRange>();
            for(TemplateEntity userTemplate : userTemplates) {
            	TemplateSearchParameters.LowerAccessLevelRange range = new TemplateSearchParameters().new LowerAccessLevelRange(userTemplate.getLowerAccessLevelFrom(), userTemplate.getLowerAccessLevelTo());
                ranges.add(range);
            }

            //roleList = new RoleMapper(requestBean).toBeans(this.roleDao.findAllRolesByLowerAccessLevel(ranges));
            roleList = this.roleDao.findAllRolesByLowerAccessLevel(ranges);
        }

        Page<RoleEntity> page = new PageImpl<RoleEntity>(roleList);
        return new RoleMapper(requestBean).toCollectionBean(page);
        
	}
    
    public RoleBean saveRole(RoleBean roleBean, RequestBean requestBean)
    {
        RoleEntity RoleEntity = null;
        Long id = roleBean.getId();

        if (id != null) {
        	RoleEntity = this.roleDao.findOne(id);
            ValidationHelper.getInstance().validateNotFound(RoleEntity, "Role ID: " + id);
        }

        // Validate unique role name
        this.roleValidator.onSave(roleBean, roleDao, requestBean);

        RoleMapper roleMapper = new RoleMapper(requestBean);
        RoleEntity = roleMapper.toEntity(roleBean, RoleEntity);
        RoleEntity = this.roleDao.saveAndFlush(RoleEntity);

        return roleMapper.toBean(RoleEntity);
    }


}
