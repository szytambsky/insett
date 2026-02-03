package com.insett.ingestionservice.data.event;

public record CardFetchedEvent(String cardId,
                               String riftboundId) {
}
