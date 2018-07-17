package au.com.suttons.notification.config;

public enum ResourceJNDI
{
	PropertiesFactory("java:global/"+ AppConfig.applicationContext + "/PropertiesFactory"),
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
