package au.com.suttons.notification.resource.error;

import javax.ejb.ApplicationException;
import java.util.ArrayList;
import java.util.List;

@ApplicationException(rollback = true)
public class ApiExceptionWrapper extends RuntimeException{

    Exception exception;
    List<String> methodInfoList = new ArrayList<String>();

    public ApiExceptionWrapper(Exception e, String methodInfoList){
        this.exception = e;
        this.methodInfoList.add(methodInfoList);
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public List<String> getMethodInfoList() {
        return methodInfoList;
    }

    public void setMethodInfoList(List<String> methodInfoList) {
        this.methodInfoList = methodInfoList;
    }

}
