package com.insett.indicesservice.domain.exceptions;

public class BadRequestException extends RuntimeException {

    /**
     * Constructs a BadRequestException with the specified detail message.
     *
     * @param message the detail message describing the bad request
     */
    public BadRequestException(String message) {
        super(message);
    }
}