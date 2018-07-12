package au.com.suttons.notification.data.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "EMAILLOG")
public class EmailLogEntity extends BaseEntity
{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_EMAILLOG_MAILRECIPIENT"), nullable=false)
    private MailRecipientEntity mailRecipient;

    @Column(name = "STATUS", nullable=false, length=20)
    private String status;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "emailLog", cascade=CascadeType.ALL, orphanRemoval = true)
    private List<EmailLogEmployeeEntity> emailLogEmployees;

    public MailRecipientEntity getMailRecipient() {
        return mailRecipient;
    }

    public void setMailRecipient(MailRecipientEntity mailRecipient) {
        this.mailRecipient = mailRecipient;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<EmailLogEmployeeEntity> getEmailLogEmployees() {
        return emailLogEmployees;
    }

    public void setEmailLogEmployees(List<EmailLogEmployeeEntity> emailLogEmployees) {
        this.emailLogEmployees = emailLogEmployees;
    }
}
