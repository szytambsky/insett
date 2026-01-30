package com.insett.indicesservice.domain.util;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.search.Suggester;
import com.insett.indicesservice.api.dto.search.SearchRequestParameters;
import com.insett.indicesservice.api.dto.SuggestionRequestParameters;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;

import java.util.List;
import java.util.Optional;

public class NativeQueryBuilder {

    private static final List<QueryRule> FILTER_QUERY_RULES = List.of(
            QueryRules.STATE_QUERY,
            QueryRules.RATING_QUERY,
            QueryRules.DISTANCE_QUERY,
            QueryRules.OFFERINGS_QUERY
    );

    private static final List<QueryRule> MUST_QUERY_RULES = List.of(
            QueryRules.SEARCH_QUERY
    );

    private static final List<QueryRule> SHOULD_QUERY_RULES = List.of(
            QueryRules.CATEGORY_QUERY
    );

    /**
     * Builds a NativeQuery configured for autocomplete suggestions using the provided parameters.
     *
     * @param parameters request parameters containing the suggestion prefix and result limit
     * @return a NativeQuery containing a completion suggester named for suggestions, with no search hits returned and the source excluded
     */
    public static NativeQuery toSuggestQuery(SuggestionRequestParameters parameters) {
        Suggester suggester = ElasticsearchUtil.buildCompletionSuggester(Constants.Suggestion.SUGGEST_NAME,
                Constants.Suggestion.SEARCH_TERM, parameters.prefix(), parameters.limit());
        return NativeQuery.builder()
                .withSuggester(suggester)
                .withMaxResults(0)
                .withSourceFilter(FetchSourceFilter.of(b -> b.withExcludes("*")))
                .build();
    }

    /**
     * Builds an Elasticsearch NativeQuery for searching businesses using the provided request parameters.
     *
     * The resulting query uses a boolean query composed of filter, must, and should clauses derived from the request
     * parameters and predefined rule sets, includes a terms aggregation for offerings, applies pagination from the
     * request, and enables total hit tracking.
     *
     * @param parameters the search request parameters (controls filters, pagination, and other query options)
     * @return a configured NativeQuery containing the boolean query, offerings aggregation, pagination, and trackTotalHits enabled
     */
    public static NativeQuery toSearchQuery(SearchRequestParameters parameters) {
        List<Query> filterQueries = buildQueries(FILTER_QUERY_RULES, parameters);
        List<Query> mustQueries = buildQueries(MUST_QUERY_RULES, parameters);
        List<Query> shouldQueries = buildQueries(SHOULD_QUERY_RULES, parameters);
        BoolQuery boolQuery = BoolQuery.of(builder -> builder.filter(filterQueries)
                .must(mustQueries)
                .should(shouldQueries));
        return NativeQuery.builder()
                .withQuery(Query.of(builder -> builder.bool(boolQuery)))
                .withAggregation(Constants.Business.OFFERINGS_AGGREGATE_NAME,
                        ElasticsearchUtil.buildTermsAggregation(Constants.Business.OFFERINGS_RAW))
                .withPageable(PageRequest.of(parameters.page(), parameters.size()))
                .withTrackTotalHits(true)
                .build();
    }

    /**
     * Convert a list of QueryRule objects into concrete Query instances using the provided search parameters.
     *
     * Filters out any rules that do not produce a query.
     *
     * @param queryRules the rules to evaluate
     * @param parameters the search parameters used when building each rule's query
     * @return a list of Query objects produced by the rules; empty if no rule produced a query
     */
    private static List<Query> buildQueries(List<QueryRule> queryRules, SearchRequestParameters parameters) {
        return queryRules.stream()
                .map(queryRule -> queryRule.build(parameters))
                .flatMap(Optional::stream)
                .toList();
    }
}