package au.com.suttons.notification.interceptor.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface AuditLog {
	String template();
	AuditLog.Type activityType();
	boolean isTest() default false;

    public static enum Type {
    	LOGIN,LOGOUT,
    	ADD,UPDATE,DISABLE,ENABLE,DELETE,
    	EMAIL,
    	LIST,VIEW,SEARCH,LOOKUP,
    	REPORT,
    	ATTACHMENT,DOWNLOAD,UPLOAD,
    	REJECT,APPROVE,CANCEL,
    	AUDIT
    }
}
