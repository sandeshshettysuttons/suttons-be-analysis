package au.com.suttons.notification.resource.bean;

public class ResponseDeleteBean implements APIResponse
{
    String message;

    public ResponseDeleteBean()
    {
        this.message = "The resource has been successfully deleted.";
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
