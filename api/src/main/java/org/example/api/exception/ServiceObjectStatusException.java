package org.example.api.exception;

public class ServiceObjectStatusException extends ServiceObjectException {

    public ServiceObjectStatusException(String message) {
        super(message);
    }

    public ServiceObjectStatusException(String message, Throwable cause) {
        super(message, cause);
    }
}
