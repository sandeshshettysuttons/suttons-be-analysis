package au.com.suttons.notification.resource.bean;

public class SecondaryResourceBean extends BaseResourceBean {

	public String primaryResourceType;
	public Long primaryResourceId;

	public String getPrimaryResourceType() {
		return primaryResourceType;
	}
	public void setPrimaryResourceType(String primaryResourceType) {
		this.primaryResourceType = primaryResourceType;
	}
	public Long getPrimaryResourceId() {
		return primaryResourceId;
	}
	public void setPrimaryResourceId(Long primaryResourceId) {
		this.primaryResourceId = primaryResourceId;
	}
}
