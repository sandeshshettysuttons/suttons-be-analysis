package au.com.suttons.notification.data.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "EMAILLOGEMPLOYEE")
public class EmailLogEmployeeEntity extends BaseEntity
{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_EMAILLOGEMPLOYEE_EMAILLOG"), nullable=false)
    private EmailLogEntity emailLog;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_EMAILLOGEMPLOYEE_EMPLOYEE"), nullable=false)
    private EmployeeEntity employee;

    @Column(name = "TERMINATIONDATE")
    @Temporal(TemporalType.DATE)
    private Date terminationDate;

    public EmailLogEntity getEmailLog() {
        return emailLog;
    }

    public void setEmailLog(EmailLogEntity emailLog) {
        this.emailLog = emailLog;
    }

    public EmployeeEntity getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeEntity employee) {
        this.employee = employee;
    }

    public Date getTerminationDate() {
        return terminationDate;
    }

    public void setTerminationDate(Date terminationDate) {
        this.terminationDate = terminationDate;
    }
}
