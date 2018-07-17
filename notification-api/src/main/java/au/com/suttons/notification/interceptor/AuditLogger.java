package au.com.suttons.notification.interceptor;

import au.com.suttons.notification.config.AppConfig;
import au.com.suttons.notification.config.ResourceJNDI;
import au.com.suttons.notification.data.dao.AuditLogDao;
import au.com.suttons.notification.data.dao.DepartmentDao;
import au.com.suttons.notification.data.dao.UserDao;
import au.com.suttons.notification.data.entity.AuditLogEntity;
import au.com.suttons.notification.interceptor.ApiInterceptor.ApiParams;
import au.com.suttons.notification.interceptor.annotation.AuditLog;
import au.com.suttons.notification.resource.bean.*;
import au.com.suttons.notification.util.DateUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AuditLogger {

    private static final Logger logger = LoggerFactory.getLogger(AuditLogger.class);

	public static final String BEAN_PREFIX = "bean.";
	public static final String RESULT_PREFIX = "result.item.";
	public static final String HTTPREQUEST_PREFIX = "httpRequest.";

	public static final String VARIABLE_PREFIX = "{";
	public static final String VARIABLE_SUFFIX = "}";
	public static final String REPLACEMENT_PREFIX = "'";
	public static final String REPLACEMENT_SUFFIX = "'";

	public static final String VARIABLE_USER_NAME = "user.name";

	public static final String REGULAR_EXPRESSION = "\\{([^}]*)\\}";
    private static final Pattern PATTERN = Pattern.compile(REGULAR_EXPRESSION);

    private static Map<String,String> loggingTypesMap;
    static {
    	loggingTypesMap = new HashMap<String,String>();
    	if(AppConfig.auditLogTypesSave != null) {

    		if("*".equals(AppConfig.auditLogTypesSave)) {
    	    	for(AuditLog.Type type : AuditLog.Type.values()) {
        			loggingTypesMap.put(type.name(), type.name());
    	    	}
    			
    		} else {
        		String[] logTypes = AppConfig.auditLogTypesSave.split(",");
        		for(String logType : logTypes) {
        			loggingTypesMap.put(logType, logType);
        		}
    		}
    	}
    }

    public void log(AuditLog auditLogAnnotation, ApiParams params, APIResponse result, RequestBean requestBean) {

        if(auditLogAnnotation == null) {
			throw new IllegalArgumentException("missing mandatory annotation: @"+AuditLog.class.getSimpleName());
        }

        if(auditLogAnnotation != null && loggingTypesMap.containsKey(auditLogAnnotation.activityType().name())) {
        	try {

        		RootResourceBean bean = params.getBean();
	        	String log = auditLogAnnotation.template();

	        	List<String> variables = this.getVariables(log);
	        	for(String variable : variables) {
    	        	String replacement = null;

    	        	//retrieve variable from input
	        		if(variable.startsWith(BEAN_PREFIX)) {
	        			if(bean == null) {
		        			throw new IllegalArgumentException("bean resource not found");
	        			}
//	    	        	Class<?> beanClass = ResourceAlias.getClazz(bean.getResourceType());
	        			replacement = this.getProperty(bean, variable.substring(BEAN_PREFIX.length()));

    	        	//retrieve variable from result
	        		} else if(variable.startsWith(HTTPREQUEST_PREFIX)) {
	        			if(params.getHttpRequest() == null) {
		        			throw new IllegalArgumentException("http request not found");
	        			}

	        			String queryParamName = variable.substring(HTTPREQUEST_PREFIX.length());
	        			replacement = requestBean.getQueryParam(queryParamName);

	        		} else if(variable.startsWith(RESULT_PREFIX)) {
	        			if(result == null) {
		        			throw new IllegalArgumentException("result resource not found");
	        			}
	        			if(!(result instanceof ResponseInstanceBean)) {
		        			throw new IllegalArgumentException("result resource must be ResponseInstanceBean type");
	        			}
	        			RootResourceBean resultItem = ((RootResourceBean)((ResponseInstanceBean)result).getItem());
//	    	        	Class<?> beanClass = ResourceAlias.getClazz(resultItem.getResourceType());
	        			replacement = this.getProperty(resultItem, variable.substring(RESULT_PREFIX.length()));

	        		} else if(VARIABLE_USER_NAME.equals(variable)){
	        			replacement = requestBean.getDisplayName();

    	        	//variable not found
	        		} else {
	        			throw new IllegalArgumentException(variable + " not found");
	        		}

	        		log = log.replace(VARIABLE_PREFIX+variable+VARIABLE_SUFFIX, REPLACEMENT_PREFIX+replacement+REPLACEMENT_SUFFIX);
	        	}

	        	AuditLogEntity auditLogEntity = new AuditLogEntity();
	        	auditLogEntity.setActivityType(auditLogAnnotation.activityType().name());
	        	auditLogEntity.setActivityDate(DateUtil.getCurrentSqlDate());
	        	auditLogEntity.setChannel(requestBean.getChannel());
	        	auditLogEntity.setIpAddress(requestBean.getIpAddress());
	        	auditLogEntity.setDescription(log);
	        	auditLogEntity.setIsPublic(true);

	        	//log entity info from input
	        	if(bean != null) {
	        		//attach this audit log to parent level
	        		if(bean instanceof SecondaryResourceBean
	        				&& ((SecondaryResourceBean) bean).getPrimaryResourceType() != null
	        				&& ((SecondaryResourceBean) bean).getPrimaryResourceId() != null) {
			        	auditLogEntity.setActivityResource(((SecondaryResourceBean) bean).getPrimaryResourceType());
			        	auditLogEntity.setActivityResourceId(((SecondaryResourceBean) bean).getPrimaryResourceId());
	        		} else {
			        	auditLogEntity.setActivityResource(bean.getResourceType());
			        	auditLogEntity.setActivityResourceId(bean.getId());
	        		}
	        	}

	        	//log entity info from result (required also if a new entity being added) 
	        	if(auditLogEntity.getActivityResourceId() == null && result != null && result instanceof ResponseInstanceBean) {
        			RootResourceBean resultItem = ((RootResourceBean)((ResponseInstanceBean)result).getItem());
        			if(resultItem != null) {
			        	auditLogEntity.setActivityResource(resultItem.getResourceType());
			        	auditLogEntity.setActivityResourceId(resultItem.getId());
        			}
	        	}
	        	auditLogEntity.setLastUpdatedBy(requestBean.getUserId());

	        	this.save(auditLogEntity, requestBean);
        	} catch(Throwable t) {
        		t.printStackTrace();
        	}
        }
    }

    protected void save(AuditLogEntity auditLogEntity, RequestBean requestBean)
    {
		logger.info(auditLogEntity.getDescription());
        try {
            InitialContext context = new InitialContext();

            UserDao userDao = (UserDao) context.lookup(ResourceJNDI.userDao.getJndi());
            auditLogEntity.setUser(userDao.findOne(requestBean.getUserId()));

            if(requestBean.getDepartmentCode() != null) {
				DepartmentDao departmentDao = (DepartmentDao) context.lookup(ResourceJNDI.departmentDao.getJndi());
	            auditLogEntity.setDepartment(departmentDao.findByCode(requestBean.getDepartmentCode()));
            }

            AuditLogDao auditLogDao = (AuditLogDao) context.lookup(ResourceJNDI.auditLogDao.getJndi());
        	auditLogDao.saveAndFlush(auditLogEntity);
        }
        catch (NamingException nameEx) {
            logger.error("Unable to load an EJB via JNDI.  The bean will not be initialised", nameEx);
        }
    }

    protected String getProperty(Object object, String propertyName) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		if(propertyName.indexOf(".") > 0) {
			String childObjectPropertyName = StringUtils.capitalize(propertyName.substring(0, propertyName.indexOf(".")));
			Method method = object.getClass().getDeclaredMethod("get"+childObjectPropertyName);
			Object childObject = method.invoke(object);

			return this.getProperty(childObject, propertyName.substring(propertyName.indexOf(".")+1));
		}

		Method method = object.getClass().getDeclaredMethod("get"+StringUtils.capitalize(propertyName.substring(propertyName.indexOf(".")+1)));
		Object value = method.invoke(object);

		//convert date object
		if(value instanceof java.util.Date) {
			return DateUtil.formatDateToString((java.util.Date)value);
		}

		return value.toString();
    }

    //return all variables in the template
    public List<String> getVariables(String template) {
    	List<String> result = null;

        if (template != null) {
        	result = new ArrayList<String>();
            Matcher matcher = PATTERN.matcher(template);

            while(matcher.find()) {
            	result.add(matcher.group(1));
            }
        }

        return result;
    }
}
