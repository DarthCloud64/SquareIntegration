package com.robert.reyes.payments.application.exceptions;

public class MissingProviderDetailsException extends RuntimeException{
    public MissingProviderDetailsException(String message) {
        super(message);
    }

    public MissingProviderDetailsException(String message, Throwable cause) {
        super(message, cause);
    }
}
