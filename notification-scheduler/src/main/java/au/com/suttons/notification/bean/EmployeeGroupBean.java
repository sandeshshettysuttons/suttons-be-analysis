package au.com.suttons.notification.bean;


import java.util.List;

public class EmployeeGroupBean implements Comparable<EmployeeGroupBean>
{

	private int daysToTerminationRangeFrom;
	private int daysToTerminationRangeTo;
	private String description;
	private int sequence;

	private List<EmployeeBean> employees;

	public int getDaysToTerminationRangeFrom() {
		return daysToTerminationRangeFrom;
	}

	public void setDaysToTerminationRangeFrom(int daysToTerminationRangeFrom) {
		this.daysToTerminationRangeFrom = daysToTerminationRangeFrom;
	}

	public int getDaysToTerminationRangeTo() {
		return daysToTerminationRangeTo;
	}

	public void setDaysToTerminationRangeTo(int daysToTerminationRangeTo) {
		this.daysToTerminationRangeTo = daysToTerminationRangeTo;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public List<EmployeeBean> getEmployees() {
		return employees;
	}

	public void setEmployees(List<EmployeeBean> employees) {
		this.employees = employees;
	}

	@Override
	public int compareTo(EmployeeGroupBean other) {
		return this.getSequence() - other.getSequence();
	}
}
