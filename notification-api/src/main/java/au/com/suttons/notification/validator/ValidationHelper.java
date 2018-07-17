package au.com.suttons.notification.validator;

import au.com.suttons.notification.data.entity.BaseEntity;
import au.com.suttons.notification.model.ErrorCode;
import au.com.suttons.notification.resource.bean.RequestBean;
import au.com.suttons.notification.resource.error.RestApiException;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ValidationHelper {

    protected List<RestApiException> errors;

    public static ValidationHelper getInstance(){
        return new ValidationHelper();
    }

    public List<RestApiException> getErrors() {
        return errors;
    }

    //Add a RestApiException to errors collection
    public void addError(RestApiException error) {

        if (errors == null) {
            errors = new ArrayList<RestApiException>();
        }

        errors.add(error);
    }

    //Custom validator for checking not found
    public void validateNotFound(Object entity, String message){
        if(entity == null){
            throw RestApiException.getNotFoundException(message);
        }
        
        if(entity instanceof Collection && CollectionUtils.isEmpty((Collection)entity)) {
            throw RestApiException.getNotFoundException(message);
        }
    }

    //Custom validator for checking access to the department in returned entity
    public void validateAccessToDepartment(String departmentCode, RequestBean requestBean){
        if(requestBean.isSystemAdmin() || requestBean.getDepartmentCode() == null) {
            return;
        }

        if(departmentCode.equals(requestBean.getDepartmentCode())){
            return;
        }

        throw RestApiException.getAccessRuleException(ErrorCode.DT_B_003, new Object[]{}, new Object[]{requestBean.getUserName()});
    }

    //Custom validator for checking access to the site in returned entity
    public void validateAccessToSite(Long siteId, RequestBean requestBean){
        if(requestBean.isSystemAdmin() || requestBean.getDepartmentCode() == null) {
            return;
        }

        if(siteId.equals(requestBean.getDepartmentCode())){
            return;
        }
    
        throw RestApiException.getAccessRuleException(ErrorCode.DT_B_003, new Object[]{}, new Object[]{requestBean.getUserName()});
    }

    //Custom validator for checking access to the site in returned entity
    public void validateEntityAgainstSite(Long siteId, RequestBean requestBean){
        if(siteId.equals(requestBean.getDepartmentCode())){
            return;
        }
    
        throw RestApiException.getAccessRuleException(ErrorCode.DT_B_005, new Object[]{}, new Object[]{requestBean.getUserName(), requestBean.getDepartmentCode(), siteId});
    }

    //Custom validator for version
    public void validateVersion(BaseEntity entity, Long version){
        
        if(entity == null || entity.getVersion() == null || version == null 
                || entity.getVersion().longValue() != version.longValue()){
            throw RestApiException.getVersionMismatchException();
        }
        
    }

    //Custom validator for checking access to the site in returned entity
    public void validateAdminAccess(RequestBean requestBean){
        if(requestBean.isSystemAdmin()) {
            return;
        }

        throw RestApiException.getAccessRuleException(ErrorCode.DT_B_006, new Object[]{}, new Object[]{requestBean.getUserName()});
    }
}
