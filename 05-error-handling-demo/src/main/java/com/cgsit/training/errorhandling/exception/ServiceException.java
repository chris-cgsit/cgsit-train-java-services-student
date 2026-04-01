package com.cgsit.training.errorhandling.exception;

public abstract class ServiceException extends RuntimeException {

    private final int statusCode;

    protected ServiceException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
