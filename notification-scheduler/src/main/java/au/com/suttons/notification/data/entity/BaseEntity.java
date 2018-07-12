package au.com.suttons.notification.data.entity;

import au.com.suttons.notification.data.listener.TrimListener;
import au.com.suttons.notification.util.DateUtil;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@MappedSuperclass
@EntityListeners({TrimListener.class})
@Cacheable(false)
@JsonIgnoreProperties(value = {"id","version","creationTS","createdBy","lastUpdateTS","lastUpdatedBy"})
public class BaseEntity implements Serializable
{
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Version
    @Column(name = "VERSION")
    @NotNull @ColumnDefault("1")
    protected Long version;

    @Column(name = "CREATIONTS")
    @NotNull @Temporal(TemporalType.TIMESTAMP)
    protected Date creationTS;

    @Column(name = "CREATEDBY")
    @NotNull 
    private Long createdBy;

    @Column(name = "LASTUPDATETS")
    @NotNull 
    @Temporal(TemporalType.TIMESTAMP)
    protected Date lastUpdateTS;

	@Column(name = "LASTUPDATEDBY")
	@NotNull 
	private Long lastUpdatedBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Date getCreationTS() {
        return creationTS;
    }

    public void setCreationTS(Date creationTS) {
        this.creationTS = creationTS;
    }
    
    public Long getCreatedBy() {
		return createdBy;
	}
    
    public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

    public Date getLastUpdateTS() {
        return lastUpdateTS;
    }

    public void setLastUpdateTS(Date lastUpdateTS) {
        this.lastUpdateTS = lastUpdateTS;
    }
    
    public Long getLastUpdatedBy() {
		return lastUpdatedBy;
	}
    
    public void setLastUpdatedBy(Long lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

    public static List<Long> getIds(List<? extends BaseEntity> entities) {
        List<Long> ids = new ArrayList<Long>();
        for(BaseEntity entity : entities){
            ids.add(entity.getId());
        }
        return ids;
    }

    @PrePersist
    @PreUpdate
    void onPrePersistOrUpdate() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException
    {
        Date date = DateUtil.getCurrentDate();

        if (this.getCreationTS() == null) {
            this.setCreationTS(date);
            this.setCreatedBy(this.getLastUpdatedBy());
        }

        this.setLastUpdateTS(date);
    }
}
