package org.example.api.exception;

public class ServiceObjectServiceException extends ServiceEntityException {
    public ServiceObjectServiceException(String message) {
        super(message);
    }

    public ServiceObjectServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
