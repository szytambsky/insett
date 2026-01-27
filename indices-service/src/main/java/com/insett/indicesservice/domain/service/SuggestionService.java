package com.insett.indicesservice.domain.service;

import com.insett.indicesservice.api.dto.SuggestionRequestParameters;
import com.insett.indicesservice.domain.util.Constants;
import com.insett.indicesservice.domain.util.NativeQueryBuilder;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.suggest.response.Suggest;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SuggestionService {

    private static final Logger log = LoggerFactory.getLogger(SuggestionService.class);

    private final ElasticsearchOperations elasticsearchOperations;

    public List<String> fetchSuggestions(SuggestionRequestParameters parameters) {
        log.info("suggestion request: {}", parameters);
        NativeQuery query = NativeQueryBuilder.toSuggestQuery(parameters);
        SearchHits<Object> searchHits = this.elasticsearchOperations.search(query, Object.class, Constants.Index.SUGGESTION);
        return Optional.ofNullable(searchHits.getSuggest())
                .map(suggest -> suggest.getSuggestion(Constants.Suggestion.SUGGEST_NAME))
                .stream()
                .map(Suggest.Suggestion::getEntries)
                .flatMap(Collection::stream)
                .map(Suggest.Suggestion.Entry::getOptions)
                .flatMap(Collection::stream)
                .map(Suggest.Suggestion.Entry.Option::getText)
                .toList();
    }
}
