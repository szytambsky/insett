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

    public SearchResponse search(SearchRequestParameters parameters) {
        log.info("Search request: {}", parameters);
        NativeQuery nativeQuery = NativeQueryBuilder.toSearchQuery(parameters); // todo:
        log.info("Bool query: {}", nativeQuery);
        SearchHits<Business> searchHits = elasticsearchOperations.search(nativeQuery, Business.class, Constants.Index.BUSINESS);
        return buildSearchResponse(parameters, searchHits);
    }

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

    private Facet buildFacet(String name, StringTermsAggregate stringTermsAggregate) {
        List<FacetItem> facets = stringTermsAggregate.buckets()
                .array()
                .stream()
                .map(b -> new FacetItem(b.key().stringValue(), b.docCount()))
                .toList();
        return new Facet(name, facets);
    }
}
