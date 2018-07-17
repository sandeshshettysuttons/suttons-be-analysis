package au.com.suttons.notification.resource;

public class BaseResource
{
    /**
     * Defines a DON-formatted string containing fields that are to be excluded from the resource when returned to the client. Fields specified in this
     * list can never be included by any other means (even if the same field is specified in the 'expand' list).
     *
     * This should be implemented by any subclass that requires this functionality
     *
     * @return
     */
    public String getExcludedFields()
    {
        return null;
    }
}
