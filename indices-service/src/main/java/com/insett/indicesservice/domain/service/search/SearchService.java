package com.insett.indicesservice.domain.service.search;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsAggregate;
import com.insett.indicesservice.api.dto.search.Business;
import com.insett.indicesservice.api.dto.search.Facet;
import com.insett.indicesservice.api.dto.search.FacetItem;
import com.insett.indicesservice.api.dto.search.Pagination;
import com.insett.indicesservice.api.dto.search.SearchRequestParameters;
import com.insett.indicesservice.api.dto.search.SearchResponse;
import com.insett.indicesservice.domain.util.Constants;
import com.insett.indicesservice.domain.util.NativeQueryBuilder;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchAggregation;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHitSupport;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.SearchPage;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.insett.indicesservice.domain.util.Constants.Business.OFFERINGS_AGGREGATE_NAME;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SearchService {

    private static final Logger log = LoggerFactory.getLogger(SearchService.class);

    private final ElasticsearchOperations elasticsearchOperations;

    /**
     * Execute a search using the given request parameters and return a structured response.
     *
     * @param parameters search criteria including filters, pagination, and sorting
     * @return a SearchResponse containing matching Business results, computed facets, pagination metadata, and the query execution duration in milliseconds
     */
    public SearchResponse search(SearchRequestParameters parameters) {
        log.info("Search request: {}", parameters);
        NativeQuery nativeQuery = NativeQueryBuilder.toSearchQuery(parameters);
        log.info("Bool query: {}", nativeQuery);
        SearchHits<Business> searchHits = elasticsearchOperations.search(nativeQuery, Business.class, Constants.Index.BUSINESS);
        return buildSearchResponse(parameters, searchHits);
    }

    /**
     * Builds a SearchResponse from raw search hits and the original request parameters.
     *
     * Assembles the list of Business results, pagination information (derived from the requested page and size),
     * facets extracted from aggregations, and the search execution duration in milliseconds.
     *
     * @param parameters request parameters used to determine pagination (page and size)
     * @param searchHits raw search hits and aggregations returned by Elasticsearch
     * @return a SearchResponse containing the results, facets, pagination, and execution duration in milliseconds
     */
    private SearchResponse buildSearchResponse(SearchRequestParameters parameters, SearchHits<Business> searchHits) {
        List<Business> results = searchHits.getSearchHits()
                .stream()
                .map(SearchHit::getContent)
                .toList();
        SearchPage<Business> searchPage = SearchHitSupport.searchPageFor(searchHits, PageRequest.of(parameters.page(), parameters.size()));
        Pagination pagination = new Pagination(
                searchPage.getNumber(),
                searchPage.getNumberOfElements(),
                searchPage.getTotalElements(),
                searchPage.getTotalPages());
        List<Facet> facets = buildFacets((List<ElasticsearchAggregation>) searchHits.getAggregations().aggregations());
        return new SearchResponse(results, facets, pagination, searchHits.getExecutionDuration().toMillis());
    }

    /**
     * Convert Elasticsearch aggregations into a list of Facet objects.
     *
     * @param aggregations the aggregations returned with search hits
     * @return a list of facets; currently contains the facet for {@code OFFERINGS_AGGREGATE_NAME}
     *         created from its string terms aggregation
     */
    private List<Facet> buildFacets(List<ElasticsearchAggregation> aggregations) {
        Map<String, Aggregate> map = aggregations.stream()
                .map(ElasticsearchAggregation::aggregation)
                .collect(Collectors.toMap(
                        singleFacet -> singleFacet.getName(),
                        singleFacet -> singleFacet.getAggregate()
                ));
        return List.of(
                buildFacet(OFFERINGS_AGGREGATE_NAME, map.get(OFFERINGS_AGGREGATE_NAME).sterms())
        );
    }

    /**
     * Builds a Facet from a string terms aggregation by converting each aggregation bucket
     * into a FacetItem and grouping them under the given facet name.
     *
     * @param name the facet name to assign to the resulting Facet
     * @param stringTermsAggregate the string terms aggregation containing buckets of term keys and document counts
     * @return a Facet containing FacetItems derived from the aggregation buckets
     */
    private Facet buildFacet(String name, StringTermsAggregate stringTermsAggregate) {
        List<FacetItem> facets = stringTermsAggregate.buckets()
                .array()
                .stream()
                .map(b -> new FacetItem(b.key().stringValue(), b.docCount()))
                .toList();
        return new Facet(name, facets);
    }
}