package au.com.suttons.notification.config;


import java.util.Properties;

import javax.annotation.Resource;
import javax.ejb.Stateless;

/***
 * Provides a way to create a {@link java.util.Properties} object using container managed properties.
 *
 * This class is called by the container and returns a {@link java.util.Properties} object when
 * asked for a JDNI resource with the following name {@code notification/properties}.
 */
@Stateless
public class PropertiesFactory
{
    @Resource(name = "java:global/notification/properties")
    private Properties properties;

    public Properties getProperties() {
        return properties;
    }
}
