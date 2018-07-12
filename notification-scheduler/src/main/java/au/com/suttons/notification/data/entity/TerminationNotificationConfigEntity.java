package au.com.suttons.notification.data.entity;

import javax.persistence.*;

@Entity
@Table(name = "TERMINATIONNOTIFICATIONCONFIG")
public class TerminationNotificationConfigEntity extends BaseEntity
{

    @Column(name = "TYPE", length=20, nullable=false)
    private String type;

    @Column(name = "DAYSTOTERMINATIONFROM", nullable=false)
    private Long daysToTerminationFrom;

    @Column(name = "DAYSTOTERMINATIONTO", nullable=false)
    private Long daysToTerminationTo;

    @Column(name = "DESCRIPTION", length=100, nullable=false)
    private String description;

    @Column(name = "SEQUENCE", nullable=false)
    private Long sequence;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getDaysToTerminationFrom() {
        return daysToTerminationFrom;
    }

    public void setDaysToTerminationFrom(Long daysToTerminationFrom) {
        this.daysToTerminationFrom = daysToTerminationFrom;
    }

    public Long getDaysToTerminationTo() {
        return daysToTerminationTo;
    }

    public void setDaysToTerminationTo(Long daysToTerminationTo) {
        this.daysToTerminationTo = daysToTerminationTo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getSequence() {
        return sequence;
    }

    public void setSequence(Long sequence) {
        this.sequence = sequence;
    }
}
