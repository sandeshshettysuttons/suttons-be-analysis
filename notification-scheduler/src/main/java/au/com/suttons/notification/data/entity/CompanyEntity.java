package au.com.suttons.notification.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name = "COMPANY")
public class CompanyEntity extends BaseEntity
{
	private static final long serialVersionUID = 1L;

	@Column(name = "CODE", length=50)
	private String code;

	@Column(name = "NAME", nullable=false, length=200)
	private String name;

	@Column(name = "STATUS", nullable=false, length=20)
	private String status;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
