package com.insett.ingestionservice.services;

import com.insett.ingestionservice.data.dtos.CardDto;
import com.insett.ingestionservice.data.wrapper.CardsWrapper;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class CardIngestionProcessor {

    public static List<CardDto> unwrapCards(CardsWrapper cards) {
        return Collections.unmodifiableList(cards.getCards());
    }
}
