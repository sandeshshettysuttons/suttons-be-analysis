package au.com.suttons.notification.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "LOOKUP")
public class LookupEntity extends BaseEntity
{
	private static final long serialVersionUID = 1L;

	@Column(name = "TYPE", nullable=false, length=50)
    private String type;

	@Column(name = "SEQUENCE", nullable=false)
    private Long sequence;

    @Column(name = "LABEL", nullable=false, length=100) 
    private String label;

    @Column(name = "VALUE", nullable=false, length=100) 
    private String value;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getSequence() {
		return sequence;
	}

	public void setSequence(Long sequence) {
		this.sequence = sequence;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
