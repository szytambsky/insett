package com.insett.ingestionservice.api;

import com.insett.ingestionservice.data.dtos.CardDto;
import com.insett.ingestionservice.services.CardIngestionFetcher;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/cards")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CardController {

    private final CardIngestionFetcher cardIngestionFetcher;

    @GetMapping()
    public ResponseEntity<List<CardDto>> getCards() {
        List<CardDto> allCards = cardIngestionFetcher.getAllCards();
        return ResponseEntity.ok(allCards);
    }

    @GetMapping("/{cardId}")
    public ResponseEntity<CardDto> getCard(@PathVariable String cardId) {
        CardDto singleCard = cardIngestionFetcher.getCardById(cardId);
        return ResponseEntity.ok(singleCard);
    }

    @PostMapping("/cards")
    public ResponseEntity<CardDto> createCard(@RequestBody CardDto cardDto) {
        CardDto newCard = cardIngestionFetcher.createCard(cardDto);
        return ResponseEntity.ok(newCard);
    }

}
