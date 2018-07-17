/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package au.com.suttons.notification.model;

public class Constants {

	public static final Long SYSTEM_USER_ID = 1L;

	public static final String BASEAPI_NOTIFICATION = "NOTIFICATION_API";
	public static final String BASEAPI_STAR = "STAR_API";

   /*
     * Resource statuses
     * All resources have ACTIVE & DISABLED status. Some of them have additional statuses
     */
    public static final String RESOURCE_STATUS_ACTIVE = "ACTIVE";
    public static final String RESOURCE_STATUS_DISABLED= "DISABLED";

	public static final String USER_STATUS_ACTIVE = RESOURCE_STATUS_ACTIVE;
	public static final String USER_STATUS_DISABLED = RESOURCE_STATUS_DISABLED;

}
