package au.com.suttons.notification.resource.bean;

public class ResponseInstanceBean implements APIResponse
{
    APIResource item;

    RequestBean requestBean;

    public APIResource getItem() {
        return item;
    }

    public void setItem(APIResource item) {
        this.item = item;
    }

    public RequestBean getRequestBean() {
        return requestBean;
    }

    public void setRequestBean(RequestBean requestBean) {
        this.requestBean = requestBean;
    }
}
