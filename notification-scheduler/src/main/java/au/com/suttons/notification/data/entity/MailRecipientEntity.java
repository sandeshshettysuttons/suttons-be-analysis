package au.com.suttons.notification.data.entity;

import javax.persistence.*;

@Entity
@Table(name = "MAILRECIPIENT")
public class MailRecipientEntity extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@Column(name = "NAME", length=100)
	private String name;

	@Column(name = "EMAIL", nullable=false, length=100)
	private String email;

	@Column(name = "STATUS", nullable=false, length=20)
	private String status;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
