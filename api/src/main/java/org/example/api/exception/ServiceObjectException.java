package org.example.api.exception;

public class ServiceObjectException extends Exception {
    public ServiceObjectException(String message) {
        super(message);
    }
    public ServiceObjectException(String message, Throwable cause) {
        super(message, cause);
    }
}

