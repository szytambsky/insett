package com.insett.indicesservice.query;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.aggregations.AggregationRange;
import co.elastic.clients.elasticsearch._types.aggregations.RangeAggregation;
import co.elastic.clients.elasticsearch._types.aggregations.StatsAggregation;
import co.elastic.clients.elasticsearch._types.aggregations.TermsAggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.NumberRangeQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import co.elastic.clients.elasticsearch.core.search.CompletionSuggester;
import co.elastic.clients.elasticsearch.core.search.FieldSuggester;
import co.elastic.clients.elasticsearch.core.search.Suggester;
import com.fasterxml.jackson.core.type.TypeReference;
import com.insett.indicesservice.AbstractIndicesServiceTests;
import com.insett.indicesservice.domain.dao.GarmentRepository;
import com.insett.indicesservice.entity.Garment;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchAggregation;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.suggest.response.Suggest;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class NativeAndCriteriaQueryTest extends AbstractIndicesServiceTests {

    @Autowired
    private GarmentRepository repository;

    @Autowired
    private ElasticsearchOperations operations;

    @BeforeAll
    public void dataSetup() {
        dataRemnantsAndRefresh();
        List<Garment> garments
                = super.readResource("data/garments.json", new TypeReference<List<Garment>>() {
        });
        repository.saveAll(garments);
        Assertions.assertEquals(20, repository.count());
    }

    public void dataRemnantsAndRefresh() {
        repository.deleteAll();
        operations.indexOps(Garment.class).refresh();
    }

    /**
     * Complex query programmatically with "and" "or" "not" conditions etc.
     * We might not be able to use the hard coded @Query. In those cases, criteria query could be useful.
     **/
    @Test
    public void criteriaQuery() {
        Criteria nameIsShirt = Criteria.where("name").is("shirt");
        this.verify(nameIsShirt, 1);
        Criteria priceAbove100 = Criteria.where("price").greaterThan(100);
        this.verify(priceAbove100, 5);
        this.verify(nameIsShirt.or(priceAbove100), 6);

        Criteria brandIsZara = Criteria.where("brand").is("Zara");
        this.verify(priceAbove100.and(brandIsZara.not()), 3);

        Criteria fuzzyMatch = Criteria.where("name").fuzzy("short");
        this.verify(fuzzyMatch, 1);

        // Boost and Geo Location
        Criteria boostCriteria = Criteria.where("brand").is("Zara").boost(3.0F);
        Criteria.where("location").within("53.551", "53.564"); // (point, radious)
    }

    private void verify(Criteria criteria, int expectedResultsCount) {
        CriteriaQuery query = CriteriaQuery.builder(criteria).build();
        SearchHits<Garment> search = this.operations.search(query, Garment.class);
        search.forEach(print());
        Assertions.assertEquals(expectedResultsCount, search.getTotalHits());
    }

    /**
     * {
     * "query": {
     * "bool": {
     * "filter": [
     * {
     * "term": {
     * "occasion": "Casual"
     * }
     * },
     * {
     * "range": {
     * "price": {
     * "lte": 50
     * }
     * }
     * }
     * ],
     * "should": [
     * {
     * "term": {
     * "color": "Brown"
     * }
     * }
     * ]
     * }
     * }
     * }
     */
    @Test
    public void boolQuery() {
        Query occasionCasual = Query.of(builder -> builder.term(
                TermQuery.of(termBuilder -> termBuilder
                        .field("occasion").value("Casual"))
        ));
        Query colorBrown = Query.of(builder -> builder.term(
                TermQuery.of(termBuilder -> termBuilder
                        .field("color").value("Brown"))
        ));
        Query priceBelowFifty = Query.of(builder -> builder.range(
                RangeQuery.of(rangeBuilder -> rangeBuilder.number(
                                NumberRangeQuery.of(numRangeBuilder -> numRangeBuilder
                                        .field("price").lte(50.0)
                                )
                        )
                )));
        Query boolQuery = Query.of(b -> b.bool(
                BoolQuery.of(bb -> bb.filter(occasionCasual, priceBelowFifty)
                        .should(colorBrown))
        ));
        NativeQuery nativeQuery = NativeQuery.builder()
                .withQuery(boolQuery)
                .build();
        SearchHits<Garment> searchHits = this.operations.search(nativeQuery, Garment.class);
        searchHits.forEach(print());
        Assertions.assertEquals(4, searchHits.getTotalHits());
    }

    /**
     * {
     * "size": 0,
     * "aggs": {
     * "price-stats": {
     * "stats": {
     * "field": "price"
     * }
     * },
     * "group-by-brand": {
     * "terms": {
     * "field": "brand"
     * }
     * },
     * "group-by-color": {
     * "terms": {
     * "field": "color"
     * }
     * },
     * "price-range": {
     * "range": {
     * "field": "price",
     * "ranges": [
     * {
     * "to": 50
     * },
     * {
     * "from": 50,
     * "to": 100
     * },
     * {
     * "from": 100,
     * "to": 150
     * },
     * {
     * "from": 150
     * }
     * ]
     * }
     * }
     * }
     * }
     */
    @Test
    public void aggregation() {
        Aggregation priceStats = Aggregation.of(b -> b.stats(
                StatsAggregation.of(sb -> sb.field("price"))
        ));
        Aggregation brandTerm = Aggregation.of(b -> b.terms(
                TermsAggregation.of(tb -> tb.field("brand"))
        ));
        Aggregation colorTerm = Aggregation.of(b -> b.terms(
                TermsAggregation.of(tb -> tb.field("color"))
        ));
        List<AggregationRange> ranges = List.of(
                AggregationRange.of(b -> b.to(50d)),
                AggregationRange.of(b -> b.from(50d).to(100d)),
                AggregationRange.of(b -> b.from(100d).to(150d)),
                AggregationRange.of(b -> b.from(150d))
        );
        Aggregation priceRange = Aggregation.of(b -> b.range(
                RangeAggregation.of(rb -> rb.field("price").ranges(ranges))
        ));

        NativeQuery nativeQuery = NativeQuery.builder()
                .withMaxResults(0)
                .withAggregation("price-stats", priceStats)
                .withAggregation("group-by-brand", brandTerm)
                .withAggregation("group-by-color", colorTerm)
                .withAggregation("price-range", priceRange)
                .build();

        SearchHits<Garment> searchHits = this.operations.search(nativeQuery, Garment.class);
        searchHits.forEach(print());
        List<ElasticsearchAggregation> aggregations = (List<ElasticsearchAggregation>) searchHits.getAggregations().aggregations();
        Map<String, Aggregate> aggregationsMap = aggregations.stream()
                .map(ElasticsearchAggregation::aggregation)
                .collect(Collectors.toMap(
                        aggregation -> aggregation.getName(),
                        aggregation -> aggregation.getAggregate())
                );

        this.print().accept(aggregationsMap);
        Assertions.assertEquals(4, aggregationsMap.size());

        Assertions.assertTrue(aggregationsMap.get("price-stats").isStats());
        Assertions.assertTrue(aggregationsMap.get("price-range").isRange());
        Assertions.assertTrue(aggregationsMap.get("group-by-brand").isSterms());
        Assertions.assertTrue(aggregationsMap.get("group-by-color").isSterms());

        if (aggregationsMap.get("group-by-brand").isSterms()) {
            aggregationsMap.get("group-by-brand").sterms()
                    .buckets()
                    .array()
                    .stream()
                    .map(b -> b.key().stringValue() + ":" + b.docCount())
                    .forEach(this.print());
        }
    }

    /**
     * {
     *   "suggest": {
     *     "product-suggest": {
     *       "prefix": "ca",
     *       "completion": {
     *           "field": "name.completion"
     *       }
     *     }
     *   },
     *   "_source": false
     * }
     */
    @Test
    public void suggestion() {
        FieldSuggester fieldSuggester = FieldSuggester.of(b -> b.prefix("ca")
                .completion(
                        CompletionSuggester.of(csb -> csb.field("name.completion")
                                .skipDuplicates(true)
                                .size(5)
                        )
                )
        );
        Suggester suggester = Suggester.of(b -> b.suggesters("product-suggest", fieldSuggester));
        NativeQuery nativeQuery = NativeQuery.builder()
                .withSuggester(suggester)
                .withMaxResults(0)
                .withSourceFilter(FetchSourceFilter.of(b -> b.withExcludes("*")))
                .build();
        SearchHits<Garment> searchHits = this.operations.search(nativeQuery, Garment.class);
        Assertions.assertNotNull(searchHits.getSuggest());
        Set<String> suggestions = searchHits.getSuggest().getSuggestion("product-suggest")
                .getEntries()
                .getFirst()
                .getOptions()
                .stream().map(Suggest.Suggestion.Entry.Option::getText)
                .collect(Collectors.toSet());
        Assertions.assertEquals(Set.of("Casual Wrap", "Casual Maxi"), suggestions);
    }
}
