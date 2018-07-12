package au.com.suttons.notification.data.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "EMPLOYEEFILE")
public class EmployeeFileEntity extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    @Column(name = "FILEURL", nullable=false)
    private String fileUrl;

    @Column(name = "FILENAME", nullable=false, length=50)
    private String fileName;

    @Column(name = "STATUS", nullable=false, length=20)
    private String status;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "employeeFile")
    private List<EmployeeFileDetailEntity> employeeFileDetails;

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<EmployeeFileDetailEntity> getEmployeeFileDetails() {
        return employeeFileDetails;
    }

    public void setEmployeeFileDetails(List<EmployeeFileDetailEntity> employeeFileDetails) {
        this.employeeFileDetails = employeeFileDetails;
    }
}
