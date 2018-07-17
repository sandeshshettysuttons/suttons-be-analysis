package au.com.suttons.notification.resource.bean;

import au.com.suttons.notification.config.AppConfig;
import au.com.suttons.notification.resource.error.RestApiException;
import au.com.suttons.notification.util.StringUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.Response.Status;
import org.apache.commons.lang.StringUtils;

public class BaseResourceBean extends RootResourceBean
{
    private String departmentCode;

    private String f;
    private Map<String, String> fMap;
    private boolean isUpdating;
    private String logMessage;

    @Override
    public void setHref(String href)
    {
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

    public String getDepartmentCode() {
		return departmentCode;
	}

	public void setDepartmentCode(String departmentCode) {
		this.departmentCode = departmentCode;
        this.href = this.constructHref();
	}

    public String getF() {
        return f;
    }

    public void setF(String f) {
        this.f = f;
    }

    public boolean isUpdatable(String fieldName) {
        
        if(!this.isIsUpdating()) {
            return true;
        }

        if (this.fMap == null) {
            this.generateFMap();
        }

        return this.fMap == null || this.fMap.containsKey(fieldName);
    }

    public boolean isIsUpdating() {
        return isUpdating;
    }

    public void setIsUpdating(boolean isUpdating) {
        this.isUpdating = isUpdating;
    }

    private void generateFMap() {
        if(StringUtils.isNotBlank(this.getF())) {
            String fields[] = this.getF().split(",");
            this.fMap = new HashMap<String, String>();
            for(String field : fields) {
                this.fMap.put(field, field);
            }
        }
    }

    @Override
    protected String constructHref() {
        // If this resource has no ID, then it can have no unique HREF
        if (this.id == null) {
            return null;
        }

        // If this resource has no site id, then it can have no unique HREF
        if (this.departmentCode == null) {
            return null;
        }

        StringBuilder itemURI = new StringBuilder();

        itemURI.append(AppConfig.baseAPIURL)
                .append("/").append(this.departmentCode)
                .append("/").append(this.resourceType)
                .append("/").append(this.id);

        return itemURI.toString();
    }

    public String getHrefId()
    {
        String result = null;

        if (this.href != null) {
            String[] hrefChunks = this.href.split("/");

            if (hrefChunks.length >= 1) {
                result = hrefChunks[hrefChunks.length - 1];
            }
        }

        return result;
    }

    public static List<Long> getIds(String idsString, String msg)
    {
        List<Long> ids = new ArrayList<Long>();
        try {
            if (idsString != null) {
                List<String> idsList = Arrays.asList(idsString.split(","));

                for (String idStr : StringUtil.trimList(idsList)) {
                    if (idStr != null && idStr.length() > 0) {
                        ids.add(Long.parseLong(idStr));
                    }
                }
            }
        }
        catch (Exception e) {
            throw RestApiException.getBadRequestException(msg);
        }

        return ids;
    }

    public String getLogMessage() {
        return logMessage;
    }

    public void setLogMessage(String logMessage) {
        this.logMessage = logMessage;
    }
}
