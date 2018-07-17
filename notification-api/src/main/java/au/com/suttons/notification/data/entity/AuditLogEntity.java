package au.com.suttons.notification.data.entity;

import org.hibernate.annotations.ColumnDefault;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.Table;


@Entity
@Table(name = "AUDITLOG",
		indexes = {@Index(name="INDEX_RESOURCE", columnList="ACTIVITYRESOURCE, ACTIVITYRESOURCEID")})
@NamedEntityGraphs({
    @NamedEntityGraph(name="graph.auditLog.user", attributeNodes={
        @NamedAttributeNode("user")
    })
})
public class AuditLogEntity extends BaseEntity
{
	private static final long serialVersionUID = 1L;

	@Column(name = "ACTIVITYTYPE", nullable=false, length=20)
    private String activityType;

	@Column(name = "ACTIVITYRESOURCE", length=50)
    private String activityResource;

	@Column(name = "ACTIVITYRESOURCEID", length=20)
    private Long activityResourceId;

	@Column(name = "ACTIVITYDATE")
    private Date activityDate;

	@Column(name = "CHANNEL", nullable=false, length=20) 
    private String channel;

    @Column(name = "IPADDRESS", nullable=false, length=50)
    private String ipAddress;

	@Column(name = "DESCRIPTION", nullable=false, length=1000) 
    private String description;

	@Column(name = "ISPUBLIC", nullable=false, columnDefinition = "CHAR(1)")
	@ColumnDefault("1")
	private boolean isPublic;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_AUDITLOG_DEPARTMENT"))
    private DepartmentEntity department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_AUDITLOG_USER"))
    private UserEntity user;

	public String getActivityType() {
		return activityType;
	}

	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}

	public String getActivityResource() {
		return activityResource;
	}

	public void setActivityResource(String activityResource) {
		this.activityResource = activityResource;
	}

	public Long getActivityResourceId() {
		return activityResourceId;
	}

	public void setActivityResourceId(Long activityResourceId) {
		this.activityResourceId = activityResourceId;
	}

	public Date getActivityDate() {
		return activityDate;
	}

	public void setActivityDate(Date activityDate) {
		this.activityDate = activityDate;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean getIsPublic() {
		return isPublic;
	}

	public void setIsPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}

	public DepartmentEntity getDepartment() {
		return department;
	}

	public void setDepartment(DepartmentEntity department) {
		this.department = department;
	}

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}
}
