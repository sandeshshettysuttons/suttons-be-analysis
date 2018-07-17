package au.com.suttons.notification.config;

public enum ResourceJNDI
{
	PropertiesFactory("java:global/"+ AppConfig.applicationContext + "/PropertiesFactory"),
    userDao("java:global/"+ AppConfig.applicationContext + "/UserDao"),
    departmentDao("java:global/"+ AppConfig.applicationContext + "/DepartmentDao"),
    depotDao("java:global/"+ AppConfig.applicationContext + "/DepotDao"),
    templateDao("java:global/"+ AppConfig.applicationContext + "/TemplateDao"),
    auditLogDao("java:global/"+ AppConfig.applicationContext + "/AuditLogDao"),
    ;

    private final String jndi;

    ResourceJNDI(String jndi)
    {
        this.jndi = jndi;
    }

    public String getJndi() {
        return jndi;
    }
}
