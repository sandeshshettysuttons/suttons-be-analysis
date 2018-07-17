package au.com.suttons.notification.mapper;

import au.com.suttons.notification.resource.bean.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public enum ResourceAlias
{
    auditLog(au.com.suttons.notification.resource.bean.AuditLogBean.class),
    department(DepartmentBean.class),
    lookup(au.com.suttons.notification.resource.bean.LookupBean.class),
    role(au.com.suttons.notification.resource.bean.RoleBean.class),
    sequenceGenerator(SequenceGeneratorBean.class),
    user(UserBean.class),
    template(au.com.suttons.notification.resource.bean.TemplateBean.class),
    userAccess(au.com.suttons.notification.resource.bean.UserAccessBean.class),
    company(CompanyBean.class),
    mailRecipient(MailRecipientBean.class),
    ResponseCollectionBean(au.com.suttons.notification.resource.bean.ResponseCollectionBean.class),
    ResponseInstanceBean(au.com.suttons.notification.resource.bean.ResponseInstanceBean.class),
    ResponseDeleteBean(au.com.suttons.notification.resource.bean.ResponseDeleteBean.class),
    ResponseErrorBean(au.com.suttons.notification.resource.bean.ResponseErrorBean.class),
    ;

    private final static Logger logger = LoggerFactory.getLogger(ResourceAlias.class);
    public final Class clazz;
    public final String resourceType;

    ResourceAlias(Class clazz)
    {
        this(clazz, null);
    }

    ResourceAlias(Class clazz, String resourceType)
    {
        this.clazz = clazz;
        this.resourceType = resourceType;
    }

    public static String getClassName(String friendlyName)
    {
        String result = null;
        if (friendlyName.equals("*")) {
            return friendlyName;
        }
        else
        {
            try
            {
                result = ResourceAlias.valueOf(friendlyName).clazz.getSimpleName();
            }
            catch (Exception ex)
            {
                logger.warn(String.format("An attempt was made to look up a ResourceAlias type using the friendly name: '%s', " +
                        "however this friendly name is not defined as a ResourceAlias type", friendlyName), ex);
            }

            return result;
        }
    }

    /**
     * A simple check to see if the friendlyName exists as an enum
     *
     * @param friendlyName - Friendly name to see if exists
     *
     * @return - bool
     */
    public static boolean doesAliasExist(String friendlyName)
    {
        try
        {
            return ResourceAlias.valueOf(friendlyName) != null;
        }
        catch (Exception ex)
        {
            return false;
        }
    }

    public static Class getClazz(String friendlyName)
    {
        if (friendlyName.equals("*")) {
            return null;
        }
        else {
            return ResourceAlias.valueOf(friendlyName).clazz;
        }
    }

    public static ResourceAlias fromClass(Class queryClazz)
    {
        for (ResourceAlias alias : ResourceAlias.values()) {
            if (alias.clazz.equals(queryClazz)) {
                return alias;
            }
        }

        return null;
    }

    /**
     * Get ResourceAlias by class name.
     * e.g. "RepairOrderBean" => ResourceAlias.repairOrder
     * @param simpleClassName
     * @return
     */
    public static ResourceAlias fromSimpleClass(String simpleClassName)
    {
        for (ResourceAlias alias : ResourceAlias.values())
        {
            if (alias.clazz.getSimpleName().equals(simpleClassName)) {
                return alias;
            }
        }

        return null;
    }

    /**
     * Get ResourceAlias by resource name.
     * e.g. "repairOrders" => ResourceAlias.repairOrder
     * @param resourceType
     * @return
     */
    public static ResourceAlias fromResourceType(String resourceType)
    {
        for (ResourceAlias alias : ResourceAlias.values()) {
            if (alias.resourceType != null && alias.resourceType.equals(resourceType)) {
                return alias;
            }
        }

        return null;
    }
}
