/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package au.com.suttons.notification.interceptor;

import java.net.BindException;
import java.util.List;

import javax.ejb.EJBException;
import javax.persistence.OptimisticLockException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response.Status;

import org.hibernate.TypeMismatchException;

import au.com.suttons.notification.resource.bean.ResponseErrorBean;
import au.com.suttons.notification.resource.error.ApiExceptionWrapper;
import au.com.suttons.notification.resource.error.RestApiException;

public class ExceptionHandler {
    public ResponseErrorBean handleException(Throwable e, HttpServletRequest request, HttpServletResponse response)
    {
        ResponseErrorBean result;
        List<String> methodInfoList = null;

        if(e instanceof ApiExceptionWrapper){
            ApiExceptionWrapper apiExceptionWrapper = (ApiExceptionWrapper) e;
            methodInfoList = apiExceptionWrapper.getMethodInfoList();
            e = apiExceptionWrapper.getException();
        }

        if(e instanceof RestApiException)
        {
            //logger.warn("Service API threw an exception", e);
            return new ResponseErrorBean(request, response, (RestApiException)e, methodInfoList);
        }

        //400
        if(e instanceof TypeMismatchException || e instanceof BindException){
            result = new ResponseErrorBean(Status.BAD_REQUEST.getStatusCode(), request, response, e, methodInfoList);
            result.setMessage("One or more values do not match expected type.");
            return result;
        }

        // If e is an EJBException then something has gone wrong in the service or dao layers, and the original
        // exception has been wrapped up, we want to unwrap it and get the original exception.
        if (e instanceof EJBException) {
            Exception causedByException = ((EJBException) e).getCausedByException();
            
            if(getRootCause((EJBException)e) instanceof OptimisticLockException) {
                result = new ResponseErrorBean(Status.CONFLICT.getStatusCode(), request, response, e, methodInfoList);
                result.setMessage(ResponseErrorBean.VERSION_MISMATCH_MSG);
                return result;
            }

            if (causedByException != null) {
                return new ResponseErrorBean(Status.INTERNAL_SERVER_ERROR.getStatusCode(), request, response, causedByException, methodInfoList);
            }
        }

        return new ResponseErrorBean(Status.INTERNAL_SERVER_ERROR.getStatusCode(), request, response, e, methodInfoList);

    }

    public static Exception getRootCause ( EJBException exception )  
    {  
        if ( null == exception ) {  
          return null;  
        }  

        EJBException effect = exception;  
        Exception cause = effect.getCausedByException();  

        while ( null != cause  &&  cause instanceof EJBException ) {  
          effect = (EJBException) cause;  
          cause = effect.getCausedByException();  
        }  

        return null == cause ? effect : cause;  
    }
    
}
