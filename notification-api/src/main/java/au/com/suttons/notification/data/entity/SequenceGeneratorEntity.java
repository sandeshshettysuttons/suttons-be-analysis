package au.com.suttons.notification.data.entity;

import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "SEQUENCEGENERATOR")
@NamedEntityGraphs({
        @NamedEntityGraph(name="graph.sequencegenerator.details")
})
public class SequenceGeneratorEntity implements Serializable
{
	private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(name = "NAME", nullable=false, length=40)
    protected String name;

    @Column(name = "SEQUENCENUMBER", nullable=false)
    private Long sequenceNumber;

    @Version
    @Column(name = "VERSION")
    @NotNull @ColumnDefault("1")
    protected Long version;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(Long sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        if(this.id != null && !this.version.equals(version)) {
            throw new OptimisticLockException();
        }
        this.version = version;
    }

}
