package com.insett.ingestionservice.api.exceptions;

public class CardNotFoundException extends RuntimeException {

    public CardNotFoundException(String cardId) {
        super("Card not found with id: " + cardId);
    }
}
