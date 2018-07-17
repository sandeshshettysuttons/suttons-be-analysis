package au.com.suttons.notification.data.entity;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;


@Entity
@Table(name = "TEMPLATE")
@NamedEntityGraphs({
    @NamedEntityGraph(name="graph.template.roles", attributeNodes={
        @NamedAttributeNode("roles")
    })
})
public class TemplateEntity extends BaseEntity
{
	private static final long serialVersionUID = 1L;

    @ManyToMany(fetch=FetchType.LAZY)
    @JoinTable(name="ROLEACCESS", 
        joinColumns=@JoinColumn(name="TEMPLATE_ID", foreignKey=@ForeignKey(name="FK_ROLEACCESS_TEMPLATE")),
        inverseJoinColumns=@JoinColumn(name="ROLE_ID", foreignKey=@ForeignKey(name="FK_ROLEACCESS_ROLE"))
    )
    private List<RoleEntity> roles;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "template")
    private List<UserAccessEntity> userAccess;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "ACCESS_LEVEL")
    private Long accessLevel;

    @Column(name = "LOWER_ACCESS_LEVEL_FROM")
    private Long lowerAccessLevelFrom;

    @Column(name = "LOWER_ACCESS_LEVEL_TO")
    private Long lowerAccessLevelTo;

    @PrePersist
    @PreUpdate
    void onPrePersistOrUpdateTemplate() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException
    {
        if(this.getName() != null){
            this.setName(this.getName().toUpperCase());
        }
    }

    public List<RoleEntity> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleEntity> roles) {
        this.roles = roles;
    }

    public List<UserAccessEntity> getUserAccess() {
        return userAccess;
    }

    public void setUserAccess(List<UserAccessEntity> userAccess) {
        this.userAccess = userAccess;
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

    public Long getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(Long accessLevel) {
        this.accessLevel = accessLevel;
    }

    public Long getLowerAccessLevelFrom() {
        return lowerAccessLevelFrom;
    }

    public void setLowerAccessLevelFrom(Long lowerAccessLevelFrom) {
        this.lowerAccessLevelFrom = lowerAccessLevelFrom;
    }

    public Long getLowerAccessLevelTo() {
        return lowerAccessLevelTo;
    }

    public void setLowerAccessLevelTo(Long lowerAccessLevelTo) {
        this.lowerAccessLevelTo = lowerAccessLevelTo;
    }
}
