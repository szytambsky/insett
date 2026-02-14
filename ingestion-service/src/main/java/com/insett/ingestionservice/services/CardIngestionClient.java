package com.insett.ingestionservice.services;

import com.insett.ingestionservice.data.dtos.CardDto;
import com.insett.ingestionservice.data.wrapper.CardsWrapper;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;


@HttpExchange(url = "https://api.riftcodex.com", accept = "application/json")
public interface CardIngestionClient {

    @GetExchange(url = "/cards")
    public CardsWrapper getAllCards();

    @GetExchange(url = "/cards/{cardId}")
    public CardDto getSingleCard(@PathVariable String cardId);

    @PostExchange(url = "/cards")
    public CardDto createCard(@RequestBody CardDto cardDto);
}
