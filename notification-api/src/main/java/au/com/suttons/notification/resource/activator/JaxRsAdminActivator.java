/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package au.com.suttons.notification.resource.activator;

import au.com.suttons.notification.resource.*;
import javax.ws.rs.core.Application;
import java.util.Set;

/**
 *
 * @author nirmala.batuwitage
 */
@javax.ws.rs.ApplicationPath("/admin-api")
public class JaxRsAdminActivator extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<Class<?>>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Add all resources used by ADMIN into addRestResourceClasses() method.
     * Note: these resources will not require authentication and authorisation.
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(AuditLogResource.class);
        resources.add(DepartmentResource.class);
        resources.add(LookupResource.class);
        resources.add(MyProfileResource.class);
        resources.add(SequenceGeneratorResource.class);
        resources.add(TemplateResource.class);
        resources.add(UserResource.class);

        resources.add(CompanyResource.class);
        resources.add(MailRecipientResource.class);
    }
}
