package com.insett.indicesservice.domain.util;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static com.insett.indicesservice.domain.util.Constants.Business.ADDRESS;
import static com.insett.indicesservice.domain.util.Constants.Business.CATEGORY;
import static com.insett.indicesservice.domain.util.Constants.Business.CATEGORY_RAW;
import static com.insett.indicesservice.domain.util.Constants.Business.DESCRIPTION;
import static com.insett.indicesservice.domain.util.Constants.Business.LOCATION;
import static com.insett.indicesservice.domain.util.Constants.Business.NAME;
import static com.insett.indicesservice.domain.util.Constants.Business.OFFERINGS;
import static com.insett.indicesservice.domain.util.Constants.Business.OFFERINGS_RAW;
import static com.insett.indicesservice.domain.util.Constants.Business.RATING;
import static com.insett.indicesservice.domain.util.Constants.Business.STATE;

public class QueryRules {

    public static final String BOOST_FIELD_FORMAT = "%s^%f";

    public static final QueryRule STATE_QUERY = QueryRule.of(
            searchRequestParameters ->
                    Objects.nonNull(searchRequestParameters.state()),
            searchRequestParameters ->
                    ElasticsearchUtil.buildTermQuery(STATE,
                            searchRequestParameters.state(),
                            1.0f)
    );

    public static final QueryRule OFFERINGS_QUERY = QueryRule.of(
            searchRequestParameters ->
                    Objects.nonNull(searchRequestParameters.offerings()),
            searchRequestParameters ->
                    ElasticsearchUtil.buildTermQuery(OFFERINGS_RAW,
                            searchRequestParameters.offerings(),
                            1.0f)
    );

    public static final QueryRule RATING_QUERY = QueryRule.of(
            searchRequestParameters ->
                    Objects.nonNull(searchRequestParameters.rating()),
            searchRequestParameters ->
                    ElasticsearchUtil.buildRangeQuery(RATING,
                            builder -> builder.gte(searchRequestParameters.rating()))
    );

    public static final QueryRule DISTANCE_QUERY = QueryRule.of(
            srp -> Stream.of(srp.distance(), srp.latitude(), srp.longitude())
                    .allMatch(Objects::nonNull),
            srp ->
                    ElasticsearchUtil.buildGeoDistanceQuery(LOCATION, srp.distance(), srp.latitude(), srp.longitude())
    );

    public static final QueryRule CATEGORY_QUERY = QueryRule.of(
            srp -> Objects.nonNull(srp.query()), // Predicate.isTrue() if not needed to check
            srp -> ElasticsearchUtil.buildTermQuery(CATEGORY_RAW, srp.query(), 5.0f)
    );

    public static final List<String> SEARCH_BOOST_FIELDS = List.of(
            boostField(NAME, 2.0f),
            boostField(CATEGORY, 1.5f),
            boostField(OFFERINGS, 1.5f),
            boostField(ADDRESS, 1.2f),
            DESCRIPTION
    );

    public static final QueryRule SEARCH_MULTI_MATCH_QUERY = QueryRule.of(
            srp -> Objects.nonNull(srp.query()),
            srp -> ElasticsearchUtil.buildMultiMatchQuery(SEARCH_BOOST_FIELDS, srp.query())
    );

    public static String boostField(String field, float boost) {
        return BOOST_FIELD_FORMAT.formatted(field, boost);
    }
}
