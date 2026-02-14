package com.insett.ingestionservice.api.exceptions;

public class ExternalServiceException extends RuntimeException {

    public ExternalServiceException(String message) {
        super(message);
    }
}
