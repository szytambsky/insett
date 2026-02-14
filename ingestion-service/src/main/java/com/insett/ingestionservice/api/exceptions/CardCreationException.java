package com.insett.ingestionservice.api.exceptions;

public class CardCreationException extends RuntimeException {

    public CardCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
