package com.insett.indicesservice.domain.util;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.insett.indicesservice.api.dto.search.SearchRequestParameters;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

public record QueryRule(Predicate<SearchRequestParameters> predicate,
                        Function<SearchRequestParameters, Query> function) {

    /**
     * Create a QueryRule pairing a predicate with a function that produces an Elasticsearch Query from
     * SearchRequestParameters.
     *
     * @param predicate a condition evaluated against SearchRequestParameters to decide whether the rule applies
     * @param function  a mapping from SearchRequestParameters to an Elasticsearch {@code Query}
     * @return          a new {@code QueryRule} that applies the given predicate and function
     */
    public static QueryRule of(Predicate<SearchRequestParameters> predicate,
                               Function<SearchRequestParameters, Query> function) {
        return new QueryRule(predicate, function);
    }

    /**
     * Builds an Elasticsearch Query from the given search parameters when the rule's predicate matches.
     *
     * @param parameters search parameters to test and convert into a Query
     * @return an Optional containing the produced Query if the predicate matches the provided parameters, otherwise an empty Optional
     * @throws NullPointerException if {@code parameters} is null
     */
    public Optional<Query> build(SearchRequestParameters parameters) {
        return Optional.of(parameters)
                .filter(this.predicate())
                .map(this.function());
    }
}