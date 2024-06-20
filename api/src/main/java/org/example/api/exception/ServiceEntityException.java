package org.example.api.exception;

public class ServiceEntityException extends Exception {
    public ServiceEntityException(String message) {
        super(message);
    }
    public ServiceEntityException(String message, Throwable cause) {
        super(message, cause);
    }
}

