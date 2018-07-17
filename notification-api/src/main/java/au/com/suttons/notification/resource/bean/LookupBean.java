package au.com.suttons.notification.resource.bean;

public class LookupBean extends BaseResourceBean
{
    private Long version;
    private String type;
    private Long sequence;
    private String label;
    private String value;

    public LookupBean()
    {
        this.setResourceType("lookup");
    }

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Long getSequence() {
		return sequence;
	}
	public void setSequence(Long sequence) {
		this.sequence = sequence;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
