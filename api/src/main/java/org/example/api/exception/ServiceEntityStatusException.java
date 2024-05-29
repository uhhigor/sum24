package org.example.api.exception;

public class ServiceEntityStatusException extends ServiceEntityException {

    public ServiceEntityStatusException(String message) {
        super(message);
    }

    public ServiceEntityStatusException(String message, Throwable cause) {
        super(message, cause);
    }
}
