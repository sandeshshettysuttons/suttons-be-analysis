package au.com.suttons.notification.data.entity;

import javax.persistence.*;

@Entity
@Table(name = "USERACCESS")
@NamedEntityGraphs({
	@NamedEntityGraph(name="graph.userAccess.details", 
		attributeNodes={
	        @NamedAttributeNode("user"),
	        @NamedAttributeNode("department"),
	        @NamedAttributeNode("template")
        }
	)
})
public class UserAccessEntity extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_USERACCESS_USER"))
    private UserEntity user;

	//department role
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_USERACCESS_DEPARTMENT"))
	private DepartmentEntity department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_USERACCESS_TEMPLATE"))
    private TemplateEntity template;

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	public DepartmentEntity getDepartment() {
		return department;
	}

	public void setDepartment(DepartmentEntity department) {
		this.department = department;
	}

	public TemplateEntity getTemplate() {
		return template;
	}

	public void setTemplate(TemplateEntity template) {
		this.template = template;
	}

}
