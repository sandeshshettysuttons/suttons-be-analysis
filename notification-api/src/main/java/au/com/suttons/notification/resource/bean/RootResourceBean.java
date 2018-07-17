package au.com.suttons.notification.resource.bean;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Digits;
import javax.ws.rs.core.Response.Status;

import au.com.suttons.notification.config.AppConfig;
import au.com.suttons.notification.resource.error.RestApiException;

public class RootResourceBean implements APIResource
{

    @Digits(integer = 9, fraction = 0)
    protected Long id;

    protected String resourceType;

    protected String trailingResourceType;

    protected String href;
    
    public String getHref()
    {
        return href;
    }

    public void setHref(String href) {
        this.href = href;

        // If the resource's ID is null but its HREF inst, then attempt to parse the ID from the HREF
        if (id == null && this.href != null && this.href.split("\\?")[0] != null) {
            String[] hrefChunks = this.href.split("\\?")[0].split("/");
            String idAsString = null;

            try {
                idAsString = hrefChunks[hrefChunks.length - 1];

                this.id = Long.parseLong(idAsString);
            }
            catch (ArrayIndexOutOfBoundsException e) {
                throw new RestApiException(Status.BAD_REQUEST.getStatusCode(),
                        null,
                        String.format("HREF has of an invalid format. received: %s", href),
                        null
                );
            }
            catch (NumberFormatException e) {
                throw new RestApiException(Status.BAD_REQUEST.getStatusCode(),
                        null,
                        String.format("Resource ID must be of Long type, received: %s, href: %s", idAsString, href),
                        null
                );
            }
        }
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
        this.href = this.constructHref();
    }

	public String getResourceType()
    {
        return resourceType;
    }

    protected void setResourceType(String resourceType)
    {
        this.resourceType = resourceType;
        this.href = this.constructHref();
    }

    /**
     * Gets the trailing resource type, this string will be appended to the end of this resource's href
     *
     * @return - str
     */
    public String getTrailingResourceType()
    {
        return trailingResourceType;
    }

    /**
     * Sets the trailing resource type, this string will be appended to the end of this resource's href
     *
     * @param trailingResourceType - str
     */
    public void setTrailingResourceType(String trailingResourceType)
    {
        this.trailingResourceType = trailingResourceType;
    }

    protected String constructHref()
    {
        // If this resource has no ID, then it can have no unique HREF
        if (this.id == null) {
            return null;
        }

        StringBuilder itemURI = new StringBuilder();

        itemURI.append(AppConfig.baseAPIURL)
                .append("/").append(this.resourceType);

        if(this.id != null && this.trailingResourceType == null){
            itemURI.append("/").append(this.id);
        }

        if (this.trailingResourceType != null) {
            itemURI.append("/").append(this.trailingResourceType);
        }

        return itemURI.toString();
    }

    public static List<Long> getIds(List<? extends RootResourceBean> entities) {
        List<Long> ids = new ArrayList<Long>();
        for(RootResourceBean entity : entities){
            ids.add(entity.getId());
        }
        return ids;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof RootResourceBean)) {
            return false;
        }

        Long myId = this.getId();
        Long theirId = ((RootResourceBean) obj).getId();

        return (myId != null && theirId != null) && myId.equals(theirId);
    }
}
