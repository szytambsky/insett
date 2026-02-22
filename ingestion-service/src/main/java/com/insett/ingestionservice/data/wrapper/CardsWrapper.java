package com.insett.ingestionservice.data.wrapper;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.insett.ingestionservice.data.dtos.CardDto;
import lombok.Data;

import java.util.List;
import java.util.Objects;

// todo: make ultimate wrapper items instead of property and then inheritance

@Data
public class CardsWrapper {

    @JsonProperty("items")
    private List<CardDto> cards;

    @JsonProperty("total")
    private Integer totalCards;

    @JsonProperty("page")
    private Integer pageNumber;

    @JsonProperty("size")
    private Integer pageSize;

    @JsonProperty("pages")
    private Integer totalPages;

    @Override
    public String toString() {
        return "CardsWrapper{" +
                ", totalCards=" + totalCards +
                ", pageNumber=" + pageNumber +
                ", pageSize=" + pageSize +
                ", totalPages=" + totalPages +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CardsWrapper that = (CardsWrapper) o;
        return Objects.equals(cards, that.cards)
                && Objects.equals(totalCards, that.totalCards)
                && Objects.equals(pageNumber, that.pageNumber)
                && Objects.equals(pageSize, that.pageSize)
                && Objects.equals(totalPages, that.totalPages);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cards, totalCards, pageNumber, pageSize, totalPages);
    }
}
