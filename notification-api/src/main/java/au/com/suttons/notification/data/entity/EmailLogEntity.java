package au.com.suttons.notification.data.entity;

import javax.persistence.*;

@Entity
@Table(name = "EMAILLOG")
public class EmailLogEntity extends BaseEntity
{

    @Column(name = "SOURCEID", nullable=false)
    private Long sourceId;

    @Column(name = "SOURCETYPE", nullable=false)
    private String sourceType;
    
    @Column(name = "STATUS")
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_EMAILLOG_DEPARTMENT"), nullable=false)
    private DepartmentEntity department;

    public Long getSourceId() {
        return sourceId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public DepartmentEntity getDepartment() {
        return department;
    }

    public void setDepartment(DepartmentEntity department) {
        this.department = department;
    }
}
