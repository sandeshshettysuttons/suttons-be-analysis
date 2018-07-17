package au.com.suttons.notification.data.entity;

import java.lang.reflect.InvocationTargetException;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;


@Entity
@Table(name = "ROLE")
public class RoleEntity extends BaseEntity
{
	private static final long serialVersionUID = 1L;

	@Column(name = "NAME", nullable=false, length=50)
    private String name;

    @Column(name = "DESCRIPTION", length=200)
    private String description;

    @PrePersist
    @PreUpdate
    void onPrePersistOrUpdateRole() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException
    {
        if(this.getName() != null){
            this.setName(this.getName().toUpperCase());
        }
    }

	public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
