package au.com.suttons.notification.data.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "VERSIONHISTORY")
public class VersionHistoryEntity {

	@Id
    @Column(name = "VERSION", nullable=false, length=50)
    private String version;

    @Column(name = "CREATIONTS", nullable=false)
    @NotNull @Temporal(TemporalType.TIMESTAMP)
    protected Date creationTS;

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Date getCreationTS() {
		return creationTS;
	}

	public void setCreationTS(Date creationTS) {
		this.creationTS = creationTS;
	}
}
