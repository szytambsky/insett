package com.insett.ingestionservice.services;

import com.insett.ingestionservice.data.dtos.CardDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class CardIngestionService {

    private final RestClient restClient;

    public CardIngestionService(RestClient.Builder builder) {
        this.restClient = builder.baseUrl("https://api.riftcodex.com").build();
        CardDto singleCard = getSingleCard("bf1bafdc-2739-469b-bde6-c24a868f4979");
        List<CardDto> allCards = getAllCards();
    }

    public CardDto getSingleCard(String cardId) {
        return restClient
                .get()
                .uri("/cards/{cardId}", cardId)
                .retrieve()
                .body(CardDto.class);
    }

    public List<CardDto> getAllCards() {
        return restClient
                .get()
                .uri("/cards/search")
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    public CardDto createCard(CardDto cardDto) {
        return restClient
                .post()
                .uri("/cards")
                .contentType(MediaType.APPLICATION_JSON)
                .body(cardDto)
                .retrieve().
                body(CardDto.class);
    }
}
