package au.com.suttons.notification.mapper;

import au.com.suttons.notification.data.entity.UserAccessEntity;
import au.com.suttons.notification.data.entity.UserEntity;
import au.com.suttons.notification.resource.bean.*;
import au.com.suttons.notification.resource.page.Page;
import au.com.suttons.notification.util.EncryptionUtils;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class UserMapper extends BaseMapper
{

    public UserMapper(RequestBean requestBean) {
        super(requestBean);
    }

    public UserMapper(RequestBean requestBean, int depth) {
        super(requestBean, depth);
    }

    public UserBean toBean(UserEntity entity)
    {
        if (entity == null) { return null; }

        UserBean bean = new UserBean();

        bean.setId(entity.getId());
        bean.setVersion(entity.getVersion());
        bean.setFirstName(entity.getFirstName());
        bean.setLastName(entity.getLastName());
        bean.setName();
        bean.setEmail(entity.getEmail());
        bean.setPhoneNumber(entity.getPhoneNumber());

        bean.setUserName(entity.getUserName());
        bean.setLoginId(entity.getLoginId());

        bean.setPassword(entity.getPassword());
        bean.setNonADLoginSessionId(entity.getNonADLoginSessionId());

        bean.setStatus(entity.getStatus());
        bean.setIsSystemAdmin(entity.isIsSystemAdmin());

        if(this.isValidLevel() && CollectionUtils.isNotEmpty(entity.getUserAccess()) ) {

            List<UserAccessEntity> userAccesses =  new ArrayList<>();

            for (UserAccessEntity userAccessEntity : entity.getUserAccess()) {
                userAccesses.add(userAccessEntity);
            }

            bean.setUserAccesses(new UserAccessMapper(this.requestBean, this.getDepth()).toBeans(new ArrayList(entity.getUserAccess())));

            List<DepartmentBean> departmentList = new ArrayList<DepartmentBean>();
            List<TemplateBean> templateList = new ArrayList<TemplateBean>();

            for(UserAccessEntity userAccess : entity.getUserAccess()) {
                departmentList.add(new DepartmentMapper(this.getRequestBean(), this.getDepth()).toBean(userAccess.getDepartment()));
                templateList.add(new TemplateMapper(this.getRequestBean(), this.getDepth()).toBean(userAccess.getTemplate()));
            }
            bean.setDepartments(departmentList);
            bean.setTemplates(templateList);
        }

        bean.setDepartmentCode(this.getRequestBean().getDepartmentCode());
        return bean;
    }

    public List<UserBean> toBeans(List<UserEntity> entities)
    {
        List<UserBean> beans = new ArrayList<UserBean>();

        for (UserEntity e : entities) {
            beans.add(this.toBean(e));
        }

        return beans;
    }

    public BaseCollectionBean<UserBean> toCollectionBean(Page<UserEntity> page)
    {
        BaseCollectionBean<UserBean> baseCollectionBean = new BaseCollectionBean(page);

        List<UserBean> beans = this.toBeans(page.getContent());
        baseCollectionBean.setContent(beans);

        return baseCollectionBean;
    }

    public UserEntity toEntity(UserBean bean, UserEntity entity)
    {

        if (entity == null) {
            bean.setIsUpdating(false);
            entity = new UserEntity();
        } else {
            bean.setIsUpdating(true);
        }

        entity.setVersion(bean.getVersion());

        if(bean.isUpdatable("status")) {
            entity.setStatus(bean.getStatus());
        }
        if(bean.isUpdatable("firstName")) {
            entity.setFirstName(bean.getFirstName());
        }
        if(bean.isUpdatable("lastName")) {
            entity.setLastName(bean.getLastName());
        }
        if(bean.isUpdatable("email")) {
            entity.setEmail(bean.getEmail());
        }
        if(bean.isUpdatable("phoneNumber")) {
            entity.setPhoneNumber(bean.getPhoneNumber());
        }
        if(bean.isUpdatable("userName")) {
            entity.setUserName(bean.getUserName());
        }
        if(bean.isUpdatable("loginId")) {
            entity.setLoginId(bean.getLoginId());
        }
        if(bean.isUpdatable("password")) {
            if(bean.getPassword() != null) {
                entity.setPassword( EncryptionUtils.MD5Hex(bean.getPassword()) );
            }
        }
        if(bean.isUpdatable("isSystemAdmin")) {
            if (bean.getIsSystemAdmin() != null) {
                entity.setIsSystemAdmin(bean.getIsSystemAdmin());
            } else {
                entity.setIsSystemAdmin(false);
            }
        }

        //User accesses are cleared and add new user accesses
        if(bean.isUpdatable("userAccesses")) {
            if(bean.getUserAccesses() != null) {
                //because of JPA behaviour, existing collection should be cleared instead of being created. (refer to JPA orphanremoval)
                if(entity.getUserAccess() != null) {
                    entity.getUserAccess().clear();
                }else{
                    entity.setUserAccess(new HashSet<UserAccessEntity>() );
                }
                for(UserAccessBean userAccessBean : bean.getUserAccesses()) {
                    entity.getUserAccess().add(new UserAccessMapper(this.getRequestBean()).toEntity(userAccessBean, null));
                }

            }
        }

        entity.setLastUpdatedBy(this.getRequestBean().getUserId());

        return entity;
    }
}
