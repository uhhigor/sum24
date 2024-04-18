package org.example.api.exceptions;

public class ServiceStatusException extends ServiceException {

    public ServiceStatusException(String message) {
        super(message);
    }

    public ServiceStatusException(String message, Throwable cause) {
        super(message, cause);
    }
}
