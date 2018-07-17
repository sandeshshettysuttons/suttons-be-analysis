package au.com.suttons.notification.data.entity;

import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;


@Entity
@Table(name = "USER")
@NamedEntityGraphs({
    @NamedEntityGraph(name="graph.user.details",
        attributeNodes={
         }
    ),
    @NamedEntityGraph(name="graph.user.login",
    	attributeNodes={
            @NamedAttributeNode(
                value="userAccess",
                subgraph="userAccessWithDepartments"
            )
    	},
    	subgraphs = {
    		//userAccess departments
			@NamedSubgraph(
				name="userAccessWithDepartments",
				attributeNodes = {
		            @NamedAttributeNode("department")
				}
			)
    	}
    ),
    @NamedEntityGraph(name="graph.user.template",
            attributeNodes={
                    @NamedAttributeNode(
                            value="userAccess",
                            subgraph="userAccessWithTemplates"
                    )
            },
            subgraphs = {
                    //userAccess templates
                    @NamedSubgraph(
                            name="userAccessWithTemplates",
                            attributeNodes = {
                                    @NamedAttributeNode("template")
                            }
                    )
            }
    )
})
public class UserEntity extends BaseEntity
{

	private static final long serialVersionUID = 1L;

    @Column(name = "USERNAME", length=20, unique=true)
    private String userName;

    @Column(name = "LOGINID", length=20, unique=true)
    private String loginId;

    @Column(name = "PASSWORD", length=32)
    private String password;

    @Column(name = "NONADLOGINSESSIONID", length=100)
    private String nonADLoginSessionId;

    @Column(name = "FIRSTNAME", nullable=false, length=50)
    private String firstName;

    @Column(name = "LASTNAME", nullable=false, length=50)
    private String lastName;

    @Column(name = "EMAIL", nullable=false, length=100)
    private String email;

    @Column(name = "PHONENUMBER", length=30)
    private String phoneNumber;

    @Column(name = "STATUS", nullable=false, length=20) 
    @ColumnDefault("'ACTIVE'")
    private String status;

    @Column(name = "ISSYSTEMADMIN", nullable=false, columnDefinition = "CHAR(1)")
    @ColumnDefault("0")
    private boolean isSystemAdmin;

    @Column(name = "LASTLOGINIP", length=50)
    private String lastLoginIp;

    @Column(name = "LASTLOGINTS")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastLoginTS;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade=CascadeType.ALL, orphanRemoval = true)
    private Set<UserAccessEntity> userAccess;

    public String getPrintName() {
        return (this.getFirstName() + " " + this.getLastName()).trim();
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isIsSystemAdmin() { return isSystemAdmin; }

    public void setIsSystemAdmin(boolean isSystemAdmin) {
        this.isSystemAdmin = isSystemAdmin;
    }

    public boolean isSystemAdmin() { return isSystemAdmin; }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNonADLoginSessionId() {
		return nonADLoginSessionId;
	}

	public void setNonADLoginSessionId(String nonADLoginSessionId) {
		this.nonADLoginSessionId = nonADLoginSessionId;
	}

	public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    public Date getLastLoginTS() {
        return lastLoginTS;
    }

    public void setLastLoginTS(Date lastLoginTS) {
        this.lastLoginTS = lastLoginTS;
    }
    
	public Set<UserAccessEntity> getUserAccess() { return userAccess; }

    public void setUserAccess(Set<UserAccessEntity> userAccess) {
        this.userAccess = userAccess;
    }
}
