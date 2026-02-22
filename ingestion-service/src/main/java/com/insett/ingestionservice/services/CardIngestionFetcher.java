package com.insett.ingestionservice.services;

import com.insett.ingestionservice.api.exceptions.CardCreationException;
import com.insett.ingestionservice.api.exceptions.CardNotFoundException;
import com.insett.ingestionservice.api.exceptions.ExternalServiceException;
import com.insett.ingestionservice.data.dtos.CardDto;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import javax.smartcardio.Card;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CardIngestionFetcher {

    private static final Logger log = LoggerFactory.getLogger(CardIngestionFetcher.class);
    private final CardIngestionClient cardIngestionClient;

    public List<CardDto> getAllCards() {
        try {
            return CardIngestionProcessor.unwrapCards(cardIngestionClient.getAllCards());
        } catch (Exception e) {
            log.error("Failed to fetch all cards: " + e.getMessage());
            return Collections.emptyList();
        }

    }

    public CardDto getCardById(String cardId) {
        try {
            return cardIngestionClient.getSingleCard(cardId);
        } catch (HttpClientErrorException.NotFound e) {
            throw new CardNotFoundException(cardId);
        } catch (Exception e) {
            log.error("Failed to fetch card with id {}. Error: {}", cardId, e.getMessage());
            throw new ExternalServiceException("Failed to fetch card");
        }
    }

    public CardDto createCard(CardDto cardDto) {
        try {
            return cardIngestionClient.createCard(cardDto);
        } catch (Exception e) {
            throw new CardCreationException(e.getMessage(), e.getCause());
        }
    }
}
