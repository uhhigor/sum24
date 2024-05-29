package org.example.api.exception;

public class ServiceEntityNotFoundException extends ServiceEntityException{
    public ServiceEntityNotFoundException(String message) {
        super(message);
    }

    public ServiceEntityNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
