package com.insett.indicesservice.domain.util;

import co.elastic.clients.elasticsearch.core.search.Suggester;
import com.insett.indicesservice.api.dto.SuggestionRequestParameters;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;

public class NativeQueryBuilder {

    public static NativeQuery toSuggestQuery(SuggestionRequestParameters parameters) {
        Suggester suggester = ElasticsearchUtil.buildCompletionSuggester(Constants.Suggestion.SUGGEST_NAME,
                Constants.Suggestion.SEARCH_TERM, parameters.prefix(), parameters.limit());
        return NativeQuery.builder()
                .withSuggester(suggester)
                .withMaxResults(0)
                .withSourceFilter(FetchSourceFilter.of(b -> b.withExcludes("*")))
                .build();
    }
}
