package au.com.suttons.notification.data.entity;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.ColumnDefault;


@Entity
@Table(name = "DEPARTMENT")
public class DepartmentEntity extends BaseEntity
{
	private static final long serialVersionUID = 1L;

	@Column(name = "NAME", nullable=false, length=200)
	private String name;

	@Column(name = "CODE", nullable=false, length=20)
	private String code;

	@Column(name = "ISACTIVE", nullable=false, columnDefinition = "CHAR(1)")
	@ColumnDefault("0")
	private boolean isActive;

	@PrePersist
	@PreUpdate
	void onPrePersistOrUpdateDepartment() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException
	{
		if(this.getCode() != null){
			this.setCode(this.getCode().toUpperCase());
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}
}
