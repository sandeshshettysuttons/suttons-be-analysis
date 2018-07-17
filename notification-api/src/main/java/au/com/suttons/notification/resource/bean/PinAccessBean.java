package au.com.suttons.notification.resource.bean;

public class PinAccessBean extends BaseResourceBean
{
    private String trainingId;
    private String pin;
    private String pinLoginSessionId;

    public PinAccessBean()
    {
        this.setResourceType("pinAccess");
    }

	public String getTrainingId() {
		return trainingId;
	}

	public void setTrainingId(String trainingId) {
		this.trainingId = trainingId;
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	public String getPinLoginSessionId() {
		return pinLoginSessionId;
	}

	public void setPinLoginSessionId(String pinLoginSessionId) {
		this.pinLoginSessionId = pinLoginSessionId;
	}
}
