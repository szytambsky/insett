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

    /**
     * Create a Suggester that produces completion suggestions for a specific field and prefix.
     *
     * The returned suggester contains a field-level completion suggester configured to apply fuzzy
     * matching with the utility's fuzziness settings and to skip duplicate suggestions.
     *
     * @param suggestName the name under which the field suggester will be registered in the Suggester
     * @param field       the document field to target for completion suggestions
     * @param prefix      the input prefix to use for generating suggestions
     * @param limit       the maximum number of suggestion results to return
     * @return            a Suggester containing the configured FieldSuggester for the specified field and prefix
     */
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

    /**
     * Creates a term query for the specified field and value with case-insensitive matching and the given boost.
     *
     * @param field the document field to match
     * @param value the exact term value to match
     * @param boost relevancy boost to apply to this term
     * @return a Query containing the configured TermQuery
     */
    public static Query buildTermQuery(String field, String value, float boost) {
        TermQuery termQuery = TermQuery.of(termBuilder -> termBuilder.field(field)
                .value(value)
                .boost(boost)
                .caseInsensitive(true));
        return Query.of(queryBuilder -> queryBuilder.term(termQuery));
    }

    /**
     * Builds a numeric range Query for the specified field using the provided builder operator.
     *
     * @param field the document field to apply the numeric range to
     * @param unaryOperator a function that receives a `NumberRangeQuery.Builder` pre-configured with the field
     *                      and returns the builder after setting range bounds (for example `gt`, `gte`, `lt`, `lte`)
     * @return a `Query` containing the configured numeric range for the given field
     */
    public static Query buildRangeQuery(String field, UnaryOperator<NumberRangeQuery.Builder> unaryOperator) {
        NumberRangeQuery numberRangeQuery = NumberRangeQuery.of(builder ->
                unaryOperator.apply(builder.field(field)));
        RangeQuery rangeQuery = RangeQuery.of(builder -> builder.number(numberRangeQuery));
        return Query.of(builder -> builder.range(rangeQuery));
    }

    /**
     * Builds a geo-distance Query that matches documents within the specified distance of a latitude/longitude point.
     *
     * @param field the geo field name to query
     * @param distance the distance string (for example "12km" or "5mi")
     * @param latitude latitude of the center point
     * @param longitude longitude of the center point
     * @return a Query representing a geo-distance query for the given field and location
     */
    public static Query buildGeoDistanceQuery(String field, String distance, Double latitude, Double longitude) {
        LatLonGeoLocation latLonGeoLocation = LatLonGeoLocation.of(builder -> builder.lat(latitude)
                .lon(longitude));
        GeoLocation geoLocation = GeoLocation.of(builder -> builder.latlon(latLonGeoLocation));
        GeoDistanceQuery geoDistanceQuery = GeoDistanceQuery.of(builder -> builder.field(field)
                .distance(distance)
                .location(geoLocation));
        return Query.of(builder -> builder.geoDistance(geoDistanceQuery));
    }

    /**
     * Builds a multi-field text query that matches the given search term across the provided fields.
     *
     * The constructed MultiMatchQuery uses fuzziness and prefix length from Constants.Fuzziness,
     * sets the type to MostFields and the operator to AND.
     *
     * @param fields     the list of document fields to search
     * @param searchTerm the search term to match against the fields
     * @return           a Query wrapping a MultiMatchQuery configured for the provided fields and search term
     */
    public static Query buildMultiMatchQuery(List<String> fields, String searchTerm) {
        MultiMatchQuery multiMatchQuery = MultiMatchQuery.of(builder -> builder.fields(fields)
                .fuzziness(Constants.Fuzziness.LEVEL)
                .prefixLength(Constants.Fuzziness.PREFIX_LENGTH)
                .type(TextQueryType.MostFields)
                .operator(Operator.And)
                .query(searchTerm));
        return Query.of(builder -> builder.multiMatch(multiMatchQuery));
    }

    /**
     * Builds a terms aggregation on the specified document field with a fixed bucket size of 10.
     *
     * @param field the document field to aggregate on
     * @return an Aggregation containing a TermsAggregation for the given field limited to 10 buckets
     */
    public static Aggregation buildTermsAggregation(String field) {
        TermsAggregation termsAggregation = TermsAggregation.of(builder -> builder.field(field).size(10));
        return Aggregation.of(builder -> builder.terms(termsAggregation));
    }
}