/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package au.com.suttons.notification.jobs.exception;

/**
 *
 * @author chris.lee
 */
public class JobProcessException extends RuntimeException {

    public JobProcessException(String message) {
        super(message);
    }

    public JobProcessException(String message, Throwable cause) {
        super(message, cause);
    }

}
