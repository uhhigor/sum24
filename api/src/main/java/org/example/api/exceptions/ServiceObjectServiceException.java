package org.example.api.exceptions;

public class ServiceObjectServiceException extends ServiceObjectException{
    public ServiceObjectServiceException(String message) {
        super(message);
    }

    public ServiceObjectServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
