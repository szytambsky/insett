package com.insett.indicesservice.domain.util;

import co.elastic.clients.elasticsearch._types.GeoLocation;
import co.elastic.clients.elasticsearch._types.LatLonGeoLocation;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.aggregations.TermsAggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.GeoDistanceQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MultiMatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.NumberRangeQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Operator;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.TextQueryType;
import co.elastic.clients.elasticsearch.core.search.CompletionSuggester;
import co.elastic.clients.elasticsearch.core.search.FieldSuggester;
import co.elastic.clients.elasticsearch.core.search.SuggestFuzziness;
import co.elastic.clients.elasticsearch.core.search.Suggester;

import java.util.List;
import java.util.function.UnaryOperator;

public class ElasticsearchUtil {

    private ElasticsearchUtil() {}

    public static Suggester buildCompletionSuggester(String suggestName, String field, String prefix, int limit) {
        SuggestFuzziness suggestFuzziness = SuggestFuzziness.of(suggFuzzBuilder -> suggFuzzBuilder.fuzziness(Constants.Fuzziness.LEVEL)
                .prefixLength(Constants.Fuzziness.PREFIX_LENGTH));
        CompletionSuggester completionSuggester = CompletionSuggester.of(complBuilder -> complBuilder.field(field)
                .size(limit)
                .fuzzy(suggestFuzziness)
                .skipDuplicates(true));
        FieldSuggester fieldSuggester = FieldSuggester.of(fieldSuggBuilder -> fieldSuggBuilder.prefix(prefix)
                .completion(completionSuggester));
        return Suggester.of(suggBuilder -> suggBuilder.suggesters(suggestName, fieldSuggester));
    }

    public static Query buildTermQuery(String field, String value, float boost) {
        TermQuery termQuery = TermQuery.of(termBuilder -> termBuilder.field(field)
                .value(value)
                .boost(boost)
                .caseInsensitive(true));
        return Query.of(queryBuilder -> queryBuilder.term(termQuery));
    }

    public static Query buildRangeQuery(String field, UnaryOperator<NumberRangeQuery.Builder> unaryOperator) {
        NumberRangeQuery numberRangeQuery = NumberRangeQuery.of(builder ->
                unaryOperator.apply(builder.field(field)));
        RangeQuery rangeQuery = RangeQuery.of(builder -> builder.number(numberRangeQuery));
        return Query.of(builder -> builder.range(rangeQuery));
    }

    public static Query buildGeoDistanceQuery(String field, String distance, Double latitude, Double longitude) {
        LatLonGeoLocation latLonGeoLocation = LatLonGeoLocation.of(builder -> builder.lat(latitude)
                .lon(longitude));
        GeoLocation geoLocation = GeoLocation.of(builder -> builder.latlon(latLonGeoLocation));
        GeoDistanceQuery geoDistanceQuery = GeoDistanceQuery.of(builder -> builder.field(field)
                .distance(distance)
                .location(geoLocation));
        return Query.of(builder -> builder.geoDistance(geoDistanceQuery));
    }

    public static Query buildMultiMatchQuery(List<String> fields, String searchTerm) {
        MultiMatchQuery multiMatchQuery = MultiMatchQuery.of(builder -> builder.fields(fields)
                .fuzziness(Constants.Fuzziness.LEVEL)
                .prefixLength(Constants.Fuzziness.PREFIX_LENGTH)
                .type(TextQueryType.MostFields)
                .operator(Operator.And)
                .query(searchTerm));
        return Query.of(builder -> builder.multiMatch(multiMatchQuery));
    }

    public static Aggregation buildTermsAggregation(String field) {
        TermsAggregation termsAggregation = TermsAggregation.of(builder -> builder.field(field)
                .size(Constants.Size.AGGREGATION_BUCKETS_LIMIT));
        return Aggregation.of(builder -> builder.terms(termsAggregation));
    }
}
