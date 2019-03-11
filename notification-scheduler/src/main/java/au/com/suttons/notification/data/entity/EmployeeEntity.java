package au.com.suttons.notification.data.entity;

import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "EMPLOYEE")
public class EmployeeEntity extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@Column(name = "EMPLOYEENUMBER", length=20, nullable = false)
	private String employeeNumber;

	@Column(name = "FIRSTNAME", length=50)
	private String firstName;

	@Column(name = "LASTNAME", length=50)
	private String lastName;

	@Column(name = "PREFERREDNAME", length=50)
	private String preferredName;

	@Column(name = "REPORTNAME", length=50)
	private String reportName;

	@Column(name = "DESCRIPTION", length=100)
	private String description;

	@Column(name = "POSITION", length=50)
	private String position;

	@Column(name = "TERMINATIONDATE")
	@Temporal(TemporalType.DATE)
	private Date terminationDate;

	@Column(name = "TERMINATIONREASON", length=20)
	private String terminationReason;

	@Column(name = "TERMINATIONDESCRIPTION", length=100)
	private String terminationDescription;

	@Column(name = "COMMENT", length=200)
	private String comment;

	@Column(name = "STATUS", nullable=false, length=20)
	private String status;

	@Column(name = "NOTIFICATIONSENT", columnDefinition = "CHAR(1)")
	@ColumnDefault("0")
	private boolean notificationSent;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(foreignKey = @ForeignKey(name = "FK_EMPLOYEE_COMPANY"))
	private CompanyEntity company;

	public String getEmployeeNumber() {
		return employeeNumber;
	}

	public void setEmployeeNumber(String employeeNumber) {
		this.employeeNumber = employeeNumber;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPreferredName() {
		return preferredName;
	}

	public void setPreferredName(String preferredName) {
		this.preferredName = preferredName;
	}

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public Date getTerminationDate() {
		return terminationDate;
	}

	public void setTerminationDate(Date terminationDate) {
		this.terminationDate = terminationDate;
	}

	public String getTerminationReason() {
		return terminationReason;
	}

	public void setTerminationReason(String terminationReason) {
		this.terminationReason = terminationReason;
	}

	public String getTerminationDescription() {
		return terminationDescription;
	}

	public void setTerminationDescription(String terminationDescription) {
		this.terminationDescription = terminationDescription;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public boolean isNotificationSent() {
		return notificationSent;
	}

	public void setNotificationSent(boolean notificationSent) {
		this.notificationSent = notificationSent;
	}

	public CompanyEntity getCompany() {
		return company;
	}

	public void setCompany(CompanyEntity company) {
		this.company = company;
	}
}
