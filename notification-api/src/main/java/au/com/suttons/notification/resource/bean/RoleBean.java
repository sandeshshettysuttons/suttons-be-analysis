package au.com.suttons.notification.resource.bean;

public class RoleBean extends RootResourceBean
{
    private String name;
    private String description;

    public RoleBean()
    {
        this.setResourceType("roles");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
