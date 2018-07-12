package au.com.suttons.notification.bean;

import java.util.List;

public class TerminationNotificationBean
{

	private String mailRecipientName;
	private String mailRecipientEmail;

	List<EmployeeGroupBean> employeeGroups;

	public String getMailRecipientName() {
		return mailRecipientName;
	}

	public void setMailRecipientName(String mailRecipientName) {
		this.mailRecipientName = mailRecipientName;
	}

	public String getMailRecipientEmail() {
		return mailRecipientEmail;
	}

	public void setMailRecipientEmail(String mailRecipientEmail) {
		this.mailRecipientEmail = mailRecipientEmail;
	}

	public List<EmployeeGroupBean> getEmployeeGroups() {
		return employeeGroups;
	}

	public void setEmployeeGroups(List<EmployeeGroupBean> employeeGroups) {
		this.employeeGroups = employeeGroups;
	}
}
