package au.com.suttons.notification.resource.bean;

public class SequenceGeneratorBean extends BaseResourceBean
{
    private Long version;

    private String name;
    private Long sequenceNumber;

    public SequenceGeneratorBean()
    {
        this.setResourceType("sequenceNumber");
    }

    public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(Long sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }
}
