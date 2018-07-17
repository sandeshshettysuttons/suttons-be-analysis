/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package au.com.suttons.notification.resource.activator;

import java.util.Set;

import javax.ws.rs.core.Application;

@javax.ws.rs.ApplicationPath("/pin-api")
public class JaxRsPinActivator extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<Class<?>>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Add all resources used by RPG into addRestResourceClasses() method.
     * Note: these resources will not require authentication and authorisation.
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(au.com.suttons.notification.resource.PinAccessResource.class);
    }
}
