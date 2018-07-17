package au.com.suttons.notification.resource.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response.Status;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import au.com.suttons.notification.resource.error.RestApiException;
import au.com.suttons.notification.resource.page.Sort;
import au.com.suttons.notification.security.BaseRequestWrapper;
import au.com.suttons.notification.util.DonUtil;

public class RequestBean {
    
    private final int DEFAULT_LIMIT = 10;
    private final int MINIMUM_LIMIT = 1;
    private final int MAXIMUM_LIMIT = 1000;
    
    private String q;

    private Map<String, String> qMap;

    private String expand;

    private String order;

    private String donExcludedResourceFields;

    private Integer status;

    private String departmentCode;

    private String userName;

    private Long userId;

    private String displayName;

    private boolean systemAdmin;

    private Integer page;

    private Integer limit;

    private Integer cacheDuration;

    private String channel;

    private String ipAddress;

    public RequestBean()
    {
        this.cacheDuration = 0;
        this.systemAdmin = false;
    }

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }

    public Map<String, String> getQMap() {
        return DonUtil.parse(this.q);
    }

    public String getQueryParam(String key) {
        if (this.qMap == null) {
            this.qMap = this.getQMap();
        }

        String param = this.qMap.get(key);

        return param != null ? param.trim() : null;
    }

    public void setQMap(Map<String, String> qMap) {
        this.qMap = qMap;
    }

    public String getExpand() {
        return expand;
    }

    public void setExpand(String expand) {
        this.expand = expand;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public Sort getSort() throws Exception {

        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        if(StringUtils.isNotBlank(this.getOrder())) {

            if(this.getOrder().charAt(0) != '('
             || this.getOrder().charAt(this.getOrder().length()-1) != ')') {
                throw new Exception("Request parameter 'order' is wrong format: "+this.getOrder());
            }
            String orderString = this.getOrder().substring(1, this.getOrder().length()-1);

            String[] fields = orderString.split(",");

            for(String field : fields) {
                String[] tokens = field.trim().split(" ");
                if(tokens.length > 2) {
                    throw new Exception("Request parameter 'order' is wrong format: "+this.getOrder());
                }

                if(tokens.length == 1) {
                    orders.add(new Sort.Order(Sort.Direction.ASC, tokens[0]));
                    continue;
                }

                if(tokens[1].equalsIgnoreCase("ASC")) {
                    orders.add(new Sort.Order(Sort.Direction.ASC, tokens[0]));
                } else if(tokens[1].equalsIgnoreCase("DESC")) {
                    orders.add(new Sort.Order(Sort.Direction.DESC, tokens[0]));
                } else {
                    throw new Exception("Request parameter 'order' is wrong format: "+this.getOrder());
                }
            }

            return !CollectionUtils.isEmpty(orders) ? new Sort(orders) : null;
        }

        return null;
    }

    public Map<String, String> getExpandResourceFields() {
        return DonUtil.parse(this.expand);
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

	public String getUserName() {
        return userName;
    }

    public boolean isSystemAdmin() {
        return systemAdmin;
    }

    public void setSystemAdmin(boolean systemAdmin) {
        this.systemAdmin = systemAdmin;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getDonExcludedResourceFields()
    {
        return donExcludedResourceFields;
    }

    public void setDonExcludedResourceFields(String donExcludedResourceFields)
    {
        this.donExcludedResourceFields = donExcludedResourceFields;
    }

    public Map<String, String> getExcludedResourceFields()
    {
        return DonUtil.parse(this.donExcludedResourceFields);
    }

    public Integer getPage() {

        int defaultPage = 0;

        if (page == null) {
            return defaultPage;
        }

        if (page <= 0) {
            throw new RestApiException(Status.BAD_REQUEST.getStatusCode(), "",
                    "The page index must not be less equal zero!",
                    String.format("The page index must not be less equal zero!. Page: %s", page));
        }

        return page - 1;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getLimit() {

        //Set limit default value
        if (this.limit == null) {
            return DEFAULT_LIMIT;
        }

        if (limit < MINIMUM_LIMIT || limit > MAXIMUM_LIMIT) {
            throw new RestApiException(Status.BAD_REQUEST.getStatusCode(), "",
                    String.format("Page limit should between %s to %s", MINIMUM_LIMIT, MAXIMUM_LIMIT),
                    String.format("Page limit should between %s to %s. Limit: %s", MINIMUM_LIMIT, MAXIMUM_LIMIT, limit));
        }

        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;

    }

    /**
     * Get browser cache expiration in minutes
     * @return
     */
    public Integer getCacheDuration() {
        return cacheDuration;
    }

    /**
     * Set browser cache expiration in minutes
     * @param cacheDuration
     */
    public void setCacheDuration(Integer cacheDuration) {
        this.cacheDuration = cacheDuration;
    }

    public void setMandatoryExpand(String mandatoryExpand)
    {
        String expand = this.getExpand();
        expand = (expand == null) ? mandatoryExpand : expand + '+' + mandatoryExpand;
        this.setExpand(expand);
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public boolean isWebRequest() {
        return BaseRequestWrapper.CHANNEL_WEB.equals(this.getChannel());
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }
}
